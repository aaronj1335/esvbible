package aaronstacy.esvbible.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.broadcastIn
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.launch

private const val TAG = "Store"
private fun log(message: String) = log(TAG, message)
private fun <T> Flow<T>.log(message: String): Flow<T> = log(TAG, message)

private typealias StoreChangeCallback<S> = (S) -> Unit

interface Store<S, A> {
  fun dispatch(action: A): Job
  fun onChange(callback: StoreChangeCallback<S>): () -> Unit
  fun states(): Flow<S>
  val state: S
}

typealias Reducer<S, A> = (S, A) -> S

data class InternalStoreState<S, A>(
  val state: S,
  val action: A
) {
  override fun toString() = "InternalStoreState(action=$action, state=$state)"
}



private class StoreImplementation<S, A>(
  val scope: CoroutineScope,
  val reducer: Reducer<S, A>,
  initialState: S
) : Store<S, A> {
  @Volatile private var latest: S = initialState

  private val channel = ConflatedBroadcastChannel<InternalStoreState<S, A>>()
  private val flow by lazy {
    channel.asFlow()
      .log("Reducing").map { reducer(it.state, it.action) }
      .distinctUntilChanged()
      .log("New state").onEach { latest = it }
      .flowOn(IO)

      // Every subscription to a flow will typically re-run the whole flow, which would mean a
      // separate call to `reduce` for every subscription. This wouldn't be incorrect, but it would
      // be inefficient. A "shared" flow as described in the to-do below would solve this, but for
      // now we'll just use the hack of converting to a broadcast channel (essentially a hot flow),
      // and then back.
      //
      // TODO(https://github.com/Kotlin/kotlinx.coroutines/pull/1716): Use Flow#shareIn
      .broadcastIn(scope)
  }

  override fun dispatch(action: A) = scope.launch {
    log("Dispatching $action")
    channel.send(InternalStoreState(latest, action))
  }

  override fun onChange(callback: StoreChangeCallback<S>): () -> Unit {
    val job = flow.asFlow()
      .log("Firing onChange callback for ${callback.hashCode()}").onEach { callback(it) }
      .flowOn(Main)
      .produceIn(scope)
    return { job.cancel() }
  }

  override fun states() = flow.asFlow()

  override val state: S get() = latest
}

fun <S, A> CoroutineScope.createStore(reducer: Reducer<S, A>, initialState: S): Store<S, A> {
  return StoreImplementation(this, reducer, initialState)
}
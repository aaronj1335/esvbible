package aaronstacy.esvbible.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "Fetcher"

fun CoroutineScope.createFetcher(store: Store<State, Action>) = launch(Main) {
  // Since we're subscribing to a hot flow of states, we won't get the previously-emitted state at
  // the point when we subscribe. So create a flow from a channel that sends the current state and
  // and all subsequent states.
  channelFlow {
      send(store.state)
      store.states().collect(::send)
    }
    .log(TAG, "State update")
    .map {
      it.navigationStack
        .filterIsInstance<AppLocation.Read>()
        .last()
        .reference
    }
    .distinctUntilChanged()
    .log(TAG, "Fetching")
    .map { reference ->
      delay(700)
      Chapter(reference, (0..300).map { reference }.joinToString(" "))
    }
    .flowOn(IO)
    .log(TAG, "Fetched and dispatching")
    .collect { store.dispatch(AddChapter(it)) }
}
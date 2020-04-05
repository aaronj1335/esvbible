package aaronstacy.esvbible.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "Fetcher"

fun CoroutineScope.createFetcher(store: Store<State, Action>) = launch(Main) {
  store.states()
    .log(TAG, "State update")
    .flatMapConcat { it.requestedChapters.asFlow() }
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
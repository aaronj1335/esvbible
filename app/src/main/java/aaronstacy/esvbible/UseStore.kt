package aaronstacy.esvbible

import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.onPreCommit
import androidx.compose.setValue
import androidx.compose.state
import kotlinx.coroutines.Job

@Composable
fun <S, A, SS> UseStore(
  mapStateToProp: (S) -> SS,
  content: @Composable() (SS, (A) -> Job) -> Unit
) {
  val store = currentAmbientStore<Any, Any>()
  @Suppress("UNCHECKED_CAST") var currentStateMapping by state { mapStateToProp(store.state as S) }

  onPreCommit {
    val cancel = store.onChange {
      @Suppress("UNCHECKED_CAST") val newStateMapping = mapStateToProp(it as S)
      if (newStateMapping != currentStateMapping) currentStateMapping = newStateMapping
    }

    onDispose(cancel)
  }

  content(currentStateMapping) { store.dispatch(it as Any) }
}

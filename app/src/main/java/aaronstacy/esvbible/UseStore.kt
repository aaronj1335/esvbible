package aaronstacy.esvbible

import android.util.Log
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.onPreCommit
import androidx.compose.setValue
import androidx.compose.state
import kotlinx.coroutines.Job

@Composable
fun <S, A> UseStore(content: @Composable() (S, (A) -> Job) -> Unit) {
  val store = currentAmbientStore<Any, Any>()
  var currentState by state { store.state }

  onPreCommit {
    val cancel = store.onChange {
      currentState = it
    }

    onDispose {
      cancel()
    }
  }

  Log.w("EsvBibleUseStore", "Update UseStore to pull out subset of re-renderable state")

  @Suppress("UNCHECKED_CAST") content(currentState as S) { store.dispatch(it as Any) }
}

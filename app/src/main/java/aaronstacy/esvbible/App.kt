package aaronstacy.esvbible

import aaronstacy.esvbible.data.AppLocation
import aaronstacy.esvbible.data.Book
import aaronstacy.esvbible.data.Reference
import aaronstacy.esvbible.data.State
import aaronstacy.esvbible.data.createStore
import aaronstacy.esvbible.data.reducer
import androidx.compose.Composable
import androidx.ui.material.MaterialTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun App(scope: CoroutineScope, content: @Composable() () -> Unit) {
  MaterialTheme {
    ProvideCoroutineScope(scope) {
      ProvideStore(
        createStore = @Composable {
          AmbientScope.current.createStore(
            reducer,
            State(
              mapOf(),
              listOf(AppLocation.Read(Reference(Book.Genesis, 1, 1)))
            )
          )
        }
      ) {
        ProvideFetcher {
          content()
        }
      }
    }
  }
}
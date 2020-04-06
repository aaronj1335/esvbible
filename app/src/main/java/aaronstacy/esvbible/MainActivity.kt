package aaronstacy.esvbible

import aaronstacy.esvbible.data.Action
import aaronstacy.esvbible.data.Book
import aaronstacy.esvbible.data.Chapter
import aaronstacy.esvbible.data.Reference
import aaronstacy.esvbible.data.RequestChapter
import aaronstacy.esvbible.data.State
import aaronstacy.esvbible.data.createStore
import aaronstacy.esvbible.data.log
import aaronstacy.esvbible.data.reducer
import android.app.Activity
import android.os.Bundle
import androidx.compose.Composable
import androidx.compose.getValue
import androidx.compose.setValue
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.foundation.Icon
import androidx.ui.foundation.Text
import androidx.ui.layout.ColumnScope.weight
import androidx.ui.layout.Stack
import androidx.ui.material.BottomNavigation
import androidx.ui.material.BottomNavigationItem
import androidx.ui.material.MaterialTheme
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.filled.Favorite
import androidx.ui.material.icons.filled.Search
import androidx.ui.material.icons.filled.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

private fun indexToReference(index: Int) = when (index) {
  0 -> Reference(Book.Genesis, 1, 1)
  1 -> Reference(Book.Exodus, 1, 1)
  2 -> Reference(Book.Exodus, 2, 1)
  else -> throw RuntimeException("can't figure out index book for $index")
}

private const val TAG = "MainAc"

class MainActivity : Activity(), CoroutineScope by MainScope() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      MaterialTheme {
        ProvideCoroutineScope(this) {
          ProvideStore(
            createStore = @Composable {
              AmbientScope.current.createStore(reducer, State(mapOf(), setOf()))
            }
          ) {
            ProvideFetcher {
              UseStore<State, Action, Map<Reference, Chapter>>(
                mapStateToProp = { it.chapters }
              ) { chapters, dispatch ->
                log(TAG, "Re-rendering with new set of chapters $chapters")

                Stack {
                  var selectedItem by state { 0 }
                  val reference = indexToReference(selectedItem)
                  val text = chapters[reference]?.text ?: "Loading ${reference.displayName}"

                  dispatch(RequestChapter(reference))

                  Stack(Modifier.weight(1f, fill = true)) {
                    Text(text)
                  }

                  BottomNavigation(Modifier.gravity(Alignment.BottomCenter)) {
                    BottomNavigationItem(
                      { Icon(Icons.Filled.Favorite) },
                      selected = selectedItem == 0,
                      onSelected = { selectedItem = 0 }
                    )
                    BottomNavigationItem(
                      { Icon(Icons.Filled.Search) },
                      selected = selectedItem == 1,
                      onSelected = { selectedItem = 1 }
                    )
                    BottomNavigationItem(
                      { Icon(Icons.Filled.Settings) },
                      selected = selectedItem == 2,
                      onSelected = { selectedItem = 2 }
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    cancel()
  }
}
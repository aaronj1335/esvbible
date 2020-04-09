package aaronstacy.esvbible

import aaronstacy.esvbible.containers.Read
import aaronstacy.esvbible.ui.Nav
import aaronstacy.esvbible.ui.NavItem
import android.app.Activity
import android.os.Bundle
import androidx.ui.core.Modifier
import androidx.ui.core.setContent
import androidx.ui.layout.Column
import androidx.ui.layout.fillMaxWidth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class MainActivity : Activity(), CoroutineScope by MainScope() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      App(this) {
        Column {
          Read(Modifier.weight(1f, fill = true).fillMaxWidth())

          Nav(Modifier, NavItem.Read) { }
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    cancel()
  }
}
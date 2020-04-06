package aaronstacy.esvbible

import androidx.compose.Composable
import androidx.compose.Providers
import androidx.compose.ambientOf
import androidx.compose.onDispose
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val AmbientScope = ambientOf<CoroutineScope> {
  error("Scope has not been set. Use aaronstacy.esvbible.ProvideCoroutineScope.")
}

@Composable
fun ProvideCoroutineScope(scope: CoroutineScope, content: @Composable() () -> Unit) {
  var childScope: CoroutineScope? = null

  scope.launch(start = CoroutineStart.UNDISPATCHED) {
    childScope = this
    // Delaying keeps the associated scope alive so children can be launched from it.
    delay(1000000000000000000)
  }

  onDispose { childScope?.cancel() }

  Providers(AmbientScope provides childScope!!) {
    content()
  }
}
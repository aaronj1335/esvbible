package aaronstacy.esvbible

import androidx.compose.Composable
import androidx.compose.Providers
import androidx.compose.ambientOf
import kotlinx.coroutines.CoroutineScope

val AmbientScope = ambientOf<CoroutineScope> {
  error("Scope has not been set. Use aaronstacy.esvbible.ProvideCoroutineScope.")
}

@Composable
fun ProvideCoroutineScope(scope: CoroutineScope, content: @Composable() () -> Unit) {
  Providers(AmbientScope provides scope) {
    content()
  }
}
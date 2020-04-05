package aaronstacy.esvbible

import aaronstacy.esvbible.data.Store
import androidx.compose.Composable
import androidx.compose.Providers
import androidx.compose.ambientOf
import kotlinx.coroutines.CoroutineScope

val AmbientStore = ambientOf<Store<*, *>> {
  error("No Store defined. Please set one with ProvideStore.")
}

@Composable
fun <S, A> ProvideStore(
  createStore: @Composable() (CoroutineScope) -> Store<S, A>,
  content: @Composable() () -> Unit
) {
  Providers(AmbientStore provides createStore(AmbientScope.current)) {
    content()
  }
}

@Composable
@Suppress("UNCHECKED_CAST")
inline fun <reified S, reified A> currentAmbientStore() = AmbientStore.current as Store<S, A>
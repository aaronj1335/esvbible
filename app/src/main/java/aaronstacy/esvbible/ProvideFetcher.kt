package aaronstacy.esvbible

import aaronstacy.esvbible.data.createFetcher
import androidx.compose.Composable
import androidx.compose.Providers
import androidx.compose.ambientOf
import kotlinx.coroutines.Job

val AmbientFetcher = ambientOf<Job> {
  error("No fetcher defined. Use aaronstacy.esvbible.ProvideFetcher.")
}

@Composable
fun ProvideFetcher(content: @Composable() () -> Unit) {
  Providers(AmbientFetcher provides AmbientScope.current.createFetcher(currentAmbientStore())) {
    content()
  }
}
package aaronstacy.esvbible.ui

import aaronstacy.esvbible.data.Chapter
import androidx.compose.Composable
import androidx.ui.foundation.Text

@Composable
fun Chapter(chapter: Chapter) {
  Text(chapter.text)
}
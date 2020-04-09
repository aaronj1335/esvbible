package aaronstacy.esvbible.containers

import aaronstacy.esvbible.UseStore
import aaronstacy.esvbible.data.Action
import aaronstacy.esvbible.data.AppLocation
import aaronstacy.esvbible.data.Chapter
import aaronstacy.esvbible.data.NavigateTo
import aaronstacy.esvbible.data.Reference
import aaronstacy.esvbible.data.State
import aaronstacy.esvbible.ui.Chapter
import androidx.compose.Composable
import androidx.ui.core.Alignment
import androidx.ui.core.Modifier
import androidx.ui.foundation.Text
import androidx.ui.layout.Stack
import androidx.ui.material.Button

private data class ChapterWithNullableText(val reference: Reference, val text: String? = null)
private fun Chapter.asChapterWithNullableText() = ChapterWithNullableText(reference, text)

@Composable
fun Read(modifier: Modifier) {
  UseStore<State, Action, ChapterWithNullableText>(
    mapStateToProp = {
      val reference = it.navigationStack.filterIsInstance<AppLocation.Read>().last().reference
      it.chapters[reference]?.asChapterWithNullableText() ?: ChapterWithNullableText(reference)
    }
  ) { chapter, dispatch ->
//    dispatch(NavigateTo(AppLocation.Read(chapter.reference)))
    ReadUi(modifier, chapter.reference, chapter.text) { dispatch(NavigateTo(AppLocation.Read(it))) }
  }
}

@Composable
fun ReadUi(modifier: Modifier, reference: Reference, text: String?, onNavigateToNewChapter: (Reference) -> Unit) {
  Stack(modifier) {
    if (text != null) {
      Chapter(Chapter(reference, text))
    } else {
      Text("Loading ${reference.chapterName}")
    }

    if (!reference.isFirstChapter()) {
      Button(
        modifier = Modifier.gravity(Alignment.BottomStart),
        onClick = { onNavigateToNewChapter(reference.previousChapter()) }
      ) {
        Text("Previous")
      }
    }

    if (!reference.isLastChapter()) {
      Button(
        modifier = Modifier.gravity(Alignment.BottomEnd),
        onClick = { onNavigateToNewChapter(reference.nextChapter()) }
      ) {
        Text("Next")
      }
    }
  }
}
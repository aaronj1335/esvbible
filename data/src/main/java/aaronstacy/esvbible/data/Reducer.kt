package aaronstacy.esvbible.data

sealed class AppLocation {
  class Read(val reference: Reference) : AppLocation() {
    override fun toString() = "Read(${reference.chapterName})"
  }
}

data class State(
  val chapters: Map<Reference, Chapter>,
  val navigationStack: List<AppLocation>
) {
  private val requestedToString by lazy {
    navigationStack.reversed().joinToString()
  }

  override fun toString() = "State(${chapters.size} chapters, NavStack: $requestedToString)"
}

sealed class Action

data class NavigateTo(val location: AppLocation) : Action() {
  override fun toString() : String {
    return if (location is AppLocation.Read) {
      "NavigateTo(${location.reference.book} ${location.reference.chapter})"
    } else {
      "NavigateTo(${location.javaClass.name})"
    }
  }
}

data class AddChapter(val chapter: Chapter) : Action() {
  override fun toString() = "AddChapter(${chapter.reference.book} ${chapter.reference.chapter})"
}

val reducer: Reducer<State, Action> = { state, action ->
  when (action) {
    is NavigateTo ->
      if (state.navigationStack.last() == action.location)
        state
      else
        State(state.chapters, state.navigationStack + action.location)
    is AddChapter ->
      if (state.chapters.contains(action.chapter.reference))
        state
      else
        State(
            state.chapters + Pair(action.chapter.reference, action.chapter),
            state.navigationStack)
  }
}
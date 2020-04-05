package aaronstacy.esvbible.data

data class State(val chapters: Map<Reference, Chapter>, val requestedChapters: Set<Reference>) {
  private val requestedToString by lazy {
    requestedChapters.joinToString { "${it.book} ${it.chapter}" }
  }

  override fun toString() = "State(${chapters.size} chapters, requested: $requestedToString)"
}

sealed class Action

data class RequestChapter(val reference: Reference) : Action() {
  override fun toString() = "RequestChapter(${reference.book} ${reference.chapter})"
}

data class AddChapter(val chapter: Chapter) : Action() {
  override fun toString() = "AddChapter(${chapter.reference.book} ${chapter.reference.chapter})"
}

val reducer: Reducer<State, Action> = { state, action ->
  when (action) {
    is RequestChapter ->
      if (state.chapters.contains(action.reference))
        state
      else
        State(state.chapters, state.requestedChapters + action.reference)
    is AddChapter ->
      if (state.chapters.contains(action.chapter.reference))
        state
      else
        State(
            state.chapters + Pair(action.chapter.reference, action.chapter),
            state.requestedChapters - action.chapter.reference)
  }
}
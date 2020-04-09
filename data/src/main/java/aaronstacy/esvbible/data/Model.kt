package aaronstacy.esvbible.data

@Suppress("unused")
enum class Book(val chapterCount: Int, private val _displayName: String? = null) {
  Genesis(50),
  Exodus(40),
  Leviticus(27),
  Numbers(36),
  Deuteronomy(34),
  Joshua(24),
  Judges(21),
  Ruth(4),
  Samuel1(31, "1 Samuel"),
  Samuel2(24, "2 Samuel"),
  Kings1(22, "1 Kings"),
  Kings2(25, "2 Kings"),
  Chronicles1(29, "1 Chronicles"),
  Chronicles2(36, "2 Chronicles"),
  Ezra(10),
  Nehemiah(13),
  Esther(10),
  Job(42),
  Psalms(150),
  Proverbs(31),
  Ecclesiastes(12),
  SongOfSolomon(8, "Song of Solomon"),
  Isaiah(66),
  Jeremiah(52),
  Lamentations(5),
  Ezekiel(48),
  Daniel(12),
  Hosea(14),
  Joel(3),
  Amos(9),
  Obadiah(1),
  Jonah(4),
  Micah(7),
  Nahum(3),
  Habakkuk(3),
  Zephaniah(3),
  Haggai(2),
  Zechariah(14),
  Malachi(4),
  Matthew(28),
  Mark(16),
  Luke(24),
  John(21),
  Acts(28),
  Romans(16),
  Corinthians1(16, "1 Corinthians"),
  Corinthians2(13, "2 Corinthians"),
  Galatians(6),
  Ephesians(6),
  Philippians(4),
  Colossians(4),
  Thessalonians1(5, "1 Thessalonians"),
  Thessalonians2(3, "2 Thessalonians"),
  Timothy1(6, "1 Timothy"),
  Timothy2(4, "2 Timothy"),
  Titus(3),
  Philemon(1),
  Hebrews(13),
  James(5),
  Peter1(5, "1 Peter"),
  Peter2(3, "2 Peter"),
  John1(5, "1 John"),
  John2(1, "2 John"),
  John3(1, "3 John"),
  Jude(1),
  Revelation(2);

  val displayName: String get() = _displayName ?: name
}

private val booksArray by lazy { Book.values() }

data class Reference(val book: Book, val chapter: Int, val verse: Int) {
  fun isFirstChapter() = book === Book.Genesis && chapter == 1

  fun isLastChapter() = book === Book.Revelation && chapter == Book.Revelation.chapterCount

  fun nextChapter(): Reference {
    return if (chapter == book.chapterCount) {
      if (book.ordinal >= booksArray.size) {
        throw RuntimeException("Requested next chapter of last chapter: $displayName")
      } else {
        Reference(booksArray[book.ordinal + 1], 1, 1)
      }
    } else {
      Reference(book, chapter + 1, 1)
    }
  }

  fun previousChapter(): Reference {
    return if (chapter == 1) {
      if (book.ordinal == 0) {
        throw RuntimeException("Requested previous chapter of first: $displayName")
      } else {
        Reference(booksArray[book.ordinal - 1], 1, 1)
      }
    } else {
      Reference(book, chapter - 1, 1)
    }
  }

  init {
    check(chapter > 0) { "Reference chapter must be > 0, got $book $chapter:$verse." }
    check(chapter <= book.chapterCount) {
      "Reference chapter count must be < ${book.chapterCount}, got $book $chapter:$verse."
    }
    check(verse > 0) { "Verse must be > 0, got $book $chapter:$verse." }
  }

  val chapterName get() = "${book.displayName} $chapter"

  private val displayName get() = "$chapterName:$verse"
}

data class Chapter(val reference: Reference, val text: String)
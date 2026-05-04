package ru.shprot.sudokumobdevkz.core.base.domain.model

enum class Difficulty(
    val firebaseKey: Int,
    val visibleCells: Int,
    val emoji: String,
) {
    EASY(firebaseKey = 1, visibleCells = 40, emoji = "\uD83C\uDF3F"),
    MEDIUM(firebaseKey = 2, visibleCells = 30, emoji = "☀\uFE0F"),
    HARD(firebaseKey = 3, visibleCells = 27, emoji = "\uD83D\uDC51");

    companion object {
        fun fromFirebaseKey(key: Int): Difficulty? = entries.find { it.firebaseKey == key }
        fun fromOrdinal(ordinal: Int): Difficulty = entries.getOrElse(ordinal) { EASY }
    }
}

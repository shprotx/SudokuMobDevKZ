package ru.shprot.sudokumobdevkz.core.base.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "statistic_table")
data class StatisticEntity(
    @PrimaryKey val difficulty: Int,
    val allTime: Long = 0,
    val bestTime: Int = 0,
    val averageTime: Int = 0,
    val gamesStarted: Int = 0,
    val gamesWon: Int = 0,
    val percentOfWins: Int = 0,
    val winsWithoutErrors: Int = 0,
    val bestWinsLine: Int = 0,
    val currentWinsLine: Int = 0,
) {
    fun updated(win: Boolean, timeSeconds: Int, errorCount: Int): StatisticEntity {
        val newGamesStarted = gamesStarted + 1
        val newGamesWon = if (win) gamesWon + 1 else gamesWon
        val newAllTime = allTime + timeSeconds
        val newBestTime = when {
            !win -> bestTime
            bestTime == 0 -> timeSeconds
            else -> minOf(bestTime, timeSeconds)
        }
        val newAverageTime = if (newGamesWon > 0 && win) (newAllTime / newGamesWon).toInt() else averageTime
        val newPercent = if (newGamesStarted > 0) (100 * newGamesWon) / newGamesStarted else 0
        val newWinsWithoutErrors = if (win && errorCount == 0) winsWithoutErrors + 1 else winsWithoutErrors
        val newCurrentLine = if (win) currentWinsLine + 1 else 0
        val newBestLine = maxOf(bestWinsLine, newCurrentLine)

        return copy(
            allTime = newAllTime,
            bestTime = newBestTime,
            averageTime = newAverageTime,
            gamesStarted = newGamesStarted,
            gamesWon = newGamesWon,
            percentOfWins = newPercent,
            winsWithoutErrors = newWinsWithoutErrors,
            bestWinsLine = newBestLine,
            currentWinsLine = newCurrentLine,
        )
    }
}

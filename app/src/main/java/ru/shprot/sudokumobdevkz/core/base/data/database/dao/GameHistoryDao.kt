package ru.shprot.sudokumobdevkz.core.base.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity

@Dao
interface GameHistoryDao {

    @Insert
    suspend fun insert(entry: GameHistoryEntity)

    @Query("SELECT * FROM game_history_table WHERE difficulty = :difficulty ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentGames(difficulty: Int, limit: Int = 7): Flow<List<GameHistoryEntity>>

    @Query("SELECT * FROM game_history_table WHERE timestamp >= :sinceMs ORDER BY timestamp ASC")
    fun observeSince(sinceMs: Long): Flow<List<GameHistoryEntity>>

    @Query("SELECT * FROM game_history_table WHERE isWin = 1 ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecentWins(limit: Int): Flow<List<GameHistoryEntity>>

    @Query("SELECT * FROM game_history_table WHERE isWin = 1 ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentWins(limit: Int): List<GameHistoryEntity>

    @Query("SELECT * FROM game_history_table WHERE isWin = 1")
    suspend fun getAllWins(): List<GameHistoryEntity>

    @Query("SELECT * FROM game_history_table WHERE isWin = 1 AND isStandardMode = 1")
    suspend fun getAllStandardWins(): List<GameHistoryEntity>

    @Query("SELECT COUNT(*) FROM game_history_table WHERE isWin = 1 AND hintsUsed = 0")
    fun observeWinsWithoutHintsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM game_history_table WHERE isWin = 1 AND hintsUsed = 0")
    suspend fun countWinsWithoutHints(): Int

    @Query("DELETE FROM game_history_table WHERE difficulty = :difficulty")
    suspend fun deleteByDifficulty(difficulty: Int)
}

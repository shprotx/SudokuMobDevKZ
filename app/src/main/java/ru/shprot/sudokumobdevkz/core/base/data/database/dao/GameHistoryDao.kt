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

    @Query("DELETE FROM game_history_table WHERE difficulty = :difficulty")
    suspend fun deleteByDifficulty(difficulty: Int)
}

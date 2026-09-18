package ru.shprot.sudokumobdevkz.core.base.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.DailyPlaytimeEntity

@Dao
interface DailyPlaytimeDao {

    @Query(
        "INSERT INTO daily_playtime_table (dateKey, difficulty, totalSeconds) " +
            "VALUES (:dateKey, :difficulty, :seconds) " +
            "ON CONFLICT(dateKey, difficulty) DO UPDATE SET totalSeconds = totalSeconds + :seconds"
    )
    suspend fun addSeconds(dateKey: String, difficulty: Int, seconds: Int)

    @Query("SELECT * FROM daily_playtime_table ORDER BY dateKey ASC")
    fun observeAll(): Flow<List<DailyPlaytimeEntity>>

    @Query("SELECT * FROM daily_playtime_table ORDER BY dateKey ASC")
    suspend fun getAll(): List<DailyPlaytimeEntity>

    @Query("DELETE FROM daily_playtime_table WHERE difficulty = :difficulty")
    suspend fun deleteByDifficulty(difficulty: Int)
}
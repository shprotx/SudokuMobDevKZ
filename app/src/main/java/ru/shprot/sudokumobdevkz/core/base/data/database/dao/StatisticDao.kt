package ru.shprot.sudokumobdevkz.core.base.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.StatisticEntity

@Dao
interface StatisticDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(statistic: StatisticEntity)

    @Query("SELECT * FROM statistic_table WHERE difficulty = :difficulty")
    suspend fun getByDifficulty(difficulty: Int): StatisticEntity?

    @Query("SELECT * FROM statistic_table WHERE difficulty = :difficulty")
    fun observeByDifficulty(difficulty: Int): Flow<StatisticEntity?>

    @Query("SELECT * FROM statistic_table")
    fun observeAll(): Flow<List<StatisticEntity>>

    @Query("SELECT * FROM statistic_table")
    suspend fun getAll(): List<StatisticEntity>

    @Query("DELETE FROM statistic_table WHERE difficulty = :difficulty")
    suspend fun deleteByDifficulty(difficulty: Int)
}

package ru.shprot.sudokumobdevkz.core.base.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.DailyChallengeEntity

@Dao
interface DailyChallengeDao {

    @Query("SELECT * FROM daily_challenge_table WHERE dateKey = :dateKey")
    suspend fun getByDate(dateKey: String): DailyChallengeEntity?

    @Upsert
    suspend fun upsert(entity: DailyChallengeEntity)

    @Query("SELECT * FROM daily_challenge_table WHERE isCompleted = 1 ORDER BY dateKey DESC LIMIT :limit")
    suspend fun getRecentCompleted(limit: Int): List<DailyChallengeEntity>

    @Query("SELECT * FROM daily_challenge_table WHERE isCompleted = 1 ORDER BY dateKey ASC")
    suspend fun getAllCompletedAsc(): List<DailyChallengeEntity>

    @Query("SELECT * FROM daily_challenge_table WHERE isCompleted = 1")
    fun observeAllCompleted(): Flow<List<DailyChallengeEntity>>

    @Query("SELECT * FROM daily_challenge_table WHERE isCompleted = 1")
    suspend fun getAllCompleted(): List<DailyChallengeEntity>
}
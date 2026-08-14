package ru.shprot.sudokumobdevkz.core.base.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.AchievementUnlockedEntity

@Dao
interface AchievementUnlockedDao {

    @Query("SELECT * FROM achievement_unlocked_table")
    fun observeAll(): Flow<List<AchievementUnlockedEntity>>

    @Query("SELECT * FROM achievement_unlocked_table")
    suspend fun getAll(): List<AchievementUnlockedEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM achievement_unlocked_table WHERE id = :id)")
    suspend fun existsById(id: String): Boolean

    @Query("SELECT COUNT(*) FROM achievement_unlocked_table")
    suspend fun countUnlocked(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: AchievementUnlockedEntity)
}
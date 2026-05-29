package ru.shprot.sudokumobdevkz.core.base.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.CustomThemeEntity

@Dao
interface CustomThemeDao {

    @Query("SELECT * FROM custom_themes_table ORDER BY createdAt ASC")
    fun observeAll(): Flow<List<CustomThemeEntity>>

    @Query("SELECT * FROM custom_themes_table ORDER BY createdAt ASC")
    suspend fun getAll(): List<CustomThemeEntity>

    @Query("SELECT * FROM custom_themes_table WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): CustomThemeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: CustomThemeEntity)

    @Query("DELETE FROM custom_themes_table WHERE id = :id AND isBuiltIn = 0")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM custom_themes_table WHERE id = :id")
    suspend fun exists(id: String): Int
}
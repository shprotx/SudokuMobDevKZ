package ru.shprot.sudokumobdevkz.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.shprot.sudokumobdevkz.model.database.entity.SavedGameEntity

@Dao
interface SavedGameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(game: SavedGameEntity)

    @Query("SELECT * FROM saved_game_table WHERE id = 0")
    suspend fun get(): SavedGameEntity?

    @Query("DELETE FROM saved_game_table")
    suspend fun delete()
}

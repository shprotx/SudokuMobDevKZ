package ru.shprot.sudokumobdevkz.model.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Completable;
import ru.shprot.sudokumobdevkz.model.game.GameState;

@Dao
public interface GameStateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertGameState(GameState gameState);

    @Query("SELECT * FROM game_state_table WHERE id = :id")
    LiveData<GameState> getGameState(int id);

    @Query("DELETE FROM game_state_table")
    Completable deleteGameState();
}

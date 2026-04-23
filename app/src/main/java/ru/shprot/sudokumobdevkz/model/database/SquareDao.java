package ru.shprot.sudokumobdevkz.model.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ru.shprot.sudokumobdevkz.model.game.Square;

@Dao
public interface SquareDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSquares(List<Square> items);

    @Query("SELECT * FROM square_table")
    List<Square> getAllSquares();

    @Query("DELETE FROM square_table")
    void deleteSquares();
}

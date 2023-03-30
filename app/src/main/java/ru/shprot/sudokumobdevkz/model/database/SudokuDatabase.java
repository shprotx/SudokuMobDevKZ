package ru.shprot.sudokumobdevkz.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.shprot.sudokumobdevkz.model.game.GameState;
import ru.shprot.sudokumobdevkz.model.game.Square;
import ru.shprot.sudokumobdevkz.model.game.Statistic;

@Database(entities = {Square.class, GameState.class, Statistic.class}, version = 1)
public abstract class SudokuDatabase extends RoomDatabase {

    private static volatile SudokuDatabase INSTANCE;

    public static synchronized SudokuDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, SudokuDatabase.class, "sudoku_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract SquareDao squareDao();
    public abstract GameStateDao gameStateDao();

    public abstract StatisticDao statisticDao();

}

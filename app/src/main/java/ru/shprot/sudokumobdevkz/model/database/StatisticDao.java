package ru.shprot.sudokumobdevkz.model.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import ru.shprot.sudokumobdevkz.model.game.Statistic;

@Dao
public interface StatisticDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStatistic(Statistic statistic);

    @Query("SELECT * FROM statistic_table WHERE difficulty = :difficulty")
    Statistic getStatistic(int difficulty);

    @Query("DELETE FROM statistic_table WHERE difficulty = :difficulty")
    void removeCurrentStatistic(int difficulty);
}

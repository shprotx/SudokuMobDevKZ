package ru.shprot.sudokumobdevkz.model.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import ru.shprot.sudokumobdevkz.model.game.Statistic;

@Dao
public interface StatisticDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertStatistic(Statistic statistic);

    @Query("SELECT * FROM statistic_table WHERE difficulty = :difficulty")
    Maybe<Statistic> getStatistic(int difficulty);

    @Query("DELETE FROM statistic_table WHERE difficulty = :difficulty")
    Completable removeCurrentStatistic(int difficulty);
}

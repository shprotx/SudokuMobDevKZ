package ru.shprot.sudokumobdevkz.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import io.reactivex.Maybe;
import ru.shprot.sudokumobdevkz.model.Repository;
import ru.shprot.sudokumobdevkz.model.game.Statistic;

public class StatisticViewModel extends AndroidViewModel {

    Repository repository;

    public StatisticViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }


    public Maybe<Statistic> getStatistic(int difficulty) {
        return repository.getStatistic(difficulty);
    }

    public void removeCurrentStatistic(int difficulty) {
        repository.removeCurrentStatistic(difficulty);
    }
}

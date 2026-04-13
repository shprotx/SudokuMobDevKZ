package ru.shprot.sudokumobdevkz.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.shprot.sudokumobdevkz.model.database.GameStateDao;
import ru.shprot.sudokumobdevkz.model.database.SquareDao;
import ru.shprot.sudokumobdevkz.model.database.StatisticDao;
import ru.shprot.sudokumobdevkz.model.database.SudokuDatabase;
import ru.shprot.sudokumobdevkz.model.game.GameState;
import ru.shprot.sudokumobdevkz.model.game.Square;
import ru.shprot.sudokumobdevkz.model.game.Statistic;

public class Repository {

    private final SquareDao mSquareDao;
    private final GameStateDao mGameStateDao;
    private final StatisticDao mStatisticDao;


    private static final String TAG = "Repository";
    private static final Consumer<Throwable> ERROR_HANDLER =
            e -> Log.e(TAG, "DB operation failed", e);

    public Repository(Application application) {
        SudokuDatabase db = SudokuDatabase.getDatabase(application);
        mSquareDao = db.squareDao();
        mGameStateDao = db.gameStateDao();
        mStatisticDao = db.statisticDao();
    }


    public void removeCurrentStatistic(int difficulty) {
        mStatisticDao.removeCurrentStatistic(difficulty)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, ERROR_HANDLER);
    }
    public void insertStatistic(Statistic statistic) {
        mStatisticDao.insertStatistic(statistic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, ERROR_HANDLER);
    }

    public Maybe<Statistic> getStatistic(int difficulty) {
        return mStatisticDao.getStatistic(difficulty);
    }
    public LiveData<GameState> getGameState() {
        return mGameStateDao.getGameState(0);
    }
    public Single<List<Square>> getItems() {
        return mSquareDao.getAllSquares();
    }

    public void insertItems(List<Square> items) {
        mSquareDao.insertSquares(items)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, ERROR_HANDLER);
    }

    public void insertState(GameState gameState) {
        mGameStateDao.insertGameState(gameState)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, ERROR_HANDLER);
    }

    public void deleteStateFromDb() {
        mGameStateDao.deleteGameState().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, ERROR_HANDLER);
    }

    public void deleteGridFromDb() {
        mSquareDao.deleteSquares().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {}, ERROR_HANDLER);
    }
}

package ru.shprot.sudokumobdevkz.model;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private static final String TAG = "Repository";

    public Repository(Application application) {
        SudokuDatabase db = SudokuDatabase.getDatabase(application);
        mSquareDao = db.squareDao();
        mGameStateDao = db.gameStateDao();
        mStatisticDao = db.statisticDao();
    }


    public void removeCurrentStatistic(int difficulty) {
        executor.execute(() -> {
            try {
                mStatisticDao.removeCurrentStatistic(difficulty);
            } catch (Exception e) {
                Log.e(TAG, "DB operation failed", e);
            }
        });
    }

    public void insertStatistic(Statistic statistic) {
        executor.execute(() -> {
            try {
                mStatisticDao.insertStatistic(statistic);
            } catch (Exception e) {
                Log.e(TAG, "DB operation failed", e);
            }
        });
    }

    public interface StatisticCallback {
        void onResult(Statistic statistic);
    }

    public void getStatistic(int difficulty, StatisticCallback callback) {
        executor.execute(() -> {
            try {
                Statistic result = mStatisticDao.getStatistic(difficulty);
                mainHandler.post(() -> callback.onResult(result));
            } catch (Exception e) {
                Log.e(TAG, "DB operation failed", e);
                mainHandler.post(() -> callback.onResult(null));
            }
        });
    }

    public LiveData<GameState> getGameState() {
        return mGameStateDao.getGameState(0);
    }

    public interface ItemsCallback {
        void onResult(List<Square> items);
    }

    public void getItems(ItemsCallback callback) {
        executor.execute(() -> {
            try {
                List<Square> result = mSquareDao.getAllSquares();
                mainHandler.post(() -> callback.onResult(result));
            } catch (Exception e) {
                Log.e(TAG, "DB operation failed", e);
                mainHandler.post(() -> callback.onResult(null));
            }
        });
    }

    public void insertItems(List<Square> items) {
        executor.execute(() -> {
            try {
                mSquareDao.insertSquares(items);
            } catch (Exception e) {
                Log.e(TAG, "DB operation failed", e);
            }
        });
    }

    public void insertState(GameState gameState) {
        executor.execute(() -> {
            try {
                mGameStateDao.insertGameState(gameState);
            } catch (Exception e) {
                Log.e(TAG, "DB operation failed", e);
            }
        });
    }

    public void deleteStateFromDb() {
        executor.execute(() -> {
            try {
                mGameStateDao.deleteGameState();
            } catch (Exception e) {
                Log.e(TAG, "DB operation failed", e);
            }
        });
    }

    public void deleteGridFromDb() {
        executor.execute(() -> {
            try {
                mSquareDao.deleteSquares();
            } catch (Exception e) {
                Log.e(TAG, "DB operation failed", e);
            }
        });
    }
}

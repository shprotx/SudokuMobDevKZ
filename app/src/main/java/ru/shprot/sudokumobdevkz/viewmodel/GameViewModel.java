package ru.shprot.sudokumobdevkz.viewmodel;

import static ru.shprot.sudokumobdevkz.model.game.utils.Library.FLAG_HINTS;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.FLAG_MISTAKES;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.SETTINGS_PREFS;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ru.shprot.sudokumobdevkz.model.Repository;
import ru.shprot.sudokumobdevkz.model.game.GameState;
import ru.shprot.sudokumobdevkz.model.game.Square;
import ru.shprot.sudokumobdevkz.model.game.Statistic;
import ru.shprot.sudokumobdevkz.model.game.utils.SquareAdapter;

public class GameViewModel extends AndroidViewModel {

    private final Repository repository;
    public ArrayList<Square> items;
    public SquareAdapter adapter;
    public HashMap<Integer, Integer> backup = new HashMap<>();
    public java.util.Timer timer;
    public TextView[] buttons;
    public GameState gameState;

    public Statistic statistic;

    public int possibleHints = 1;

    public int possibleMistakes = 3;
    public boolean isGameRestarted = false;
    public boolean isGameContinued = false;
    public boolean isWin;



    public GameViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        getPreferences();
    }



    public void getPreferences() {
        SharedPreferences sharedPreferences = getApplication()
                .getSharedPreferences(SETTINGS_PREFS, Context.MODE_PRIVATE);
        boolean hints = sharedPreferences.getBoolean(FLAG_HINTS, false);
        boolean mistakes = sharedPreferences.getBoolean(FLAG_MISTAKES, false);
        if (hints) possibleHints = Integer.MAX_VALUE;
        else possibleHints = 1;
        if (mistakes) possibleMistakes = Integer.MAX_VALUE;
        else possibleMistakes = 3;
    }


    public void insertStatistic(Statistic statistic) {
        repository.insertStatistic(statistic);
        repository.syncStatisticToFirebase(getApplication(), statistic);
    }
    public void getStatisticFromDb(int difficulty) {
        repository.getStatistic(difficulty, result -> {
            if (result != null) {
                statistic = result;
            } else {
                statistic = new Statistic(difficulty);
            }
        });
    }
    public void insertGridToDb(List<Square> list) {
        repository.insertItems(list);
    }

    public void insertStateToDb(GameState gameState) {
        repository.insertState(gameState);
    }

    public void deleteGridFromDb() {
        repository.deleteGridFromDb();
    }

    public void deleteStateFromDb() {
        repository.deleteStateFromDb();
    }





    public void calculateTimerStartValue(String time)  {
        String[] s = time.split(":");
        if (s.length == 3) {
            int hours = Integer.parseInt(s[0]);
            int minutes = Integer.parseInt(s[1]);
            int seconds = Integer.parseInt(s[2]);
            gameState.setTime(hours * 3600 + minutes * 60 + seconds);
        } else if (s.length == 2) {
            int minutes = Integer.parseInt(s[0]);
            int seconds = Integer.parseInt(s[1]);
            gameState.setTime(minutes * 60 + seconds);
        }
    }
    public String getTimerString(long timer) {
        int totalSeconds = (int) timer + gameState.getTime();
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        if (hours == 0)
            return String.format("%02d:%02d", minutes, seconds);
        else
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}

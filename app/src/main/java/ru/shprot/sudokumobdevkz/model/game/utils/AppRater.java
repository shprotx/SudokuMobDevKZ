package ru.shprot.sudokumobdevkz.model.game.utils;

import static ru.shprot.sudokumobdevkz.model.game.utils.Library.RATE_PREFS;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.RATE_PREFS_DATE;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.RATE_PREFS_INTERVAL;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.RATE_PREFS_LAUNCH;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.RATE_PREFS_NEVER;

import android.content.Context;
import android.content.SharedPreferences;

public class AppRater {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public AppRater(Context context) {
        this.sharedPreferences = context.getSharedPreferences(RATE_PREFS, 0);
        this.editor = sharedPreferences.edit();
    }



    public boolean app_launched() {
        if (sharedPreferences.getBoolean(RATE_PREFS_NEVER, false)) return false;

        long launchCounter = sharedPreferences.getLong(RATE_PREFS_LAUNCH, 0) + 1;
        editor.putLong(RATE_PREFS_LAUNCH, launchCounter);

        int interval = sharedPreferences.getInt(RATE_PREFS_INTERVAL, 10);

        long firstLaunchDate = sharedPreferences.getLong(RATE_PREFS_DATE, 0);
        if (firstLaunchDate == 0) {
            firstLaunchDate = System.currentTimeMillis();
            editor.putLong(RATE_PREFS_DATE, firstLaunchDate);
        }

        editor.apply();

        if (launchCounter >= 15)
            return System.currentTimeMillis() > getDate(firstLaunchDate, interval);
        else
            return false;
    }

    private long getDate(long firstDate, int interval) {
        return firstDate * interval * 24 * 60 * 60 * 1000;
    }

    public void applyNeverShowRate() {
        editor.putBoolean(RATE_PREFS_NEVER, true);
        editor.apply();
    }

    public void increaseInterval() {
        long interval = sharedPreferences.getLong(RATE_PREFS_INTERVAL, 10) * 2;
        editor.putLong(RATE_PREFS_LAUNCH, interval);
        editor.apply();
    }
}

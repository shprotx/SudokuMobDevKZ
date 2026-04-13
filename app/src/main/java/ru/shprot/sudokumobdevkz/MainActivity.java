package ru.shprot.sudokumobdevkz;

import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_LANGUAGE_DEFAULT_VALUE;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_LANGUAGE_KEY;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME_ID;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME_INT;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.SETTINGS_PREFS;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Locale;

import ru.shprot.sudokumobdevkz.model.game.utils.AppRater;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String currentLocale;

    public static boolean needToShowRate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences(SETTINGS_PREFS, MODE_PRIVATE);
        //updateLocale();
        updateTheme();
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        View rootView = findViewById(android.R.id.content);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

        AppRater appRater = new AppRater(this);
        needToShowRate = appRater.app_launched();



    }

    private void updateLocale() {
        String lang = sharedPreferences.getString(CURRENT_LANGUAGE_KEY, CURRENT_LANGUAGE_DEFAULT_VALUE);
        currentLocale = lang;
        if (!lang.equals(CURRENT_LANGUAGE_DEFAULT_VALUE)) {
            Configuration configuration = new Configuration();
            Locale locale = new Locale(lang);
            configuration.setLocale(locale);
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }
    }

    private void updateTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(CURRENT_THEME, MODE_PRIVATE);
        int mode = sharedPreferences.getInt(CURRENT_THEME_INT, 0);
        int theme = sharedPreferences.getInt(CURRENT_THEME_ID, R.style.Theme_SudokuMobDevKZ);
        if (mode != 0) {
            AppCompatDelegate.setDefaultNightMode(mode);
        }
        setTheme(theme);
    }

}
package ru.shprot.sudokumobdevkz;

import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME_ID;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME_INT;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.shprot.sudokumobdevkz.databinding.ActivityFirstBinding;
import ru.shprot.sudokumobdevkz.model.game.utils.Library;

public class FirstActivity extends AppCompatActivity {


    private Disposable count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateTheme();
        super.onCreate(savedInstanceState);
        ActivityFirstBinding binding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_logo);

        binding.logoText.startAnimation(animation);



        count = Observable.interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong == 1) {
                        startActivity(new Intent(FirstActivity.this, MainActivity.class));
                    }
                });

        determineLocale();

    }

    private void updateTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(CURRENT_THEME, MODE_PRIVATE);
        int mode = sharedPreferences.getInt(CURRENT_THEME_INT, 0);
        int theme = sharedPreferences.getInt(CURRENT_THEME_ID, R.style.Theme_SudokuMobDevKZ);
        if (mode != 0) {
            AppCompatDelegate.setDefaultNightMode(mode);
        }
        setTheme(theme);
        if (Build.VERSION.SDK_INT >= 27)
            getWindow().setDecorCaptionShade(Window.DECOR_CAPTION_SHADE_LIGHT);

    }

    @Override
    protected void onStop() {
        super.onStop();
        count.dispose();
    }

    private void determineLocale() {
        SharedPreferences sharedPreferences = getSharedPreferences(Library.SETTINGS_PREFS, MODE_PRIVATE);
        String currentLocale = sharedPreferences.getString(Library.CURRENT_LANGUAGE_KEY
                , Library.CURRENT_LANGUAGE_DEFAULT_VALUE);
        if (currentLocale.equals(Library.CURRENT_LANGUAGE_DEFAULT_VALUE)) {
            Configuration config = Resources.getSystem().getConfiguration();
            String locale = config.locale.toString();
            String codeLang = locale.substring(0,2);
            String[] possibleLocales = getResources().getStringArray(R.array.languages_val);
            for (String possibleLocale : possibleLocales)
                if (possibleLocale.equals(codeLang)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Library.CURRENT_LANGUAGE_KEY, codeLang);
                    editor.apply();
                }
        }
    }
}
package ru.shprot.sudokumobdevkz.ui;

import static android.content.Context.MODE_PRIVATE;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.APP_PACKAGE_NAME;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_LANGUAGE_DEFAULT_VALUE;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME_ID;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME_INT;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.DIALOG_PAUSE;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.FLAG_HINTS;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.FLAG_MISTAKES;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.FLAG_REMOVE_ADS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import java.util.Locale;

import ru.shprot.sudokumobdevkz.R;
import ru.shprot.sudokumobdevkz.databinding.FragmentSettingsBinding;
import ru.shprot.sudokumobdevkz.model.game.utils.LangAdapter;
import ru.shprot.sudokumobdevkz.model.game.utils.Library;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding b;
    private String currentLang;
    private String[] lang_values;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isStateChanged = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnBackPressedDispatcher back = getActivity().getOnBackPressedDispatcher();
        back.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                changeOccurred(isStateChanged);
            }
        });
        sharedPreferences = getActivity().getSharedPreferences(Library.SETTINGS_PREFS, MODE_PRIVATE);
        updateLocale();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        b = FragmentSettingsBinding.bind(inflater.inflate(R.layout.fragment_settings,container,false));
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setFlags();
        addListeners();
        initSpinner();
        addListenersToSpinner();

    }




    private void initSpinner() {
        lang_values = getResources().getStringArray(R.array.languages_val);
        LangAdapter adapter = new LangAdapter(getContext(),
                R.layout.lang_layout, getResources().getStringArray(R.array.languages));
        b.langSpinner.setAdapter(adapter);
        for (int i = 0; i < lang_values.length; i++)
            if (lang_values[i].equals(currentLang))
                b.langSpinner.setSelection(i);
    }

    private void addListeners() {
        b.buttonBackFromSettings.setOnClickListener(v -> {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).popBackStack();
                changeOccurred(isStateChanged);
                });
        b.rateButton.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri
                .parse("market://details?id=" + APP_PACKAGE_NAME))));
        b.cardLightTheme.setOnClickListener(v -> applyTheme(AppCompatDelegate.MODE_NIGHT_NO, R.style.Theme_MyLight));
        b.cardDarkTheme.setOnClickListener(v -> applyTheme(AppCompatDelegate.MODE_NIGHT_YES, R.style.Theme_SudokuMobDevKZ));
        b.cardGreenTheme.setOnClickListener(v -> applyTheme(AppCompatDelegate.MODE_NIGHT_NO, R.style.Theme_SudokuMobDevKZ));

        b.hintCheckBox.setOnClickListener(v -> onUnlimitedHintsSelected());
        b.mistakeCheckBox.setOnClickListener(v -> onUnlimitedMistakesSelected());
        b.supportButton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            UpvoteDialog dialog = new UpvoteDialog();
            dialog.show(fragmentManager, DIALOG_PAUSE);
        });
    }


    private void addListenersToSpinner() {
        editor = sharedPreferences.edit();
        b.langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!currentLang.equals(lang_values[position])) {
                    editor.putString(Library.CURRENT_LANGUAGE_KEY, lang_values[position]);
                    editor.apply();
                    currentLang = lang_values[position];
                    getActivity().recreate();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateLocale() {
        currentLang = sharedPreferences.getString(Library.CURRENT_LANGUAGE_KEY
                , CURRENT_LANGUAGE_DEFAULT_VALUE);
        if (!currentLang.equals(CURRENT_LANGUAGE_DEFAULT_VALUE)) {
            Configuration configuration = new Configuration();
            Locale locale = new Locale(currentLang);
            configuration.setLocale(locale);
            getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
        }
    }



    private void setFlags() {
        boolean removeAds = sharedPreferences.getBoolean(FLAG_REMOVE_ADS, false);
        boolean unMistakes = sharedPreferences.getBoolean(FLAG_MISTAKES, false);
        boolean unHints = sharedPreferences.getBoolean(FLAG_HINTS, false);
        b.adCheckBox.setChecked(removeAds);
        if (removeAds) b.adCheckBox.setEnabled(false);
        b.mistakeCheckBox.setChecked(unMistakes);
        b.hintCheckBox.setChecked(unHints);
    }
    private void onUnlimitedMistakesSelected() {
        editor.putBoolean(FLAG_MISTAKES, b.mistakeCheckBox.isChecked());
        isStateChanged = true;
        editor.apply();
    }

    private void onUnlimitedHintsSelected() {
        editor.putBoolean(FLAG_HINTS, b.hintCheckBox.isChecked());
        isStateChanged = true;
        editor.apply();
    }

    private void changeOccurred(boolean changed) {
        if (changed) {
            isStateChanged = false;
            getActivity().recreate();
        }
    }

    private void applyTheme(int mode, int theme) {
        AppCompatDelegate.setDefaultNightMode(mode);
        getActivity().setTheme(theme);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CURRENT_THEME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CURRENT_THEME_INT, mode);
        editor.putInt(CURRENT_THEME_ID, theme);
        editor.apply();
        getActivity().recreate();
    }

}

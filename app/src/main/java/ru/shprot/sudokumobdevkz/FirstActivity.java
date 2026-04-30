package ru.shprot.sudokumobdevkz;

import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME_ID;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.CURRENT_THEME_INT;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.UPDATE_PREFS;
import static ru.shprot.sudokumobdevkz.model.game.utils.Library.UPDATE_SKIPPED_VERSION;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.WindowCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.shprot.sudokumobdevkz.databinding.ActivityFirstBinding;
import ru.shprot.sudokumobdevkz.model.CrashReporter;
import ru.shprot.sudokumobdevkz.model.game.utils.Library;
import ru.shprot.sudokumobdevkz.model.update.ApkDownloader;
import ru.shprot.sudokumobdevkz.model.update.UpdateChecker;
import ru.shprot.sudokumobdevkz.model.update.UpdateInfo;
import soup.neumorphism.NeumorphButton;

public class FirstActivity extends AppCompatActivity {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable navigateRunnable = () -> {
        startActivity(new Intent(FirstActivity.this, MainActivity.class));
        finish();
    };
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean isDownloading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateTheme();
        super.onCreate(savedInstanceState);
        CrashReporter.init(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ActivityFirstBinding binding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_logo);
        binding.logoText.startAnimation(animation);

        checkForUpdate();
    }

    private void checkForUpdate() {
        String currentVersion = getVersionName();

        UpdateChecker.check(currentVersion, executor, handler, new UpdateChecker.Callback() {
            @Override
            public void onUpdateAvailable(UpdateInfo info) {
                SharedPreferences prefs = getSharedPreferences(UPDATE_PREFS, MODE_PRIVATE);
                String skipped = prefs.getString(UPDATE_SKIPPED_VERSION, "");
                if (skipped.equals(info.getTagName())) {
                    navigateToMain();
                    return;
                }
                showUpdateSheet(info);
            }

            @Override
            public void onNoUpdate() {
                navigateToMain();
            }

            @Override
            public void onError() {
                navigateToMain();
            }
        });
    }

    private void showUpdateSheet(UpdateInfo info) {
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_update, null);

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(sheetView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);

        MaterialTextView titleView = sheetView.findViewById(R.id.updateTitle);
        MaterialTextView notesView = sheetView.findViewById(R.id.updateReleaseNotes);
        ProgressBar progressBar = sheetView.findViewById(R.id.updateProgressBar);
        MaterialTextView progressText = sheetView.findViewById(R.id.updateProgressText);
        NeumorphButton updateButton = sheetView.findViewById(R.id.buttonUpdate);
        View laterButton = sheetView.findViewById(R.id.buttonLater);

        String title = getString(R.string.update_available_title) + " " + info.getTagName();
        titleView.setText(title);

        String notes = info.getReleaseNotes();
        if (notes != null && !notes.isEmpty()) {
            notesView.setText(notes);
            notesView.setVisibility(View.VISIBLE);
        } else {
            notesView.setVisibility(View.GONE);
        }

        updateButton.setOnClickListener(v -> {
            isDownloading = true;
            dialog.setCancelable(false);
            updateButton.setEnabled(false);
            laterButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);

            ApkDownloader.download(this, info.getDownloadUrl(), executor, handler,
                    new ApkDownloader.Callback() {
                        @Override
                        public void onProgress(int percent) {
                            progressBar.setProgress(percent);
                            progressText.setText(getString(R.string.update_downloading, percent));
                        }

                        @Override
                        public void onSuccess(File apkFile) {
                            isDownloading = false;
                            dialog.dismiss();
                            ApkDownloader.installApk(FirstActivity.this, apkFile);
                            finishAffinity();
                        }

                        @Override
                        public void onError() {
                            isDownloading = false;
                            dialog.setCancelable(true);
                            updateButton.setEnabled(true);
                            laterButton.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            progressText.setVisibility(View.GONE);
                            Toast.makeText(FirstActivity.this,
                                    R.string.update_error, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        laterButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(UPDATE_PREFS, MODE_PRIVATE);
            prefs.edit().putString(UPDATE_SKIPPED_VERSION, info.getTagName()).apply();
            dialog.dismiss();
            navigateToMain();
        });

        dialog.setOnCancelListener(d -> {
            if (!isDownloading) {
                navigateToMain();
            }
        });

        dialog.show();
    }

    private void navigateToMain() {
        handler.post(navigateRunnable);
    }

    private String getVersionName() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0.0";
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

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(navigateRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}

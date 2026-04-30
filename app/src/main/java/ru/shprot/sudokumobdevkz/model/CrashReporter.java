package ru.shprot.sudokumobdevkz.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.shprot.sudokumobdevkz.BuildConfig;

public class CrashReporter {

    private static final String TAG = "CrashReporter";
    private static final String PREFS_NAME = "crashReporter";
    private static final String KEY_PENDING_CRASH = "pendingCrash";
    private static final String FIREBASE_DB_URL = BuildConfig.FIREBASE_DB_URL;

    public static void init(Context context) {
        Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            saveCrash(context, throwable);
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable);
            }
        });

        sendPendingCrash(context);
    }

    private static void saveCrash(Context context, Throwable throwable) {
        try {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));

            String versionName = "unknown";
            int versionCode = 0;
            try {
                PackageInfo pInfo = context.getPackageManager()
                        .getPackageInfo(context.getPackageName(), 0);
                versionName = pInfo.versionName;
                versionCode = pInfo.versionCode;
            } catch (PackageManager.NameNotFoundException ignored) {}

            String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                    .format(new Date());

            JSONObject crash = new JSONObject();
            crash.put("timestamp", timestamp);
            crash.put("versionName", versionName);
            crash.put("versionCode", versionCode);
            crash.put("device", Build.MANUFACTURER + " " + Build.MODEL);
            crash.put("android", Build.VERSION.SDK_INT);
            crash.put("stacktrace", sw.toString());

            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString(KEY_PENDING_CRASH, crash.toString()).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to save crash", e);
        }
    }

    private static void sendPendingCrash(Context context) {
        if (FIREBASE_DB_URL.isEmpty()) return;

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String pendingCrash = prefs.getString(KEY_PENDING_CRASH, null);
        if (pendingCrash == null) return;

        prefs.edit().remove(KEY_PENDING_CRASH).apply();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                String deviceId = FirebaseSync.getDeviceId(context);
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                        .format(new Date());
                String url = FIREBASE_DB_URL + "/crashes/" + deviceId + "/" + timestamp + ".json";

                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                try (OutputStream os = connection.getOutputStream()) {
                    os.write(pendingCrash.getBytes(StandardCharsets.UTF_8));
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    Log.i(TAG, "Crash report sent");
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to send crash report", e);
            } finally {
                if (connection != null) connection.disconnect();
                executor.shutdown();
            }
        });
    }
}

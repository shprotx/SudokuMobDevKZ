package ru.shprot.sudokumobdevkz.model;

import static ru.shprot.sudokumobdevkz.model.game.utils.Library.FIREBASE_DB_URL;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

import ru.shprot.sudokumobdevkz.model.game.Statistic;

public class FirebaseSync {

    private static final String TAG = "FirebaseSync";

    public interface PercentileCallback {
        void onResult(int percentile, int totalPlayers);
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static void uploadStatistic(Context context, Statistic statistic) {
        HttpURLConnection connection = null;
        try {
            String deviceId = getDeviceId(context);
            String url = FIREBASE_DB_URL + "/stats/" + deviceId + "/" + statistic.getDifficulty() + ".json";

            String json = "{\"averageTime\":" + statistic.getAverageTime()
                    + ",\"bestTime\":" + statistic.getBestTime()
                    + ",\"gamesWon\":" + statistic.getGamesWon()
                    + ",\"gamesStarted\":" + statistic.getGamesStarted() + "}";

            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                Log.e(TAG, "Upload failed, response code: " + responseCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Upload failed", e);
        } finally {
            if (connection != null) connection.disconnect();
        }
    }

    public static void fetchPercentile(Context context, int difficulty, int userAverageTime,
                                       PercentileCallback callback,
                                       ExecutorService executor, Handler mainHandler) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                String deviceId = getDeviceId(context);
                String url = FIREBASE_DB_URL + "/stats.json";

                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    mainHandler.post(() -> callback.onResult(-1, 0));
                    return;
                }

                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                String responseStr = response.toString();
                if (responseStr.equals("null") || responseStr.isEmpty()) {
                    mainHandler.post(() -> callback.onResult(-1, 0));
                    return;
                }

                JSONObject root = new JSONObject(responseStr);
                String diffKey = String.valueOf(difficulty);
                int totalPlayers = 0;
                int slowerCount = 0;

                Iterator<String> userIds = root.keys();
                while (userIds.hasNext()) {
                    String uid = userIds.next();
                    JSONObject userData = root.getJSONObject(uid);
                    if (userData.has(diffKey)) {
                        JSONObject diffData = userData.getJSONObject(diffKey);
                        int avgTime = diffData.optInt("averageTime", 0);
                        if (avgTime <= 0) continue;
                        if (uid.equals(deviceId)) continue;
                        totalPlayers++;
                        if (avgTime > userAverageTime) slowerCount++;
                    }
                }

                if (totalPlayers < 10) {
                    final int tp = totalPlayers;
                    mainHandler.post(() -> callback.onResult(-1, tp));
                } else {
                    int percentile = (slowerCount * 100) / totalPlayers;
                    final int tp = totalPlayers;
                    mainHandler.post(() -> callback.onResult(percentile, tp));
                }

            } catch (Exception e) {
                Log.e(TAG, "Fetch percentile failed", e);
                mainHandler.post(() -> callback.onResult(-1, 0));
            } finally {
                if (connection != null) connection.disconnect();
            }
        });
    }
}

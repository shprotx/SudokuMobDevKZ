package ru.shprot.sudokumobdevkz.model.update;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

public class UpdateChecker {

    private static final String TAG = "UpdateChecker";
    private static final String API_URL =
            "https://api.github.com/repos/shprotx/SudokuMobDevKZ/releases/latest";

    public interface Callback {
        void onUpdateAvailable(UpdateInfo info);
        void onNoUpdate();
        void onError();
    }

    public static void check(String currentVersion, ExecutorService executor,
                             Handler mainHandler, Callback callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) new URL(API_URL).openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.github+json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    mainHandler.post(callback::onError);
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

                JSONObject json = new JSONObject(response.toString());
                String tagName = json.optString("tag_name", "");
                String body = json.optString("body", "");
                String downloadUrl = "";

                JSONArray assets = json.optJSONArray("assets");
                if (assets != null && assets.length() > 0) {
                    downloadUrl = assets.getJSONObject(0)
                            .optString("browser_download_url", "");
                }

                if (tagName.isEmpty() || downloadUrl.isEmpty()) {
                    mainHandler.post(callback::onError);
                    return;
                }

                String remoteVersion = tagName.startsWith("v") ? tagName.substring(1) : tagName;

                if (isNewer(remoteVersion, currentVersion)) {
                    UpdateInfo info = new UpdateInfo(tagName, body, downloadUrl);
                    mainHandler.post(() -> callback.onUpdateAvailable(info));
                } else {
                    mainHandler.post(callback::onNoUpdate);
                }

            } catch (Exception e) {
                Log.e(TAG, "Update check failed", e);
                mainHandler.post(callback::onError);
            } finally {
                if (connection != null) connection.disconnect();
            }
        });
    }

    private static boolean isNewer(String remote, String current) {
        String[] remoteParts = remote.split("\\.");
        String[] currentParts = current.split("\\.");
        int length = Math.max(remoteParts.length, currentParts.length);
        for (int i = 0; i < length; i++) {
            int r = i < remoteParts.length ? parseIntSafe(remoteParts[i]) : 0;
            int c = i < currentParts.length ? parseIntSafe(currentParts[i]) : 0;
            if (r > c) return true;
            if (r < c) return false;
        }
        return false;
    }

    private static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

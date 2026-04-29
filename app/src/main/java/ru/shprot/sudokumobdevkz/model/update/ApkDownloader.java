package ru.shprot.sudokumobdevkz.model.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;

public class ApkDownloader {

    private static final String TAG = "ApkDownloader";
    private static final int BUFFER_SIZE = 8192;

    public interface Callback {
        void onProgress(int percent);
        void onSuccess(File apkFile);
        void onError();
    }

    public static void download(Context context, String url, ExecutorService executor,
                                Handler mainHandler, Callback callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                File cacheDir = context.getExternalCacheDir();
                if (cacheDir == null) {
                    mainHandler.post(callback::onError);
                    return;
                }
                File apkFile = new File(cacheDir, "update.apk");

                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setInstanceFollowRedirects(true);
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    mainHandler.post(callback::onError);
                    return;
                }

                int contentLength = connection.getContentLength();
                long totalRead = 0;
                int lastReportedPercent = -1;

                try (InputStream is = connection.getInputStream();
                     FileOutputStream fos = new FileOutputStream(apkFile)) {

                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        totalRead += bytesRead;
                        if (contentLength > 0) {
                            int percent = (int) (totalRead * 100 / contentLength);
                            if (percent != lastReportedPercent) {
                                lastReportedPercent = percent;
                                mainHandler.post(() -> callback.onProgress(percent));
                            }
                        }
                    }
                }

                mainHandler.post(() -> callback.onSuccess(apkFile));

            } catch (Exception e) {
                Log.e(TAG, "APK download failed", e);
                mainHandler.post(callback::onError);
            } finally {
                if (connection != null) connection.disconnect();
            }
        });
    }

    public static void installApk(Context context, File apkFile) {
        Uri apkUri = FileProvider.getUriForFile(context,
                context.getPackageName() + ".fileprovider", apkFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

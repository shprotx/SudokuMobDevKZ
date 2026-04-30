package ru.shprot.sudokumobdevkz.model.update;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
        try {
            PackageInstaller installer = context.getPackageManager().getPackageInstaller();
            PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                    PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            params.setSize(apkFile.length());

            int sessionId = installer.createSession(params);
            PackageInstaller.Session session = installer.openSession(sessionId);

            try (FileInputStream fis = new FileInputStream(apkFile);
                 OutputStream out = session.openWrite("update.apk", 0, apkFile.length())) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                session.fsync(out);
            }

            Intent intent = new Intent(context, InstallReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, sessionId,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

            session.commit(pendingIntent.getIntentSender());

        } catch (Exception e) {
            Log.e(TAG, "PackageInstaller failed", e);
        }
    }
}

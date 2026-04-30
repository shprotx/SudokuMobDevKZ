package ru.shprot.sudokumobdevkz.model.update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.util.Log;

public class InstallReceiver extends BroadcastReceiver {

    private static final String TAG = "InstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE);

        switch (status) {
            case PackageInstaller.STATUS_PENDING_USER_ACTION:
                Intent confirmIntent = intent.getParcelableExtra(Intent.EXTRA_INTENT);
                if (confirmIntent != null) {
                    confirmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(confirmIntent);
                }
                break;
            case PackageInstaller.STATUS_SUCCESS:
                Log.i(TAG, "Install succeeded");
                break;
            default:
                String message = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE);
                Log.e(TAG, "Install failed: status=" + status + " message=" + message);
                break;
        }
    }
}

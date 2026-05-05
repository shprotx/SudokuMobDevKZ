package ru.shprot.sudokumobdevkz.core.base.data

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.shprot.sudokumobdevkz.BuildConfig
import ru.shprot.sudokumobdevkz.core.base.data.remote.CrashDto
import ru.shprot.sudokumobdevkz.core.base.data.remote.FirebaseApi
import ru.shprot.sudokumobdevkz.core.base.data.util.DateTimeUtils
import ru.shprot.sudokumobdevkz.core.base.data.util.safeRunCatching
import java.io.PrintWriter
import java.io.StringWriter

object CrashReporter {

    private const val TAG = "CrashReporter"
    private const val PREFS_NAME = "crashReporter"
    private const val KEY_PENDING_CRASH = "pendingCrash"

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val json = Json { ignoreUnknownKeys = true }

    fun init(context: Context, firebaseApi: FirebaseApi) {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            saveCrash(context, throwable)
            defaultHandler?.uncaughtException(thread, throwable)
        }

        sendPendingCrash(context, firebaseApi)
    }

    private fun saveCrash(context: Context, throwable: Throwable) {
        runCatching {
            val sw = StringWriter()
            throwable.printStackTrace(PrintWriter(sw))

            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)

            val crash = CrashDto(
                timestamp = DateTimeUtils.formatCrashTimestamp(),
                versionName = pInfo.versionName ?: "unknown",
                versionCode = pInfo.longVersionCode.toInt(),
                device = "${Build.MANUFACTURER} ${Build.MODEL}",
                android = Build.VERSION.SDK_INT,
                stacktrace = sw.toString(),
            )

            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_PENDING_CRASH, json.encodeToString(crash))
                .apply()
        }.onFailure { Log.e(TAG, "Failed to save crash", it) }
    }

    @SuppressLint("HardwareIds")
    private fun sendPendingCrash(context: Context, firebaseApi: FirebaseApi) {
        if (BuildConfig.FIREBASE_DB_URL.isEmpty()) return

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val pending = prefs.getString(KEY_PENDING_CRASH, null) ?: return
        prefs.edit().remove(KEY_PENDING_CRASH).apply()

        scope.launch {
            safeRunCatching {
                val crash = json.decodeFromString<CrashDto>(pending)
                val deviceId = Settings.Secure.getString(
                    context.contentResolver, Settings.Secure.ANDROID_ID,
                )
                val timestamp = DateTimeUtils.formatCrashFileName()

                firebaseApi.uploadCrash(deviceId, timestamp, crash)
                Log.i(TAG, "Crash report sent")
            }.onFailure { Log.e(TAG, "Failed to send crash report", it) }
        }
    }
}

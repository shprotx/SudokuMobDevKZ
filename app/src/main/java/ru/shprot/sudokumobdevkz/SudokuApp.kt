package ru.shprot.sudokumobdevkz

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ru.shprot.sudokumobdevkz.core.base.data.CrashReporter
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudSyncOrchestrator
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationChannelInitializer
import ru.shprot.sudokumobdevkz.core.base.data.remote.FirebaseApi
import javax.inject.Inject

@HiltAndroidApp
class SudokuApp : Application(), Configuration.Provider {

    @Inject
    lateinit var firebaseApi: FirebaseApi

    @Inject
    lateinit var cloudSyncOrchestrator: CloudSyncOrchestrator

    @Inject
    lateinit var notificationChannelInitializer: NotificationChannelInitializer

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        CrashReporter.init(this, firebaseApi)
        cloudSyncOrchestrator.start()
        notificationChannelInitializer.createChannels()
    }
}

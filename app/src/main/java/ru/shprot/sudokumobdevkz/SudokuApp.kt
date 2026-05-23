package ru.shprot.sudokumobdevkz

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.shprot.sudokumobdevkz.core.base.data.CrashReporter
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudSyncOrchestrator
import ru.shprot.sudokumobdevkz.core.base.data.remote.FirebaseApi
import javax.inject.Inject

@HiltAndroidApp
class SudokuApp : Application() {

    @Inject
    lateinit var firebaseApi: FirebaseApi

    @Inject
    lateinit var cloudSyncOrchestrator: CloudSyncOrchestrator

    override fun onCreate() {
        super.onCreate()
        CrashReporter.init(this, firebaseApi)
        cloudSyncOrchestrator.start()
    }
}

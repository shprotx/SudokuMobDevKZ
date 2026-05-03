package ru.shprot.sudokumobdevkz

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.shprot.sudokumobdevkz.model.ComposeCrashReporter
import ru.shprot.sudokumobdevkz.model.remote.FirebaseApi
import javax.inject.Inject

@HiltAndroidApp
class SudokuApp : Application() {

    @Inject
    lateinit var firebaseApi: FirebaseApi

    override fun onCreate() {
        super.onCreate()
        ComposeCrashReporter.init(this, firebaseApi)
    }
}

package ru.shprot.sudokumobdevkz.core.base.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StableIdProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cloud: CloudGameServices,
) {

    fun current(): String {
        val signed = cloud.signInState.value
        return if (signed is SignInState.SignedIn) {
            pgsKey(signed.playerId)
        } else {
            deviceKey()
        }
    }

    fun pgsKey(playerId: String): String = "pgs_$playerId"

    fun deviceKey(): String = "dev_${androidId()}"

    @SuppressLint("HardwareIds")
    private fun androidId(): String =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}
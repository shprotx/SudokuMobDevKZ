package ru.shprot.sudokumobdevkz.core.base.data.util

import android.content.Context
import android.os.Build
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GmsAvailability @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    enum class GmsStatus {
        Available,
        Missing,
        Disabled,
        NeedsUpdate,
        Updating,
        Invalid,
        Unknown,
    }

    enum class InstallSource {
        PlayStore,
        Sideload,
        OtherStore,
        Unknown,
    }

    fun gmsStatus(): GmsStatus =
        mapConnectionResult(
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context),
        )

    fun installSource(): InstallSource =
        mapInstallerPackage(installerPackage())

    val hasGms: Boolean
        get() = gmsStatus() == GmsStatus.Available

    val isFromPlayStore: Boolean
        get() = installSource() == InstallSource.PlayStore

    private fun installerPackage(): String? = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.packageManager.getInstallSourceInfo(context.packageName).installingPackageName
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getInstallerPackageName(context.packageName)
        }
    }.getOrNull()

    internal companion object {

        const val PLAY_STORE_PACKAGE = "com.android.vending"
        const val SYSTEM_INSTALLER_PACKAGE = "com.google.android.packageinstaller"
        const val ANDROID_PACKAGE_INSTALLER = "com.android.packageinstaller"

        fun mapConnectionResult(code: Int): GmsStatus =
            when (code) {
                ConnectionResult.SUCCESS -> GmsStatus.Available
                ConnectionResult.SERVICE_MISSING -> GmsStatus.Missing
                ConnectionResult.SERVICE_DISABLED -> GmsStatus.Disabled
                ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED -> GmsStatus.NeedsUpdate
                ConnectionResult.SERVICE_UPDATING -> GmsStatus.Updating
                ConnectionResult.SERVICE_INVALID -> GmsStatus.Invalid
                else -> GmsStatus.Unknown
            }

        fun mapInstallerPackage(installer: String?): InstallSource =
            when (installer) {
                null -> InstallSource.Sideload
                PLAY_STORE_PACKAGE -> InstallSource.PlayStore
                SYSTEM_INSTALLER_PACKAGE, ANDROID_PACKAGE_INSTALLER -> InstallSource.Sideload
                else -> InstallSource.OtherStore
            }
    }
}

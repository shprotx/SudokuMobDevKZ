package ru.shprot.sudokumobdevkz.core.base.data.util

import com.google.android.gms.common.ConnectionResult
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.util.GmsAvailability.Companion.ANDROID_PACKAGE_INSTALLER
import ru.shprot.sudokumobdevkz.core.base.data.util.GmsAvailability.Companion.PLAY_STORE_PACKAGE
import ru.shprot.sudokumobdevkz.core.base.data.util.GmsAvailability.Companion.SYSTEM_INSTALLER_PACKAGE
import ru.shprot.sudokumobdevkz.core.base.data.util.GmsAvailability.Companion.mapConnectionResult
import ru.shprot.sudokumobdevkz.core.base.data.util.GmsAvailability.Companion.mapInstallerPackage

class GmsAvailabilityTest {

    @Test
    fun `SUCCESS maps to Available`() {
        assertEquals(
            GmsAvailability.GmsStatus.Available,
            mapConnectionResult(ConnectionResult.SUCCESS),
        )
    }

    @Test
    fun `SERVICE_MISSING maps to Missing`() {
        assertEquals(
            GmsAvailability.GmsStatus.Missing,
            mapConnectionResult(ConnectionResult.SERVICE_MISSING),
        )
    }

    @Test
    fun `SERVICE_DISABLED maps to Disabled`() {
        assertEquals(
            GmsAvailability.GmsStatus.Disabled,
            mapConnectionResult(ConnectionResult.SERVICE_DISABLED),
        )
    }

    @Test
    fun `SERVICE_VERSION_UPDATE_REQUIRED maps to NeedsUpdate`() {
        assertEquals(
            GmsAvailability.GmsStatus.NeedsUpdate,
            mapConnectionResult(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED),
        )
    }

    @Test
    fun `SERVICE_UPDATING maps to Updating`() {
        assertEquals(
            GmsAvailability.GmsStatus.Updating,
            mapConnectionResult(ConnectionResult.SERVICE_UPDATING),
        )
    }

    @Test
    fun `SERVICE_INVALID maps to Invalid`() {
        assertEquals(
            GmsAvailability.GmsStatus.Invalid,
            mapConnectionResult(ConnectionResult.SERVICE_INVALID),
        )
    }

    @Test
    fun `unknown code maps to Unknown`() {
        assertEquals(
            GmsAvailability.GmsStatus.Unknown,
            mapConnectionResult(Int.MAX_VALUE),
        )
    }

    @Test
    fun `null installer maps to Sideload`() {
        assertEquals(
            GmsAvailability.InstallSource.Sideload,
            mapInstallerPackage(null),
        )
    }

    @Test
    fun `com_android_vending maps to PlayStore`() {
        assertEquals(
            GmsAvailability.InstallSource.PlayStore,
            mapInstallerPackage(PLAY_STORE_PACKAGE),
        )
    }

    @Test
    fun `google packageinstaller maps to Sideload`() {
        assertEquals(
            GmsAvailability.InstallSource.Sideload,
            mapInstallerPackage(SYSTEM_INSTALLER_PACKAGE),
        )
    }

    @Test
    fun `android packageinstaller maps to Sideload`() {
        assertEquals(
            GmsAvailability.InstallSource.Sideload,
            mapInstallerPackage(ANDROID_PACKAGE_INSTALLER),
        )
    }

    @Test
    fun `arbitrary store maps to OtherStore`() {
        assertEquals(
            GmsAvailability.InstallSource.OtherStore,
            mapInstallerPackage("ru.vk.store"),
        )
    }
}

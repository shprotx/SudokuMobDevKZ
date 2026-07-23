package ru.shprot.sudokumobdevkz.feature.settings.presentation.contract

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInState
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState

data class SettingsUIState(
    val settings: AppSettings = AppSettings(),
    val showResetDialog: Boolean = false,
    val hasActiveStandardGame: Boolean = false,
    val showLockedSettingDialog: Boolean = false,
    val isCloudAvailable: Boolean = false,
    val signInState: SignInState = SignInState.NotAvailable,
    val isSigningIn: Boolean = false,
    val showSignOutHint: Boolean = false,
    val cloudImport: CloudImportState = CloudImportState.Idle,
    val customThemes: ImmutableList<CustomTheme> = persistentListOf(),
    val showDeleteThemeDialog: Boolean = false,
    val themeToDeleteId: String? = null,
) : UIState

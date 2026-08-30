package ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudGameServices
import ru.shprot.sudokumobdevkz.core.base.data.cloud.CloudProgressMerger
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.SignInResult
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationPermissionChecker
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationScheduler
import ru.shprot.sudokumobdevkz.core.base.data.notification.ReengagementScheduler
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings
import ru.shprot.sudokumobdevkz.core.base.data.repository.IThemeRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeMode
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.ImportFromCloudUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.SyncToCloudUseCase
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.cloud.ToggleShowNameOnLeaderboardUseCase
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.CloudImportState
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEffect
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEvent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val sudokuRepository: SudokuRepository,
    private val cloud: CloudGameServices,
    private val importFromCloud: ImportFromCloudUseCase,
    private val syncToCloud: SyncToCloudUseCase,
    private val toggleShowNameOnLeaderboard: ToggleShowNameOnLeaderboardUseCase,
    private val themeRepository: IThemeRepository,
    private val notificationScheduler: NotificationScheduler,
    private val notificationPermissionChecker: NotificationPermissionChecker,
    private val reengagementScheduler: ReengagementScheduler,
) : BaseViewModel<SettingsUIEvent, SettingsUIState, SettingsUIEffect>(
    SettingsUIState()
) {

    init {
        setState(currentState.copy(
            settings = settingsRepository.currentSettings,
            isCloudAvailable = cloud.isAvailable,
        ))
        viewModelScope.launch {
            settingsRepository.settings.collectLatest { settings ->
                updateState { copy(settings = settings) }
            }
        }
        viewModelScope.launch {
            val saved = sudokuRepository.loadSavedGame()
            updateState { copy(hasActiveStandardGame = saved != null && saved.isStandardMode) }
        }
        if (cloud.isAvailable) {
            viewModelScope.launch {
                cloud.signInState.collect { state ->
                    updateState { copy(signInState = state) }
                }
            }
        }
        viewModelScope.launch {
            themeRepository.observeAll().collectLatest { themes ->
                updateState { copy(customThemes = themes.toImmutableList()) }
            }
        }
    }

    override fun handleUIEvent(event: SettingsUIEvent) =
        when (event) {
            SettingsUIEvent.BackClicked ->
                setEffect(SettingsUIEffect.NavigateBack)

            SettingsUIEvent.NavigateToPrivacyPolicy ->
                setEffect(SettingsUIEffect.NavigateToPrivacyPolicy)

            SettingsUIEvent.NavigateToFeedback ->
                setEffect(SettingsUIEffect.NavigateToFeedback)

            SettingsUIEvent.ShowResetDialog ->
                updateState { copy(showResetDialog = true) }

            SettingsUIEvent.DismissResetDialog ->
                updateState { copy(showResetDialog = false) }

            SettingsUIEvent.ResetConfirmed ->
                handleResetConfirmed()

            SettingsUIEvent.DismissLockedDialog ->
                updateState { copy(showLockedSettingDialog = false) }

            SettingsUIEvent.ToggleCheckErrors ->
                handleSensitiveSetting { copy(checkErrors = !checkErrors) }

            SettingsUIEvent.ToggleUnlimitedErrors ->
                handleSensitiveSetting { copy(unlimitedErrors = !unlimitedErrors) }

            SettingsUIEvent.ToggleUnlimitedHints ->
                handleSensitiveSetting { copy(unlimitedHints = !unlimitedHints) }

            SettingsUIEvent.ToggleHighlightDuplicates ->
                settingsRepository.update { copy(highlightDuplicates = !highlightDuplicates) }

            SettingsUIEvent.ToggleAutoSave ->
                settingsRepository.update { copy(autoSave = !autoSave) }

            SettingsUIEvent.ToggleShowTimer ->
                settingsRepository.update { copy(showTimer = !showTimer) }

            SettingsUIEvent.ToggleShowErrors ->
                settingsRepository.update { copy(showErrors = !showErrors) }

            SettingsUIEvent.ToggleTrackStatistics ->
                settingsRepository.update { copy(trackStatistics = !trackStatistics) }

            SettingsUIEvent.ToggleSounds ->
                settingsRepository.update { copy(soundsEnabled = !soundsEnabled) }

            SettingsUIEvent.ToggleCompactNumberPad ->
                settingsRepository.update { copy(compactNumberPad = !compactNumberPad) }

            SettingsUIEvent.ShareAppClicked ->
                setEffect(SettingsUIEffect.ShareApp)

            SettingsUIEvent.RateAppClicked ->
                setEffect(SettingsUIEffect.OpenPlayStore)

            SettingsUIEvent.SignInClicked ->
                handleSignIn()

            SettingsUIEvent.SignOutClicked ->
                updateState { copy(showSignOutHint = true) }

            SettingsUIEvent.DismissSignOutHint ->
                updateState { copy(showSignOutHint = false) }

            SettingsUIEvent.OpenPlayGamesAppClicked -> {
                updateState { copy(showSignOutHint = false) }
                setEffect(SettingsUIEffect.OpenPlayGamesApp)
            }

            SettingsUIEvent.ImportFromCloudClicked ->
                handleImportFromCloud()

            SettingsUIEvent.ImportChoiceMerge ->
                handleImportChoice(ImportChoice.MERGE)

            SettingsUIEvent.ImportChoiceKeepLocal ->
                handleImportChoice(ImportChoice.KEEP_LOCAL)

            SettingsUIEvent.ImportChoiceUseCloud ->
                handleImportChoice(ImportChoice.USE_CLOUD)

            SettingsUIEvent.DismissImportDialog ->
                updateState { copy(cloudImport = CloudImportState.Idle) }

            SettingsUIEvent.ToggleShowNameOnLeaderboard ->
                handleToggleShowNameOnLeaderboard()

            SettingsUIEvent.NotificationsToggleClicked ->
                handleNotificationsToggleClicked()

            SettingsUIEvent.NavigateToThemeBuilder ->
                setEffect(SettingsUIEffect.NavigateToThemeBuilder)

            SettingsUIEvent.ConfirmDeleteTheme ->
                handleConfirmDeleteTheme()

            SettingsUIEvent.DismissDeleteThemeDialog ->
                updateState { copy(showDeleteThemeDialog = false, themeToDeleteId = null) }

            is SettingsUIEvent.SelectThemeMode ->
                settingsRepository.update { copy(themeModeId = event.mode.id) }

            is SettingsUIEvent.SelectHintMode ->
                settingsRepository.update { copy(hintMode = event.mode) }

            is SettingsUIEvent.NavigateToEditTheme ->
                setEffect(SettingsUIEffect.NavigateToEditTheme(event.themeId))

            is SettingsUIEvent.RequestDeleteTheme ->
                updateState { copy(showDeleteThemeDialog = true, themeToDeleteId = event.themeId) }

            is SettingsUIEvent.NotificationPermissionResult ->
                handleNotificationPermissionResult(event.granted)
        }

    private fun handleConfirmDeleteTheme() {
        val id = currentState.themeToDeleteId ?: return
        updateState { copy(showDeleteThemeDialog = false, themeToDeleteId = null) }
        viewModelScope.launch(exceptionHandler) {
            themeRepository.delete(id)
            if (currentState.settings.themeModeId == id) {
                settingsRepository.update { copy(themeModeId = ThemeMode.Light.id) }
                setEffect(SettingsUIEffect.ShowMessage(R.string.theme_deleted_fallback))
            }
        }
    }

    private fun handleSignIn() {
        if (currentState.isSigningIn) return
        updateState { copy(isSigningIn = true) }
        viewModelScope.launch(exceptionHandler) {
            val result = cloud.requestSignIn()
            updateState { copy(isSigningIn = false) }
            when (result) {
                SignInResult.Success -> handlePostSignIn()
                is SignInResult.Failure ->
                    setEffect(SettingsUIEffect.ShowMessage(R.string.cloud_sign_in_failed))
                SignInResult.Cancelled,
                SignInResult.NotAvailable -> Unit
            }
        }
    }

    private suspend fun handlePostSignIn() {
        val cloudProgress = importFromCloud.loadCloudSnapshot()
            ?.takeUnless(ImportFromCloudUseCase::isEmpty)
        val localProgress = importFromCloud.currentLocalProgress()
        val localEmpty = ImportFromCloudUseCase.isEmpty(localProgress)

        when {
            cloudProgress != null && localEmpty -> {
                importFromCloud.applyProgress(cloudProgress)
                setEffect(SettingsUIEffect.ShowMessage(R.string.cloud_auto_imported))
            }

            cloudProgress != null ->
                updateState {
                    copy(cloudImport = CloudImportState.Choosing(localProgress, cloudProgress))
                }

            !localEmpty -> {
                syncToCloud.syncNow()
                setEffect(SettingsUIEffect.ShowMessage(R.string.cloud_auto_synced))
            }
        }
    }

    private fun handleResetConfirmed() {
        viewModelScope.launch(exceptionHandler) {
            for (difficulty in Difficulty.entries) {
                sudokuRepository.resetStatistic(difficulty)
            }
        }
        updateState { copy(showResetDialog = false) }
    }

    private fun handleSensitiveSetting(transform: AppSettings.() -> AppSettings) {
        if (currentState.hasActiveStandardGame) {
            updateState { copy(showLockedSettingDialog = true) }
        } else {
            settingsRepository.update(transform)
        }
    }

    private fun handleImportFromCloud() {
        updateState { copy(cloudImport = CloudImportState.Loading) }
        viewModelScope.launch(exceptionHandler) {
            val cloudProgress = importFromCloud.loadCloudSnapshot()
            if (cloudProgress == null) {
                updateState { copy(cloudImport = CloudImportState.Idle) }
                setEffect(SettingsUIEffect.ShowMessage(R.string.cloud_import_empty))
                return@launch
            }
            val localProgress = importFromCloud.currentLocalProgress()
            updateState {
                copy(cloudImport = CloudImportState.Choosing(localProgress, cloudProgress))
            }
        }
    }

    private fun handleImportChoice(choice: ImportChoice) {
        val state = currentState.cloudImport as? CloudImportState.Choosing ?: return
        updateState { copy(cloudImport = CloudImportState.Applying) }
        viewModelScope.launch(exceptionHandler) {
            val progress = when (choice) {
                ImportChoice.MERGE -> CloudProgressMerger.merge(state.local, state.cloud)
                ImportChoice.KEEP_LOCAL -> state.local
                ImportChoice.USE_CLOUD -> state.cloud
            }
            importFromCloud.applyProgress(progress)
            syncToCloud.trigger()
            updateState { copy(cloudImport = CloudImportState.Idle) }
            setEffect(SettingsUIEffect.ShowMessage(R.string.cloud_import_applied))
        }
    }

    private fun handleToggleShowNameOnLeaderboard() {
        viewModelScope.launch { toggleShowNameOnLeaderboard() }
    }

    private fun handleNotificationsToggleClicked() {
        if (currentState.settings.notificationsEnabled) {
            disableNotifications()
        } else if (notificationPermissionChecker.isGranted()) {
            enableNotifications()
        } else {
            setEffect(SettingsUIEffect.RequestNotificationPermission)
        }
    }

    private fun handleNotificationPermissionResult(granted: Boolean) {
        if (granted) {
            enableNotifications()
        } else {
            setEffect(SettingsUIEffect.ShowMessage(R.string.notifications_permission_denied))
        }
    }

    private fun enableNotifications() {
        settingsRepository.setNotificationsEnabled(true)
        viewModelScope.launch(exceptionHandler) { reengagementScheduler.rescheduleAll(notificationsEnabled = true) }
    }

    private fun disableNotifications() {
        settingsRepository.setNotificationsEnabled(false)
        notificationScheduler.cancelAll()
    }

    private enum class ImportChoice { MERGE, KEEP_LOCAL, USE_CLOUD }
}
package ru.shprot.sudokumobdevkz.model.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

data class AppSettings(
    val checkErrors: Boolean = true,
    val highlightDuplicates: Boolean = true,
    val autoSave: Boolean = true,
    val showTimer: Boolean = true,
    val showErrors: Boolean = true,
    val unlimitedErrors: Boolean = false,
    val unlimitedHints: Boolean = false,
    val trackStatistics: Boolean = true,
    val isDarkTheme: Boolean = false,
    val soundsEnabled: Boolean = true,
) {
    val hasCheats: Boolean get() = unlimitedErrors || unlimitedHints
    val effectiveTrackStatistics: Boolean get() = trackStatistics && !hasCheats
}

private val Context.settingsDataStore by preferencesDataStore(name = "sudoku_settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _settings: StateFlow<AppSettings> = context.settingsDataStore.data
        .map { prefs -> prefs.toAppSettings() }
        .stateIn(scope, SharingStarted.Eagerly, AppSettings())

    val settings: Flow<AppSettings> = _settings

    val currentSettings: AppSettings get() = _settings.value

    fun update(transform: AppSettings.() -> AppSettings) {
        val newSettings = currentSettings.transform()
        scope.launch {
            context.settingsDataStore.edit { prefs ->
                prefs[Keys.CHECK_ERRORS] = newSettings.checkErrors
                prefs[Keys.HIGHLIGHT_DUPLICATES] = newSettings.highlightDuplicates
                prefs[Keys.AUTO_SAVE] = newSettings.autoSave
                prefs[Keys.SHOW_TIMER] = newSettings.showTimer
                prefs[Keys.SHOW_ERRORS] = newSettings.showErrors
                prefs[Keys.UNLIMITED_ERRORS] = newSettings.unlimitedErrors
                prefs[Keys.UNLIMITED_HINTS] = newSettings.unlimitedHints
                prefs[Keys.TRACK_STATISTICS] = newSettings.trackStatistics
                prefs[Keys.DARK_THEME] = newSettings.isDarkTheme
                prefs[Keys.SOUNDS] = newSettings.soundsEnabled
            }
        }
    }

    private fun androidx.datastore.preferences.core.Preferences.toAppSettings() = AppSettings(
        checkErrors = this[Keys.CHECK_ERRORS] ?: true,
        highlightDuplicates = this[Keys.HIGHLIGHT_DUPLICATES] ?: true,
        autoSave = this[Keys.AUTO_SAVE] ?: true,
        showTimer = this[Keys.SHOW_TIMER] ?: true,
        showErrors = this[Keys.SHOW_ERRORS] ?: true,
        unlimitedErrors = this[Keys.UNLIMITED_ERRORS] ?: false,
        unlimitedHints = this[Keys.UNLIMITED_HINTS] ?: false,
        trackStatistics = this[Keys.TRACK_STATISTICS] ?: true,
        isDarkTheme = this[Keys.DARK_THEME] ?: false,
        soundsEnabled = this[Keys.SOUNDS] ?: true,
    )

    private object Keys {
        val CHECK_ERRORS = booleanPreferencesKey("check_errors")
        val HIGHLIGHT_DUPLICATES = booleanPreferencesKey("highlight_duplicates")
        val AUTO_SAVE = booleanPreferencesKey("auto_save")
        val SHOW_TIMER = booleanPreferencesKey("show_timer")
        val SHOW_ERRORS = booleanPreferencesKey("show_errors")
        val UNLIMITED_ERRORS = booleanPreferencesKey("unlimited_errors")
        val UNLIMITED_HINTS = booleanPreferencesKey("unlimited_hints")
        val TRACK_STATISTICS = booleanPreferencesKey("track_statistics")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val SOUNDS = booleanPreferencesKey("sounds")
    }
}

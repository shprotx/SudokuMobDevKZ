package ru.shprot.sudokumobdevkz.model.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val prefs = context.getSharedPreferences("sudoku_settings", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadFromPrefs())
    val settings: Flow<AppSettings> = _settings.asStateFlow()

    val currentSettings: AppSettings get() = _settings.value

    fun update(transform: AppSettings.() -> AppSettings) {
        _settings.update { it.transform() }
        saveToPrefs(_settings.value)
    }

    private fun loadFromPrefs(): AppSettings = AppSettings(
        checkErrors = prefs.getBoolean(KEY_CHECK_ERRORS, true),
        highlightDuplicates = prefs.getBoolean(KEY_HIGHLIGHT_DUPLICATES, true),
        autoSave = prefs.getBoolean(KEY_AUTO_SAVE, true),
        showTimer = prefs.getBoolean(KEY_SHOW_TIMER, true),
        showErrors = prefs.getBoolean(KEY_SHOW_ERRORS, true),
        unlimitedErrors = prefs.getBoolean(KEY_UNLIMITED_ERRORS, false),
        unlimitedHints = prefs.getBoolean(KEY_UNLIMITED_HINTS, false),
        trackStatistics = prefs.getBoolean(KEY_TRACK_STATISTICS, true),
        isDarkTheme = prefs.getBoolean(KEY_DARK_THEME, false),
        soundsEnabled = prefs.getBoolean(KEY_SOUNDS, true),
    )

    private fun saveToPrefs(s: AppSettings) {
        prefs.edit()
            .putBoolean(KEY_CHECK_ERRORS, s.checkErrors)
            .putBoolean(KEY_HIGHLIGHT_DUPLICATES, s.highlightDuplicates)
            .putBoolean(KEY_AUTO_SAVE, s.autoSave)
            .putBoolean(KEY_SHOW_TIMER, s.showTimer)
            .putBoolean(KEY_SHOW_ERRORS, s.showErrors)
            .putBoolean(KEY_UNLIMITED_ERRORS, s.unlimitedErrors)
            .putBoolean(KEY_UNLIMITED_HINTS, s.unlimitedHints)
            .putBoolean(KEY_TRACK_STATISTICS, s.trackStatistics)
            .putBoolean(KEY_DARK_THEME, s.isDarkTheme)
            .putBoolean(KEY_SOUNDS, s.soundsEnabled)
            .apply()
    }

    private companion object {
        const val KEY_CHECK_ERRORS = "check_errors"
        const val KEY_HIGHLIGHT_DUPLICATES = "highlight_duplicates"
        const val KEY_AUTO_SAVE = "auto_save"
        const val KEY_SHOW_TIMER = "show_timer"
        const val KEY_SHOW_ERRORS = "show_errors"
        const val KEY_UNLIMITED_ERRORS = "unlimited_errors"
        const val KEY_UNLIMITED_HINTS = "unlimited_hints"
        const val KEY_TRACK_STATISTICS = "track_statistics"
        const val KEY_DARK_THEME = "dark_theme"
        const val KEY_SOUNDS = "sounds"
    }
}

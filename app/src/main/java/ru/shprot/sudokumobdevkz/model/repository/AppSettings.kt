package ru.shprot.sudokumobdevkz.model.repository

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

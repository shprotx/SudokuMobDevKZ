package ru.shprot.sudokumobdevkz.core.base.domain.model

data class AppSettings(
    val checkErrors: Boolean = true,
    val highlightDuplicates: Boolean = true,
    val autoSave: Boolean = true,
    val showTimer: Boolean = true,
    val showErrors: Boolean = true,
    val unlimitedErrors: Boolean = false,
    val unlimitedHints: Boolean = false,
    val trackStatistics: Boolean = true,
    val themeModeId: String = ThemeMode.System.id,
    val soundsEnabled: Boolean = true,
    val compactNumberPad: Boolean = false,
    val selectedDifficultyOrdinal: Int = 0,
    val hintMode: HintMode = HintMode.SINGLE_SHOT,
    val showNameOnLeaderboard: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val gameBlockOrder: List<GameBlockId> = GameBlockId.DEFAULT_ORDER,
    val actionButtonOrder: List<ActionButtonId> = ActionButtonId.DEFAULT_ORDER,
    val statusItemOrder: List<StatusItemId> = StatusItemId.DEFAULT_ORDER,
) {
    val isStandardMode: Boolean get() = checkErrors && !unlimitedErrors && !unlimitedHints
}
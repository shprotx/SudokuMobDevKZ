package ru.shprot.sudokumobdevkz.feature.game.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.domain.model.GameBlockId
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeMode
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState
import ru.shprot.sudokumobdevkz.feature.game.domain.model.CellData

data class GameUIState(
    val cells: List<List<CellData>> = List(9) { List(9) { CellData() } },
    val solution: List<List<Int>> = List(9) { List(9) { 0 } },
    val selectedRow: Int = -1,
    val selectedCol: Int = -1,
    val difficulty: Difficulty = Difficulty.EASY,
    val errors: Int = 0,
    val maxErrors: Int = 3,
    val timer: String = "00:00",
    val timeSeconds: Int = 0,
    val isNotesEnabled: Boolean = false,
    val hintsRemaining: Int = 3,
    val isGenerating: Boolean = true,
    val isPaused: Boolean = false,
    val isGameOver: Boolean = false,
    val isWin: Boolean = false,
    val availableNumbers: Set<Int> = (1..9).toSet(),
    val highlightedNumber: Int = 0,
    val showPauseDialog: Boolean = false,
    val showNewGameDialog: Boolean = false,
    val isStandardMode: Boolean = true,
    val isDailyChallenge: Boolean = false,
    val compactNumberPadPreference: Boolean = false,
    val isHintModeActive: Boolean = false,
    val themePopupExpanded: Boolean = false,
    val selectedThemeId: String = ThemeMode.System.id,
    val draftPopupVisible: Boolean = false,
    val draftPopupRow: Int = -1,
    val draftPopupCol: Int = -1,
    val blockOrder: List<GameBlockId> = GameBlockId.DEFAULT_ORDER,
    val isLayoutEditMode: Boolean = false,
) : UIState
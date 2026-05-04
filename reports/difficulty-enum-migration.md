# Difficulty Enum Migration Report

## What was done

Migrated the entire Sudoku project from raw `Int` difficulty values to a type-safe `Difficulty` enum.

## Created files (1)

- `core/base/domain/model/Difficulty.kt` — enum with `EASY`, `MEDIUM`, `HARD`; properties: `firebaseKey` (1/2/3), `visibleCells` (40/30/27), `emoji`; companion: `fromFirebaseKey()`, `fromOrdinal()`

## Deleted files (1)

- `core/uicommon/DifficultyEmoji.kt` — replaced by `Difficulty.emoji`

## Modified files (26)

### Generator
- `SudokuGenerator.kt` — `generate(Difficulty)` instead of `generate(Int)`. Removed `VISIBLE_EASY/MEDIUM/EXPERT` constants and `visibleForDifficulty()`. Uses `difficulty.visibleCells` and `difficulty != Difficulty.HARD` checks.

### Repository
- `SudokuRepository.kt` — all public methods take `Difficulty` instead of `Int`. Internally converts via `.firebaseKey` for DB/Firebase. `syncStatisticsFromFirebase()` uses `Difficulty.fromFirebaseKey()`.

### Game feature
- `GameRoutes.kt` — `difficulty: Int` renamed to `difficultyOrdinal: Int`
- `GameUIState.kt` — `difficulty: Difficulty` instead of `Int`
- `GameUIEffect.kt` — `NavigateToNewGame(difficultyOrdinal: Int)`
- `GameUIEvent.kt` — `StartNewGame(difficultyOrdinal: Int)`
- `GameViewModel.kt` — stores `Difficulty` enum; converts at boundaries (route ordinal, saved game firebaseKey)
- `GameScreen.kt` — passes `state.difficulty.ordinal` to routes/dialogs
- `GameScreenContent.kt` — uses `when(uiState.difficulty)` for difficulty label
- `NewGameDialog.kt` — uses `Difficulty.entries` loop instead of hardcoded DifficultyEmoji

### Game Over feature
- `GameOverRoutes.kt` — `difficulty` renamed to `difficultyOrdinal`
- `GameOverUIState.kt` — `difficulty: Difficulty`
- `GameOverUIEffect.kt` — `NavigateToNewGame(difficultyOrdinal: Int)`
- `GameOverViewModel.kt` — converts ordinal from route to `Difficulty`
- `GameOverScreen.kt` — uses `difficultyOrdinal` in navigation
- `GameOverScreenContent.kt` — uses `uiState.difficulty.emoji`

### Menu feature
- `MenuUIEvent.kt` — `NewGameClicked(difficultyOrdinal)`, `DifficultySelected(difficultyOrdinal)`
- `MenuUIEffect.kt` — `NavigateToGame(difficultyOrdinal)`
- `MenuViewModel.kt` — passes ordinals through
- `MenuScreen.kt` — uses `difficultyOrdinal` in navigation
- `MenuScreenContent.kt` — no changes needed (already uses ordinal Int)
- `DifficultySelector.kt` — uses `Difficulty.entries` loop

### Statistic feature
- `StatisticViewModel.kt` — uses `Difficulty.fromOrdinal()` for tab selection, passes `Difficulty` to repository
- `StatisticUIEvent.kt` — `ResetRequested(tabIndex: Int)` (renamed from `difficulty`)
- `StatisticScreen.kt` — passes `state.selectedTab` as tabIndex

### Settings feature
- `SettingsViewModel.kt` — iterates `Difficulty.entries` for reset

## Key design decisions

- **UI** uses ordinal (0, 1, 2) for tab indices and selector state
- **DB/Firebase** uses `firebaseKey` (1, 2, 3) — no DB migration needed
- **Code** uses `Difficulty` enum everywhere else
- **Conversion at boundaries only**: route args, DB entities, `GameSaveData`

## Verification

- `./gradlew assembleDebug` — BUILD SUCCESSFUL
- `./gradlew testDebugUnitTest` — BUILD SUCCESSFUL
- No remaining references to `DifficultyEmoji`

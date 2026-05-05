# Bug Fix: 9 Issues Batch Fix
Профиль: **Bug Fix**

## Bug Analysis

**Symptom**: Multiple UI and logic issues across GameOver screen, game grid, statistics, navigation bar, and splash screen.

## Investigation & Fixes Applied

### Issue 1: GameOver difficulty text overflow
**Root cause**: Long Russian text ("Легкая"/"Средняя"/"Экспертная") overflows the StatCard on the GameOver screen.
**Fix**: `GameOverScreenContent.kt` -- replaced string resource labels with `DifficultyEmoji.EASY`/`MEDIUM`/`HARD` (emoji icons) which fit in the card.

### Issue 2: Correctly placed values could be overwritten
**Root cause**: `onNumberClicked` and `onErase` in `GameViewModel.kt` only checked `cell.isGiven` but not whether the cell already has a correct (non-error) value.
**Fix**: `GameViewModel.kt` -- added `if (cell.value != 0 && !cell.isError) return` guard in both `onNumberClicked` and `onErase`.

### Issue 3: Cell highlights too faint
**Root cause**: Alpha values for `cellHighlight` and `cellSelected` were too low in both light and dark themes.
**Fix**: `Color.kt` -- Light: highlight 0.08f->0.15f, selected 0.25f->0.35f. Dark: highlight 0.12f->0.18f, selected 0.30f->0.40f.

### Issue 4: Draft/notes digits too thin and small
**Root cause**: Notes used `FontWeight.Normal` and small font sizes.
**Fix**: `SudokuGrid.kt` -- changed normal notes to `FontWeight.Medium`, increased `draftFontSizeSp` (0.18f->0.20f) and `draftHighlightFontSizeSp` (0.22f->0.24f).

### Issue 5: Matching notes highlighted in green instead of black/white
**Root cause**: Highlighted notes used `editableColor` (green) instead of `fixedColor` (black/white).
**Fix**: `SudokuGrid.kt` -- changed `editableColor` to `fixedColor` for highlighted notes.

### Issue 6: Statistics not showing (old DB)
**Skipped by design** -- the Compose version uses a separate database (`sudoku_compose_db`). Old data lives in `sudoku_database` from the legacy app. These are intentionally separate.

### Issue 7: "Экспертная" tab wraps to 2 lines
**Root cause**: Tab text too long for narrow screens, no line limit set.
**Fix**: `DifficultyTabs.kt` -- added `maxLines = 1` to the `Text` composable inside each Tab.

### Issue 8: Navigation bar not transparent
**Root cause**: `enableEdgeToEdge()` alone may not enforce transparent nav bar on all devices.
**Fix**: `ComposeActivity.kt` -- added explicit `window.navigationBarColor = android.graphics.Color.TRANSPARENT` after `enableEdgeToEdge()`.

### Issue 9: Splash screen white background in dark theme
**Root cause**: The outer `Box` in `SplashScreenContent.kt` had no `.background()` modifier, so the Activity XML theme background (white) showed through. Additionally, the night theme XML had no `windowBackground` override.
**Fix**:
- `SplashScreenContent.kt` -- added `.background(AppTheme.colors.background)` to the outer Box.
- `values-night/themes.xml` -- added `<item name="android:windowBackground">@android:color/black</item>` so pre-Compose rendering is dark.

## Files Changed
- `feature/gameover/presentation/components/screencontent/GameOverScreenContent.kt` -- emoji instead of text for difficulty
- `feature/game/presentation/viewmodel/GameViewModel.kt` -- guard against overwriting correct cells
- `core/theme/Color.kt` -- increased highlight alpha values
- `feature/game/presentation/components/SudokuGrid.kt` -- bolder/larger notes, black/white highlight color
- `feature/statistic/presentation/components/DifficultyTabs.kt` -- maxLines = 1
- `activity/ComposeActivity.kt` -- explicit transparent nav bar
- `feature/splash/presentation/components/screencontent/SplashScreenContent.kt` -- background color on outer Box
- `app/src/main/res/values-night/themes.xml` -- dark windowBackground

## Verification
**Compilation**: PASS (BUILD SUCCESSFUL)
**Tests**: Not run (no tests directly affected by these UI/logic changes)
**Similar patterns checked**: Verified `onErase` has the same guard as `onNumberClicked`

## Follow-up
- Issue 6 (old DB statistics): Users migrating from the legacy Java app will not see their old statistics. A migration utility could be built if needed.

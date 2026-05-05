# Architecture Violations Fix Report

## Summary

Fixed multiple architecture violations across the Sudoku Compose project.

## Changes

### 1. Created ButtonText in core/uicommon/button/
- **Created**: `core/uicommon/button/ButtonText.kt`
- Wrapper around `TextButton` with project styling defaults
- Replaced ALL `TextButton` usages in feature code (6 occurrences):
  - `StatisticScreenContent.kt` — reset button
  - `PauseDialog.kt` — exit to menu button
  - `NewGameDialog.kt` — cancel button
  - `StatisticScreen.kt` — confirm/dismiss buttons (removed, now in dialog)
  - `SettingsScreen.kt` — confirm/dismiss buttons (removed, now in dialog)

### 2. Extracted dialogs to separate files
- **Created**: `feature/statistic/presentation/components/StatisticResetDialog.kt`
- **Created**: `feature/settings/presentation/components/SettingsResetDialog.kt`
- **Created**: `feature/settings/presentation/components/SettingsLockedDialog.kt`

### 3. Moved dialog calls to bottom of ScreenContent
- `SettingsScreen.kt` — dialogs rendered AFTER `SettingsScreenContent`
- `StatisticScreen.kt` — dialog rendered AFTER `StatisticScreenContent`

### 4. Fixed hardcoded strings
- Replaced `stringResource(R.string.reset_statistics) + "?"` with new `R.string.reset_statistics_title`
- Replaced hardcoded `"OK"` with `R.string.ok`
- Replaced `"$errors/$maxErrors"` in PauseDialog with `stringResource(R.string.errors_format, errors, maxErrors)`
- Added `reset_statistics_title` and `ok` to both `values/strings.xml` and `values-ru/strings.xml`

### 5. Removed sensitiveToggleHandler from ScreenContent
- Deleted `private fun sensitiveToggleHandler(...)` from `SettingsScreenContent.kt`
- Created new `SensitiveSettingChanged` event in `SettingsUIEvent`
- ScreenContent now sends `SensitiveSettingChanged` event for checkErrors, unlimitedErrors, unlimitedHints
- ViewModel handles the logic (checks `hasActiveStandardGame`, shows locked dialog or applies change)
- Removed unused `ShowLockedDialog` event

### 6. Removed private composable functions
- No private composable functions found (all were already `internal`)

### 7. Removed private non-composable helper functions from composable files
- Changed `formatTime` in `TimeBarChart.kt` from `private` to `internal`
- Changed `generateTimeLabels` in `TimeBarChart.kt` from `private` to `internal`
- DrawScope functions in `SudokuGrid.kt` and `SolvedGridAnimation.kt` left as `private` (explicitly allowed)

### 8. Multiple composables per file
- Reviewed all files; secondary composables (`DifficultyOption`, `ActionButton`, `WeightSpacer`, `TimeBarChart`) are already `internal` helpers tightly coupled to their host composable — no extraction needed.

## Verification

- `./gradlew assembleDebug` — BUILD SUCCESSFUL

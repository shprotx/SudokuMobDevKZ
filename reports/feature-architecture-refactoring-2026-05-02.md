# Feature: Architecture Refactoring

**Date**: 2026-05-02
**Status**: BUILD SUCCESSFUL

## Task 1: Split multi-class files

| Original file | New files |
|---|---|
| `GameContract.kt` (deleted) | `CellData.kt`, `GameUiState.kt`, `GameEvent.kt`, `GameEffect.kt` |
| `StatisticContract.kt` (deleted) | `StatisticUiState.kt`, `StatisticEvent.kt`, `StatisticEffect.kt` |
| `FirebaseApi.kt` | `FirebaseApi.kt` (interface only), `FirebaseStatDto.kt`, `CrashDto.kt` |
| `SettingsRepository.kt` | `AppSettings.kt`, `SettingsRepository.kt` (class only) |
| `SudokuRepository.kt` | `PercentileResult.kt` extracted |
| `Type.kt` | `TextStyles.kt` extracted |

## Task 2: GameViewModel nav args

Changed from `savedStateHandle.get<Type>("key")` to `savedStateHandle.toRoute<GameRoutes.GameScreen>()`.

## Task 3: Screen/ScreenContent split

| Screen | ScreenContent |
|---|---|
| `GameScreen.kt` | `GameScreenContent.kt` |
| `MenuScreen.kt` | `MenuScreenContent.kt` |
| `StatisticScreen.kt` | `StatisticScreenContent.kt` |
| `SettingsScreen.kt` | `SettingsScreenContent.kt` |
| `HowToPlayScreen.kt` | `HowToPlayScreenContent.kt` |
| `GameOverScreen.kt` | `GameOverScreenContent.kt` |
| `SplashScreen.kt` | `SplashScreenContent.kt` |

Screen composables handle: ViewModel injection, state collection, effect handling, dialog state.
ScreenContent composables: pure UI, accept state + callbacks.

## Verification

- `./gradlew assembleDebug` -- BUILD SUCCESSFUL (warnings only, no errors)

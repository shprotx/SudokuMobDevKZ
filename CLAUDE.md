# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Android Sudoku game (100% Java) distributed via APK (GitHub Releases). Package: `ru.shprot.sudokumobdevkz`. Features 3 difficulty levels, statistics tracking with Firebase percentiles, 3 themes, notes, hints, undo, auto-save, and in-app auto-update.

## Build & Run

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK
./gradlew testDebugUnitTest      # Run unit tests
./gradlew connectedDebugAndroidTest  # Run instrumented tests
./gradlew clean                  # Clean build artifacts
```

- AGP 8.x, compileSdk/targetSdk 36, minSdk 24, Java 17
- Single `app` module, no multi-module setup

## Architecture

**MVVM** with Android Navigation Component:

- `ui/` — Fragments and Dialogs (GameFragment, MainFragment, SettingsFragment, StatisticFragment, PauseDialog, etc.)
- `viewmodel/` — ViewModels per screen (GameViewModel, MenuViewModel, GameOverViewModel, StatisticViewModel)
- `model/` — Data layer
  - `Repository` — single repository class, DB access via Room (background threads via ExecutorService)
  - `FirebaseSync` — statistics sync with Firebase Realtime Database via REST API (no SDK)
  - `database/` — Room DB (`SudokuDatabase`, singleton) with 3 DAOs: SquareDao, GameStateDao, StatisticDao
  - `game/` — Domain entities: `Square` (cell), `GameState`, `Statistic`
  - `game/generator/` — Puzzle generation using Dancing Links algorithm
  - `game/utils/` — Constants (`Library`), adapters, custom views (`SquareCardView`, `SquareCardLayout`, `MyCardLayout`)
  - `update/` — Auto-update: `UpdateChecker` (GitHub API), `ApkDownloader` (download + install via FileProvider), `UpdateInfo`

**Key data flow:** ViewModels → Repository → Room DAOs → Callbacks back to Fragments.

**Navigation:** Single-activity (`MainActivity`) with `nav_graph.xml`. `FirstActivity` is a splash screen with auto-update check.

## Auto-Update

On launch, `FirstActivity` checks GitHub Releases API for a newer version. If found, shows a `BottomSheetDialog` with release notes and download progress. APK is installed via `FileProvider`. User can skip a version ("Later" saves skipped version to SharedPreferences).

## Room Database

3 entities: `Square`, `GameState`, `Statistic`. DB version 1 with `fallbackToDestructiveMigration()`.

## CI/CD

GitHub Actions workflow (`.github/workflows/release.yml`): on PR merge to master → build release APK → create GitHub Release with tag `vX.Y.Z`. Release notes are taken from the `## Что нового` section of the PR body.

## Notable Libraries

- **Neumorphism** (`com.github.fornewid:neumorphism`) — UI styling
- **SSP/SDP** (`com.intuit.ssp` / `com.intuit.sdp`) — scalable size units
- **Google Play In-App Review** — rating prompts

## UI

XML layouts with ViewBinding (no Compose). Custom views for the Sudoku grid. Dialogs use `MaterialAlertDialogBuilder` with `RoundedCornersDialog` style and neumorphism buttons to match the app theme.

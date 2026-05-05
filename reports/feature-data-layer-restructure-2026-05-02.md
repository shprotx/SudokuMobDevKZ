# Feature: Data Layer Restructure (model/ -> core/base/)

**Date**: 2026-05-02
**Profile**: Feature

## Summary

Eliminated the `model/` directory and moved all data-layer files into `core/base/` following Clean Architecture structure.

## Moves performed

| Old path | New path |
|---|---|
| `model/database/` (DB, DAOs, entities) | `core/base/data/database/` |
| `model/remote/` (FirebaseApi, DTOs) | `core/base/data/remote/` |
| `model/repository/` (repos, settings, data classes) | `core/base/data/repository/` |
| `model/generator/` (SudokuGenerator, DLX solver) | `core/base/domain/generator/` |
| `model/ComposeCrashReporter.kt` | `core/base/data/CrashReporter.kt` (renamed class) |

## Files created: 22
## Files modified (imports): 12
## Files deleted: 22 (entire model/ directory)

## Renames
- `ComposeCrashReporter` -> `CrashReporter` (class + object name)

## Verification
- `./gradlew assembleDebug` -> BUILD SUCCESSFUL
- Grep for old `model.` imports -> 0 matches
- Grep for `ComposeCrashReporter` -> 0 matches

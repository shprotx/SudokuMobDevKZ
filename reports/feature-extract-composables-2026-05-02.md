# Extract composables: one composable per file

**Date**: 2026-05-02
**Profile**: Feature

## What was done

Extracted composable functions from 7 multi-composable files into individual files (one composable per file).

## Changes

### 1. SettingsSection.kt (deleted) -> 5 new files
- `SettingsSectionHeader.kt`
- `SettingsCard.kt`
- `SettingsToggleItem.kt`
- `SettingsNavItem.kt`
- `SettingsDivider.kt`

### 2. GameActionsBar.kt -> extracted ActionButton.kt

### 3. MenuNavCard.kt (renamed to MenuNavCard.kt with only MenuNavCard) + new MenuNavigationCards.kt

### 4. DifficultySelector.kt -> extracted DifficultyCard.kt

### 5. OverviewCards.kt -> extracted OverviewCard.kt

### 6. GameStatisticsSection.kt -> extracted StatRow.kt + StatDivider.kt

### 7. TimeBarChart.kt (now only TimeBarChart + helper functions) + new TimeChartSection.kt

## Verification

- `./gradlew assembleDebug` — BUILD SUCCESSFUL

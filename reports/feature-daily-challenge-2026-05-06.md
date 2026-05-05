# Daily Challenge — Implementation Report

**Date:** 2026-05-06
**Branch:** feature/compose-migration
**Spec:** `docs/specs/daily-challenge-spec.md`

## Summary

Реализована фича Daily Challenge: одна головоломка в день, одинаковая для всех пользователей (seed-based от даты), с системой стрика и отдельным экраном запуска.

## Files Created (15)

### Data layer
- `core/base/data/database/entity/DailyChallengeEntity.kt`
- `core/base/data/database/dao/DailyChallengeDao.kt`
- `core/base/data/database/migration/DailyChallengeMigration.kt`
- `core/base/data/repository/DailyChallengeRepository.kt`

### Feature module `feature/dailychallenge/`
- `presentation/navigation/DailyChallengeRoutes.kt`
- `presentation/contract/DailyChallengeUIState.kt`
- `presentation/contract/DailyChallengeUIEvent.kt`
- `presentation/contract/DailyChallengeUIEffect.kt`
- `presentation/viewmodel/DailyChallengeViewModel.kt`
- `presentation/screen/DailyChallengeScreen.kt`
- `presentation/components/screencontent/DailyChallengeScreenContent.kt`
- `presentation/components/DailyHeroCard.kt`
- `presentation/components/DailyResultCard.kt`
- `presentation/components/ResultStat.kt`
- `presentation/components/StreakBadge.kt`

### Game over
- `feature/gameover/presentation/components/StreakBanner.kt`

## Files Modified (~17)

### Core
- `core/base/domain/generator/SudokuGenerator.kt` — добавлен overload `generate(difficulty, seed: Long)`, все источники рандома (`Math.random()`, `shuffle()`) принимают `kotlin.random.Random`.
- `core/base/data/database/SudokuComposeDatabase.kt` — version 4, новый entity и DAO.
- `di/DatabaseModule.kt` — `MIGRATION_3_4` подключена ДО `fallbackToDestructiveMigration()`, добавлен `provideDailyChallengeDao`.

### Menu
- `MenuUIState` — поля `dailyChallengeStreak`, `isDailyChallengeCompleted`.
- `MenuUIEvent.DailyChallengeClicked`.
- `MenuUIEffect.NavigateToDailyChallenge`.
- `MenuViewModel` — inject `DailyChallengeRepository`, загрузка стрика и статуса при `init`/`ScreenResumed`.
- `MenuScreen` — навигация на `DailyChallengeRoutes.DailyChallengeScreen`.
- `DailyChallengeCard` — рефактор: убран хардкод `"0/3"`, новые параметры `streak`, `isCompleted`, `onClick`.
- `MenuPortraitContent`/`MenuLandscapeContent` — карточка вставлена после "Continue" и перед `DifficultySelector`.

### Game
- `GameRoutes.GameScreen` — параметр `isDailyChallenge: Boolean`.
- `GameUIState` — поле `isDailyChallenge`.
- `GameUIEffect.NavigateToGameOver` — параметры `isDailyChallenge`, `newStreak`.
- `GameViewModel` — inject `DailyChallengeRepository`. При `isDailyChallenge`: использует `SudokuGenerator.generate(difficulty, seed)`, не сохраняет saved game, не пишет в общую статистику. При win → `markCompleted` → передаёт `newStreak` в эффект.
- `GameScreen` — пробрасывает новые поля в `GameOverRoutes.GameOverScreen`.

### Game Over
- `GameOverRoutes.GameOverScreen` — `isDailyChallenge`, `newStreak`.
- `GameOverUIState` — те же поля.
- `GameOverViewModel` — читает из route. При `isDailyChallenge` PlayAgain → `NavigateToMenu`.
- `GameOverScreenContent` — условный `StreakBanner` (на win показывает стрик, на lose — "попробуй снова"). При daily выводит только одну кнопку «В меню».

### Nav
- `SudokuNavHost` — `composable<DailyChallengeRoutes.DailyChallengeScreen>`.

### Strings
- 6 новых строк × 8 локалей (`streak_days`, `daily_completed`, `daily_play`, `daily_try_tomorrow`, `daily_try_again`, `daily_streak_label`).

## Key Decisions Followed

- Seed-based генерация: `seed = "yyyy-MM-dd".replace("-","").toLong()`.
- Сложность дня: `Difficulty.entries[dayOfYear % 3]`.
- Реальная `Migration(3, 4)` — никаких потерь данных при апгрейде.
- Daily challenge не записывается в общую статистику и не использует `SavedGame`.
- При проигрыше стрик не начисляется, можно переиграть в тот же день.
- Стрик считается обратным проходом по датам через DAO.

## Verification

```
./gradlew assembleDebug
BUILD SUCCESSFUL in 20s
```

Никаких compile errors, только pre-existing warnings (Difficulty annotations target, `fallbackToDestructiveMigration` deprecated — не относятся к этой фиче).

## Manual Smoke (To Do on Device)

1. Запустить меню → видна `DailyChallengeCard` с иконкой огня (без `0/3`).
2. Тап → `DailyChallengeScreen` с датой, сложностью дня и стриком.
3. Тап «Играть» → `GameScreen`, головоломка генерируется детерминированно.
4. Решить → `GameOverScreen` со стрик-баннером, кнопка «В меню».
5. Вернуться в меню → карточка показывает иконку галочки (completed).
6. Открыть DailyChallengeScreen снова → карточка результата с временем/ошибками; кнопка «Играть» скрыта.
7. Перезапустить приложение в тот же день → состояние сохранилось.

## Known Limitations / Future Work

- Streak обнуляется при пропуске дня (Duolingo-style) — соответствует спецификации.
- Locale для формата даты использует системный default (`DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)`).
- При проигрыше пользователь может переиграть, но для UX-простоты после lose попадаешь сразу на меню — `PlayAgain` для daily ведёт в меню. Если нужно «переиграть в тот же день», открыть карточку на главной снова.
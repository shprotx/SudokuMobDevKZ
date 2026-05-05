# Daily Challenge — Implementation Spec

## Status: APPROVED, ready for implementation

## Overview

Daily Challenge — ежедневная головоломка, одинаковая для всех пользователей (seed-based генерация от даты). Одна головоломка в день, сложность вращается. Streak (серия дней подряд) — основная мотивация.

## Approved Decisions

| Вопрос | Решение |
|--------|---------|
| Генерация пазла | Seed-based: `seed = dateKey.replace("-","").toLong()` → `Random(seed)` → генератор |
| Сложность дня | Вращение: `Difficulty.entries[dayOfYear % 3]` |
| "0/3" в карточке | **Убрано**. Показываем только streak (🔥 N) |
| UI flow | Menu → DailyChallengeScreen → GameScreen → GameOverScreen |
| Повторная игра | Если решено — кнопка неактивна, показан результат |
| Статистика | Daily Challenge НЕ влияет на общую статистику |
| DB миграция | Реальная `Migration(3, 4)` — только CREATE TABLE |
| При проигрыше | Стрик не начисляется. Можно переиграть в тот же день |
| Стрик при пропуске | Обнуляется (Duolingo-style) |
| Timezone | `LocalDate.now()` по часовому поясу устройства — это ОК |

## Navigation Flow

```
MenuScreen
  ↓ tap DailyChallengeCard → MenuUIEffect.NavigateToDailyChallenge
DailyChallengeScreen (дата, difficulty, streak, isCompleted)
  ↓ tap "Играть" → DailyChallengeUIEffect.NavigateToGame(difficultyOrdinal)
GameScreen(difficultyOrdinal, isDailyChallenge = true)
  ↓ win → markCompleted() → GameUIEffect.NavigateToGameOver(..., isDailyChallenge=true, newStreak=N)
  ↓ lose → GameUIEffect.NavigateToGameOver(..., isDailyChallenge=true, newStreak=0)
GameOverScreen(isDailyChallenge, newStreak)
  ↓ "В меню" → popBackStack to Menu
```

При проигрыше (lose): стрик не начисляется, markCompleted не вызывается. Пользователь может вернуться и попробовать снова в тот же день.

## Data Model

### Room Entity: `DailyChallengeEntity`

```kotlin
@Entity(tableName = "daily_challenge_table")
data class DailyChallengeEntity(
    @PrimaryKey val dateKey: String,       // "2026-05-06"
    val difficultyOrdinal: Int,            // 0/1/2
    val isCompleted: Boolean,              // решена ли
    val completionTimeSeconds: Int,        // время решения (0 если не решена)
    val errors: Int,                       // ошибок при решении
    val completedAt: Long,                 // epoch ms (0 если не решена)
)
```

### Room DAO: `DailyChallengeDao`

```kotlin
@Dao
interface DailyChallengeDao {
    @Query("SELECT * FROM daily_challenge_table WHERE dateKey = :dateKey")
    suspend fun getByDate(dateKey: String): DailyChallengeEntity?

    @Upsert
    suspend fun upsert(entity: DailyChallengeEntity)

    @Query("SELECT * FROM daily_challenge_table WHERE isCompleted = 1 ORDER BY dateKey DESC LIMIT :limit")
    suspend fun getRecentCompleted(limit: Int): List<DailyChallengeEntity>
}
```

### Repository: `DailyChallengeRepository`

```kotlin
@Singleton
class DailyChallengeRepository @Inject constructor(
    private val dao: DailyChallengeDao,
) {
    fun todayDateKey(): String = LocalDate.now().toString()  // "2026-05-06"

    fun dailySeed(dateKey: String): Long = dateKey.replace("-", "").toLong()

    fun difficultyForDate(dateKey: String): Difficulty {
        val dayOfYear = LocalDate.parse(dateKey).dayOfYear
        return Difficulty.entries[dayOfYear % 3]
    }

    suspend fun getTodayChallenge(): DailyChallengeEntity {
        val dateKey = todayDateKey()
        return dao.getByDate(dateKey) ?: DailyChallengeEntity(
            dateKey = dateKey,
            difficultyOrdinal = difficultyForDate(dateKey).ordinal,
            isCompleted = false,
            completionTimeSeconds = 0,
            errors = 0,
            completedAt = 0L,
        )
    }

    suspend fun markCompleted(dateKey: String, timeSeconds: Int, errors: Int): Int {
        dao.upsert(
            DailyChallengeEntity(
                dateKey = dateKey,
                difficultyOrdinal = difficultyForDate(dateKey).ordinal,
                isCompleted = true,
                completionTimeSeconds = timeSeconds,
                errors = errors,
                completedAt = System.currentTimeMillis(),
            )
        )
        return getCurrentStreak()
    }

    suspend fun getCurrentStreak(): Int {
        var streak = 0
        val today = todayDateKey()
        val todayEntity = dao.getByDate(today)
        if (todayEntity?.isCompleted == true) streak++

        var date = LocalDate.now().minusDays(1)
        while (true) {
            val entity = dao.getByDate(date.toString())
            if (entity?.isCompleted == true) {
                streak++
                date = date.minusDays(1)
            } else break
        }
        return streak
    }
}
```

## SudokuGenerator Changes

Текущий генератор (`core/base/domain/generator/SudokuGenerator.kt`) использует:
- `(Math.random() * size).toInt()` в `generateGrid()`
- `positions.shuffle()` в `digForExpert()`
- `zeros.shuffle()` в `openSomeCells()`

### Изменения:

1. Добавить internal `generateInternal(difficulty: Difficulty, random: Random): SudokuPuzzle`
2. Перенести всю логику текущего `generate(difficulty)` в `generateInternal`, заменив:
   - `(Math.random() * size).toInt()` → `random.nextInt(size)`
   - `.shuffle()` → `.shuffle(random)`
3. Публичный `generate(difficulty)` → вызывает `generateInternal(difficulty, Random)`
4. Новый публичный `generate(difficulty, seed: Long)` → вызывает `generateInternal(difficulty, Random(seed))`

Существующий контракт не ломается. Все текущие вызовы продолжают работать.

## DB Migration

```kotlin
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS daily_challenge_table (
                dateKey TEXT NOT NULL PRIMARY KEY,
                difficultyOrdinal INTEGER NOT NULL,
                isCompleted INTEGER NOT NULL,
                completionTimeSeconds INTEGER NOT NULL,
                errors INTEGER NOT NULL,
                completedAt INTEGER NOT NULL
            )
        """)
    }
}
```

В `SudokuComposeDatabase`:
- version = 4
- Добавить `DailyChallengeEntity::class` в `@Database(entities = [...])`
- Добавить `abstract fun dailyChallengeDao(): DailyChallengeDao`

В `DatabaseModule`:
- `.addMigrations(MIGRATION_3_4)` ПЕРЕД `.fallbackToDestructiveMigration()`
- Добавить `@Provides fun provideDailyChallengeDao(db): DailyChallengeDao`

## Feature Module: `feature/dailychallenge/`

### Contract Files

**DailyChallengeUIState.kt**
```kotlin
data class DailyChallengeUIState(
    val dateLabel: String = "",
    val difficulty: Difficulty = Difficulty.EASY,
    val streak: Int = 0,
    val isCompletedToday: Boolean = false,
    val completionTimeSeconds: Int = 0,
    val errors: Int = 0,
    val isLoading: Boolean = true,
) : UIState
```

**DailyChallengeUIEvent.kt**
```kotlin
sealed interface DailyChallengeUIEvent : UIEvent {
    data object PlayClicked : DailyChallengeUIEvent
    data object BackClicked : DailyChallengeUIEvent
}
```

**DailyChallengeUIEffect.kt**
```kotlin
sealed interface DailyChallengeUIEffect : UIEffect {
    data object NavigateBack : DailyChallengeUIEffect
    data class NavigateToGame(val difficultyOrdinal: Int) : DailyChallengeUIEffect
}
```

### DailyChallengeViewModel.kt

```kotlin
@HiltViewModel
class DailyChallengeViewModel @Inject constructor(
    private val repository: DailyChallengeRepository,
) : BaseViewModel<DailyChallengeUIEvent, DailyChallengeUIState, DailyChallengeUIEffect>() {

    override fun createInitialState() = DailyChallengeUIState()

    init { loadChallenge() }

    override fun handleUIEvent(event: DailyChallengeUIEvent) =
        when (event) {
            DailyChallengeUIEvent.BackClicked ->
                setEffect(DailyChallengeUIEffect.NavigateBack)
            DailyChallengeUIEvent.PlayClicked ->
                setEffect(DailyChallengeUIEffect.NavigateToGame(currentState.difficulty.ordinal))
        }

    private fun loadChallenge() {
        viewModelScope.launch {
            val challenge = repository.getTodayChallenge()
            val streak = repository.getCurrentStreak()
            val dateLabel = formatDate(repository.todayDateKey())
            updateState {
                copy(
                    dateLabel = dateLabel,
                    difficulty = Difficulty.entries[challenge.difficultyOrdinal],
                    streak = streak,
                    isCompletedToday = challenge.isCompleted,
                    completionTimeSeconds = challenge.completionTimeSeconds,
                    errors = challenge.errors,
                    isLoading = false,
                )
            }
        }
    }

    private fun formatDate(dateKey: String): String {
        val date = LocalDate.parse(dateKey)
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
    }
}
```

### Navigation: `DailyChallengeRoutes.kt`

```kotlin
@Serializable
sealed class DailyChallengeRoutes {
    @Serializable
    data object DailyChallengeScreen : DailyChallengeRoutes()
}
```

### Screen / ScreenContent

**DailyChallengeScreen.kt** — thin wrapper: collectAsState, LaunchedEffect for effects.

**DailyChallengeScreenContent.kt** — pure UI:
- Заголовок с датой
- Иконка огня + badge difficulty дня
- Streak row (🔥 × N)
- Если `isCompletedToday`: показать результат (время, ошибки), кнопка "Играть" неактивна
- Если не решена: кнопка "Играть"
- Кнопка "Назад"

## Changes to Existing Screens

### Menu (5 files)

**MenuUIState.kt** — добавить:
```kotlin
val dailyChallengeStreak: Int = 0,
val isDailyChallengeCompleted: Boolean = false,
```

**MenuUIEvent.kt** — добавить:
```kotlin
data object DailyChallengeClicked : MenuUIEvent
```

**MenuUIEffect.kt** — добавить:
```kotlin
data object NavigateToDailyChallenge : MenuUIEffect
```

**MenuViewModel.kt** — inject `DailyChallengeRepository`, загрузить streak в init, обработать event.

**MenuScreen.kt** — обработать эффект:
```kotlin
MenuUIEffect.NavigateToDailyChallenge ->
    navController.navigate(DailyChallengeRoutes.DailyChallengeScreen)
```

**MenuPortraitContent.kt / MenuLandscapeContent.kt** — добавить `DailyChallengeCard` после кнопки "New Game" / "Continue".

### DailyChallengeCard.kt (refactor)

Убрать хардкодный "0/3". Новые параметры:
```kotlin
@Composable
fun DailyChallengeCard(
    modifier: Modifier,
    streak: Int,
    isCompleted: Boolean,
    onClick: () -> Unit,
)
```

Показывать: 🔥 streak (если > 0), или "✓" если isCompleted.

### GameRoutes.kt

Добавить параметр:
```kotlin
@Serializable
data class GameScreen(
    val difficultyOrdinal: Int,
    val continueGame: Boolean = false,
    val isDailyChallenge: Boolean = false,  // NEW
)
```

### GameUIState.kt

Добавить:
```kotlin
val isDailyChallenge: Boolean = false,
```

### GameViewModel.kt

- Читать `isDailyChallenge` из route
- При `isDailyChallenge == true`:
  - Генерировать пазл через `generator.generate(difficulty, repository.dailySeed(today))`
  - При win: `repository.markCompleted(today, timeSeconds, errors)` → получить newStreak
  - НЕ вызывать `updateStatistic()` — daily challenge не влияет на общую статистику
  - Передать `isDailyChallenge = true, newStreak = N` в `NavigateToGameOver`
- При `isDailyChallenge == true` запретить создание saved game (нет паузы/продолжения)

### GameUIEffect.kt

Расширить `NavigateToGameOver`:
```kotlin
data class NavigateToGameOver(
    val isWin: Boolean,
    val time: Long,
    val difficulty: Difficulty,
    val isDailyChallenge: Boolean = false,  // NEW
    val newStreak: Int = 0,                 // NEW
)
```

### GameOverRoutes.kt

Добавить параметры:
```kotlin
@Serializable
data class GameOverScreen(
    val isWin: Boolean,
    val timeSeconds: Int,
    val difficultyOrdinal: Int,
    val isDailyChallenge: Boolean = false,  // NEW
    val newStreak: Int = 0,                 // NEW
)
```

### GameOverUIState.kt

Добавить:
```kotlin
val isDailyChallenge: Boolean = false,
val newStreak: Int = 0,
```

### GameOverScreenContent.kt

Условный блок:
```kotlin
if (uiState.isDailyChallenge && uiState.isWin) {
    // Показать: "🔥 Streak: N days!"
}
if (uiState.isDailyChallenge && !uiState.isWin) {
    // Показать: "Попробуй ещё раз сегодня"
}
```

## Implementation Order

| Шаг | Что | Файлы | Сложность |
|-----|-----|-------|-----------|
| 1 | SudokuGenerator seed overload | 1 файл modify | Низкая |
| 2 | Room entity + DAO + Migration | 3 create, 1 modify | Низкая |
| 3 | DailyChallengeRepository | 1 create | Средняя |
| 4 | DatabaseModule DI | 1 modify | Низкая |
| 5 | Feature contracts (State/Event/Effect) | 3 create | Низкая |
| 6 | DailyChallengeViewModel | 1 create | Низкая |
| 7 | DailyChallengeRoutes | 1 create | Низкая |
| 8 | DailyChallengeScreen + ScreenContent | 2 create | Средняя |
| 9 | Refactor DailyChallengeCard | 1 modify | Низкая |
| 10 | Menu integration (state/event/effect/VM/UI) | 6 modify | Средняя |
| 11 | NavHost registration | 1 modify | Низкая |
| 12 | GameRoutes + GameUIState + GameViewModel | 3 modify | Средняя |
| 13 | GameOver integration | 4 modify | Низкая |
| 14 | Build + verify | — | — |

**Total: ~8 new files, ~17 modified files**

## String Resources Needed

Уже есть:
- `daily_challenge` — "Daily challenge" / "Ежедневная задача"
- `daily_challenge_desc` — "New puzzle every day" / "Новая головоломка каждый день"
- `progress` — "Progress" / "Прогресс" (может не пригодиться без "0/3")

Нужно добавить:
- `streak_days` — "🔥 %d days" / "🔥 %d дней"
- `daily_completed` — "Completed!" / "Решено!"
- `daily_play` — "Play" / "Играть"
- `daily_try_tomorrow` — "Come back tomorrow" / "Возвращайся завтра"
- `daily_try_again` — "Try again" / "Попробуй ещё"
- `daily_streak_label` — "Current streak" / "Текущая серия"

## Risks

1. **SudokuGenerator determinism** — если генератор содержит недетерминированные вызовы помимо Random (например, HashMap iteration order), seed может не давать одинаковый результат. Нужно проверить после реализации.

2. **DB Migration** — CRITICAL: `Migration(3, 4)` MUST быть добавлена ДО `fallbackToDestructiveMigration()`. Иначе пользователи потеряют всю статистику.

3. **GameViewModel size** — и так 500+ строк. Daily challenge добавит ещё ~30-40 строк. Терпимо, но на грани.

4. **Streak off-by-one** — тщательно тестировать: streak = 0 (никогда не играл), streak = 1 (только сегодня), streak после пропуска дня.

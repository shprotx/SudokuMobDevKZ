# Migrate Android Sudoku to Jetpack Compose + Kotlin

## Context & Problem

Приложение написано на 100% Java с XML-layouts, ViewBinding, Navigation Component (Fragments),
neumorphism-библиотекой и обратными вызовами вместо реактивных потоков. Стек устарел:
- Java не позволяет использовать Kotlin Coroutines, Flow, extension-функции, data/sealed classes.
- ViewBinding + XML layouts дают высокий overhead при поддержке и тестировании UI.
- Neumorphism-библиотека (`com.github.fornewid:neumorphism`) несовместима с Compose; её кастомные
  `View` нельзя переиспользовать в Composable-дереве.
- `ExecutorService` + `Handler(Looper.getMainLooper())` в Repository/ViewModel заменяются на
  `viewModelScope` + `Dispatchers.IO`.
- SSP/SDP (`com.intuit.ssp`, `com.intuit.sdp`) не нужны в Compose — там `sp`/`dp` нативные.

Цель — полностью переписать приложение на Kotlin + Jetpack Compose, сохранив игровую логику и
формат хранения данных в Room.

---

## Goals

1. Перевести весь исходный код с Java на Kotlin.
2. Заменить XML-layouts на Jetpack Compose UI.
3. Внедрить MVI-архитектуру по образцу SapiStar2 (`UIEvent / UIState / UIEffect / BaseViewModel`).
4. Реализовать Compose Navigation вместо `nav_graph.xml` + Android Navigation Component.
5. Воссоздать дизайн из скриншотов (главный экран и экран статистики) с новой дизайн-системой.
6. Убрать neumorphism-зависимость; реализовать аналогичный soft-shadow эффект нативными
   Compose-средствами (elevation + custom `drawBehind`).
7. Сохранить Room-схему без миграции (entities `Square`, `GameState`, `Statistic` — без изменений
   в именах таблиц и колонок; DB version остаётся 1).
8. Удалить все Java-файлы в финальном этапе после полного перехода.
9. Сохранить CI/CD: GitHub Actions workflow `.github/workflows/release.yml` должен собирать APK
   без изменений в конфигурации.

---

## Non-Goals

- Compose Multiplatform / KMP — только Android.
- Hilt DI — не вводится (проект достаточно мал; ViewModel создаётся через `viewModel()` /
  `ViewModelProvider.Factory`; при желании вводится отдельной задачей).
- Переработка серверной части (Firebase RTDB REST API остаётся как есть).
- Изменение игровой логики (генератор, правила судоку, подсчёт статистики).
- Добавление новых фич в рамках этой задачи.

---

## User Stories

| # | Роль | Действие | Ожидание |
|---|------|----------|----------|
| 1 | Игрок | Открывает приложение | Видит сплеш с версией, проверка обновлений, переход на главный экран |
| 2 | Игрок | На главном экране выбирает сложность и нажимает «Новая игра» | Запускается новая игровая сессия выбранной сложности |
| 3 | Игрок | Играет в судоку | Сетка отрисована через Compose Canvas, выбор цифр, черновики, подсказки, undo работают |
| 4 | Игрок | Заканчивает игру | Показывается экран результата с временем и ошибками |
| 5 | Игрок | Открывает статистику | Видит табы по сложности, карточки обзора, bar chart, кнопку сброса |
| 6 | Игрок | Меняет тему в настройках | UI перестраивается через `MaterialTheme` / кастомный `AppTheme` |
| 7 | Игрок | Запускает приложение с доступным обновлением | BottomSheet с changelog и прогресс-баром скачивания |

---

## Scope

### Этап 1 — Зависимости Compose

Добавить в `app/build.gradle`:
- `androidx.compose:compose-bom` (последняя стабильная версия)
- `androidx.compose.ui:ui`, `ui-tooling-preview`, `ui-tooling`
- `androidx.compose.material3:material3`
- `androidx.activity:activity-compose`
- `androidx.navigation:navigation-compose`
- `androidx.lifecycle:lifecycle-viewmodel-compose`
- `androidx.lifecycle:lifecycle-runtime-compose`
- Kotlin плагин + `kotlinOptions { jvmTarget = "17" }`
- `buildFeatures { compose = true; composeOptions { kotlinCompilerExtensionVersion = ... } }`
- Удалить: `com.github.fornewid:neumorphism`, `com.intuit.ssp`, `com.intuit.sdp`
- Оставить: `room-runtime`, `room-ktx` (заменить `annotationProcessor` → `ksp` или `kapt`)

### Этап 2 — Дизайн-система

Создать пакет `ui/theme/`:
- `Color.kt` — цветовые токены для трёх тем (Light/Dark/Neumorphic-style)
- `Type.kt` — шрифтовая шкала (заголовки, тело, подписи)
- `Padding.kt` / `Size.kt` — константы отступов и размеров (аналог SSP/SDP, но через `dp`/`sp`)
- `Theme.kt` — `AppTheme` composable, принимает `AppThemeType` (enum: LIGHT, DARK, CLASSIC)
- `Shapes.kt` — `RoundedCornerShape` для карточек
- `ShadowModifier.kt` — `Modifier.neumorphicShadow(...)` через `drawBehind` с двумя тенями
  (светлая + тёмная) для замены `NeumorphButton` / `MyCardLayout`

### Этап 3 — Архитектура (по образцу SapiStar2)

Файловая структура пакета `ru.shprot.sudokumobdevkz`:

```
activity/
  MainActivity.kt            # единственный Activity, setContent { AppTheme { SudokuNavHost() } }
  navigation/
    SudokuNavHost.kt         # NavHost с маршрутами
    Screen.kt                # sealed class/enum маршрутов
core/
  base/
    presentation/
      contract/
        UIEvent.kt
        UIState.kt
        UIEffect.kt
      viewmodel/
        BaseViewModel.kt     # аналог SapiStar2 BaseViewModel (MutableStateFlow + Channel)
feature/
  splash/
    presentation/
      screen/SplashScreen.kt
      viewmodel/SplashViewModel.kt
      contract/Splash{Event,State,Effect}.kt
  menu/
    presentation/
      screen/MenuScreen.kt
      viewmodel/MenuViewModel.kt
      contract/Menu{Event,State,Effect}.kt
  game/
    presentation/
      screen/GameScreen.kt
      components/SudokuGrid.kt
      components/NumberPanel.kt
      components/GameToolbar.kt
      viewmodel/GameViewModel.kt
      contract/Game{Event,State,Effect}.kt
  gameover/
    presentation/
      screen/GameOverScreen.kt
      viewmodel/GameOverViewModel.kt
      contract/GameOver{Event,State,Effect}.kt
  statistic/
    presentation/
      screen/StatisticScreen.kt
      components/StatisticTabs.kt
      components/TimeBarChart.kt
      viewmodel/StatisticViewModel.kt
      contract/Statistic{Event,State,Effect}.kt
  settings/
    presentation/
      screen/SettingsScreen.kt
      viewmodel/SettingsViewModel.kt
      contract/Settings{Event,State,Effect}.kt
  howtoplay/
    presentation/
      screen/HowToPlayScreen.kt
model/                        # без изменений, переписывается на Kotlin
  Repository.kt
  FirebaseSync.kt
  CrashReporter.kt
  database/
    SudokuDatabase.kt
    SquareDao.kt
    GameStateDao.kt
    StatisticDao.kt
    DraftsVisibilityConverter.kt
  game/
    Square.kt                 # @Entity, data class
    GameState.kt              # @Entity, data class (Parcelable убирается)
    Statistic.kt              # @Entity, data class
    generator/
      Generator.kt
      Solver/
        DancingLinks.kt
        DancingLinksAlgorithm.kt
        DancingNode.kt
        ColumnNode.kt
  game/utils/
    AppConstants.kt           # замена Library.java
    AppRater.kt
  update/
    UpdateChecker.kt
    ApkDownloader.kt
    UpdateInfo.kt
```

### Этап 4 — Заглушки экранов с навигацией

- Реализовать `SudokuNavHost` с полным графом: Splash → Menu → Game → GameOver → Statistic →
  Settings → HowToPlay.
- Каждый экран — заглушка с `Text("Screen name")`.
- `MainActivity` использует `setContent`, убирается XML `activity_main.xml`.
- `FirstActivity` временно остаётся (splash + update), рефакторится позже.

### Этап 5 — Реализация экранов

Порядок реализации:

1. **Menu (главный экран)**: заголовок, карточка ежедневной задачи (прогресс из Room),
   кнопка «Новая игра», выбор сложности (Chip-группа), карточки навигации (Статистика,
   Как играть, Достижения, Настройки), `BottomNavigation`.
2. **Game**: `SudokuGrid` через `Canvas` (9×9 с разделителями 3×3), `NumberPanel` (1–9 + Erase),
   панель инструментов (Pause, Undo, Draft, Hint), таймер через `LaunchedEffect` + `ticker`.
3. **GameOver**: карточка с временем / ошибками / сложностью, кнопки «Снова» и «На главную».
4. **Statistic**: `TabRow` по трём сложностям, карточки обзора (bestTime, avgTime, %wins,
   winsWithoutErrors), `LazyColumn` детальной статистики, `Canvas`-bar chart динамики,
   кнопка сброса с подтверждением через `AlertDialog`.
5. **Settings**: переключатели ошибок/подсказок, выбор темы, сброс языка.
6. **HowToPlay**: `LazyColumn` с шагами из ресурсов.
7. **Splash + Update**: `SplashScreen` → проверка обновлений → `BottomSheetScaffold` с прогрессом.

### Этап 6 — Удаление Java-файлов

После прохождения всех тестов:
- Удалить все `.java` файлы из `src/main/java/`.
- Удалить XML layouts из `src/main/res/layout/`.
- Удалить `nav_graph.xml`.
- Удалить `annotationProcessor` из `build.gradle` (заменён `ksp`/`kapt`).
- Удалить зависимости neumorphism, ssp, sdp.

---

## Functional Requirements

### FR-1: Навигация
- Compose Navigation (`NavHostController`).
- Back-stack: GameOver → Game (restart) или GameOver → Menu.
- Deep link не требуется.
- Bottom navigation присутствует только на экранах Menu / Game / Statistic.

### FR-2: Главный экран (Menu)
- Заголовок «Sudoku» + подзаголовок (текущая дата или слоган).
- Карточка «Ежедневная задача»: `LinearProgressIndicator` от 0 до 1, подпись «X/3 выполнено».
  Логика: ежедневная задача = 3 победы за текущий день (считается по Room).
  Допущение: поле `completedToday: Int` добавляется в `GameState` или вычисляется из `Statistic`
  через поле `lastWinDate: Long` (новое поле).
- Кнопка «Новая игра» — `FilledButton` с кастомной тенью.
- Выбор сложности: три `FilterChip` (Лёгкая / Средняя / Сложная) + иконки уровня + точки-
  индикаторы под активным чипом.
- Карточки меню: 2×2 grid (`LazyVerticalGrid`) — Статистика, Как играть, Достижения, Настройки.
- `BottomNavigationBar`: три пункта — Главная / Игра / Статистика.

### FR-3: Игровой экран (Game)
- `SudokuGrid`: `Canvas` composable 9×9, жирные границы каждого 3×3 блока.
- Выделение: нажатая ячейка подсвечивается цветом `primary`; строка/столбец/регион — `primaryContainer`.
- Черновики: `isVisible=false` для заполненных цифр; черновики — мелкий текст 3×3 в ячейке.
- Панель цифр: `Row` из 9 кнопок 1–9 + кнопка «Стереть».
- Тулбар: Pause (останавливает таймер), Undo (ViewModel хранит `Stack<Pair<Int,Int>>`), Draft toggle,
  Hint (использует `possibleHints`).
- Ошибочный ввод: ячейка мигает красным (`animateColorAsState`), счётчик ошибок в `GameState`.
- При `errorCounter >= possibleMistakes` → навигация на `GameOver` с `isWin = false`.
- Пауза: `PauseBottomSheet` (Compose ModalBottomSheet) с кнопками «Продолжить» / «Новая игра».
- Автосохранение: `LaunchedEffect` + `snapshotFlow` на `GameUiState` → `repository.saveGame()`.

### FR-4: Экран статистики (Statistic)
- `TabRow` с тремя вкладками: Лёгкая (0) / Средняя (1) / Сложная (2).
- Карточки обзора: `bestTime` (формат MM:SS), `averageTime`, `percentOfWins`, `winsWithoutErrors`.
- `TimeBarChart`: Compose Canvas bar chart, данные — последние N завершённых игр (хранятся как
  новая Room-таблица `GameHistory` или как часть `Statistic`; см. Open Questions).
- Детальная статистика: `LazyColumn` из пар `(ключ, значение)`.
- Кнопка «Сбросить»: `AlertDialog` подтверждения → удаление записи `Statistic` по сложности +
  сброс Firebase.
- Кнопка «На главную»: `popBackStack()`.

### FR-5: Настройки (Settings)
- `Switch` «Безлимитные ошибки» (FLAG_MISTAKES).
- `Switch` «Безлимитные подсказки» (FLAG_HINTS).
- Выбор темы: три карточки (Light / Dark / Classic) с превью цветов.
- Тема применяется сразу через `ThemeViewModel` (shared ViewModel в `MainActivity`).
- Хранение: SharedPreferences (без изменений ключей из `Library`).

### FR-6: Обновление
- `UpdateChecker.kt` (Kotlin, suspend fun) вызывается в `SplashViewModel.init`.
- При наличии обновления: `ModalBottomSheet` с changelog, `LinearProgressIndicator` скачивания,
  кнопки «Установить» / «Позже».
- «Позже» сохраняет пропущенную версию в SharedPreferences (ключ `UPDATE_SKIPPED_VERSION`).

### FR-7: Crash Reporting
- `CrashReporter.kt` остаётся без изменений по функционалу; переписывается на Kotlin.
- Инициализируется в `Application.onCreate()` (добавить `Application` класс).

---

## API / Integration

### Room (без изменений схемы)

Таблицы:
- `square_table` — `Square` entity: `position`, `x`, `y`, `region`, `color`, `cellColor`, `value`,
  `draftsVisibility: IntArray`, `isVisible`.
- `game_state_table` — `GameState` entity: `id=0` (singleton), `difficulty`, `difficultyString`,
  `timer`, `time`, `isGameFinished`, `isDraftPressed`, `errorCounter`, `numbers: IntArray`,
  `emptySquareCounter`, `hintCounter`, `isDraftEnabled`, `isHintEnabled`, `isGamePaused`.
- `statistic_table` — `Statistic` entity: `difficulty` (PK), `allTime`, `bestTime`, `averageTime`,
  `gamesStarted`, `gamesWon`, `percentOfWins`, `winsWithoutErrors`, `bestWinsLine`, `currentWinsLine`.

DAOs: `SquareDao`, `GameStateDao`, `StatisticDao` — переписываются на Kotlin, `suspend fun` вместо
callback-обёрток через `ExecutorService`.

### Firebase REST API

`FirebaseSync.kt` — без изменений по протоколу; переписывается на Kotlin coroutines
(`withContext(Dispatchers.IO)` вместо `executor.execute`).
URL из `BuildConfig.FIREBASE_DB_URL` (сохраняется из `local.properties`).

### GitHub Releases API

`UpdateChecker.kt`:
- `suspend fun checkForUpdate(): UpdateInfo?`
- URL: `https://api.github.com/repos/<owner>/<repo>/releases/latest`
- Парсинг JSON через `org.json.JSONObject` (без Gson/Moshi — нет новой зависимости).

---

## Data Model

### Kotlin data classes (замена Java entities)

```kotlin
// Square.kt — @Entity, убрать Parcelable
@Entity(tableName = "square_table")
data class Square(
    @PrimaryKey val position: Int = 0,
    val x: Int = 0,
    val y: Int = 0,
    val region: Int = 0,
    val color: Int = 0,
    val cellColor: Int = 0,
    val value: Int = 0,
    @TypeConverters(DraftsVisibilityConverter::class)
    val draftsVisibility: IntArray = IntArray(9) { 4 },
    val isVisible: Boolean = false,
)

// GameState.kt — @Entity, убрать Parcelable
@Entity(tableName = "game_state_table")
data class GameState(
    @PrimaryKey val id: Int = 0,
    val difficulty: Int = 0,
    val difficultyString: String = "Unknown",
    val timer: String = "00:00",
    val time: Int = 0,
    val isGameFinished: Boolean = true,
    val isDraftPressed: Boolean = false,
    val errorCounter: Int = 0,
    @TypeConverters(DraftsVisibilityConverter::class)
    val numbers: IntArray = IntArray(10),
    val emptySquareCounter: Int = 81,
    val hintCounter: Int = 0,
    val isDraftEnabled: Boolean = true,
    val isHintEnabled: Boolean = true,
    val isGamePaused: Boolean = false,
)

// Statistic.kt — @Entity без изменений полей
```

### UI State (MVI)

```kotlin
// Пример GameUiState
data class GameUiState(
    val cells: List<Square> = emptyList(),
    val gameState: GameState = GameState(),
    val selectedPosition: Int = -1,
    val isLoading: Boolean = false,
    override val isLoadingFailed: Boolean = false,
) : UIState
```

---

## UX/UI

### Главный экран

```
┌─────────────────────────────────┐
│  Sudoku                         │  ← H1, bold
│  Тренируй мышление              │  ← Body, muted
├─────────────────────────────────┤
│  [Ежедневная задача]            │  ← Card, LinearProgress
│   ████░░░  1/3                  │
├─────────────────────────────────┤
│  [      НОВАЯ ИГРА       ]      │  ← FilledButton, green
├─────────────────────────────────┤
│  [Лёгкая] [Средняя] [Сложная]  │  ← FilterChip row
│     •                            │  ← активный индикатор
├─────────────────────────────────┤
│  [Статистика] [Как играть]      │  ← 2×2 grid cards
│  [Достижения] [Настройки]       │
├─────────────────────────────────┤
│  [Главная] [Игра] [Статистика]  │  ← BottomNavigation
└─────────────────────────────────┘
```

### Экран статистики

```
┌─────────────────────────────────┐
│  [Лёгкая] [Средняя] [Сложная]  │  ← TabRow
├─────────────────────────────────┤
│  [Лучшее: 02:41] [Среднее: —]  │  ← 2 карточки в ряд
│  [% побед: 67%]  [Без ошибок:1]│
├─────────────────────────────────┤
│  Динамика времени               │
│  ┌─┐ ┌─┐ ┌─┐ ┌─┐ ┌─┐          │  ← Bar chart Canvas
│  └─┘ └─┘ └─┘ └─┘ └─┘          │
├─────────────────────────────────┤
│  Игр начато:    12              │  ← LazyColumn items
│  Игр выиграно:  8               │
│  Серия побед:   3               │
├─────────────────────────────────┤
│  [Сбросить]   [На главную]      │  ← Кнопки внизу
└─────────────────────────────────┘
```

### Игровой экран

- Сетка занимает максимальную квадратную область (по ширине экрана).
- Над сеткой: таймер + счётчик ошибок + difficulty badge.
- Под сеткой: `NumberPanel` (1–9 + Erase в одну строку).
- Под `NumberPanel`: тулбар (Pause / Undo / Draft / Hint) в одну строку.

### Темы

| Параметр | Light | Dark | Classic (neumorphic) |
|---|---|---|---|
| Background | #F0F0F0 | #1C1C1E | #E0E5EC |
| Surface | #FFFFFF | #2C2C2E | #E0E5EC |
| Primary | #34C759 | #30D158 | #4CAF50 |
| Shadow light | — | — | #FFFFFF, α=0.9 |
| Shadow dark | — | — | #A3B1C6, α=0.7 |

---

## State & Flows

### MVI-паттерн (по SapiStar2)

```
UIEvent  ──► ViewModel.handleUIEvent()
                   │
                   ├──► setState(newState)  ──► uiState: StateFlow<UIState>  ──► UI collect
                   └──► setEffect(effect)  ──► effect: Flow<UIEffect>  ──► LaunchedEffect
```

### GameViewModel state machine

```
IDLE ──(StartGame)──► GENERATING ──(Ready)──► PLAYING
PLAYING ──(PauseClick)──► PAUSED ──(Resume)──► PLAYING
PLAYING ──(CellCorrect, emptySquareCounter=0)──► WIN ──► GameOver
PLAYING ──(CellWrong, errorCounter>=limit)──► LOSE ──► GameOver
PLAYING ──(UndoClick)──► PLAYING (rollback последнего хода)
```

### Автосохранение

```kotlin
LaunchedEffect(Unit) {
    snapshotFlow { viewModel.uiState.value }
        .debounce(500)
        .collect { state -> repository.saveGame(state.cells, state.gameState) }
}
```

---

## Non-Functional Requirements

| Параметр | Требование |
|---|---|
| minSdk | 24 (без изменений) |
| targetSdk / compileSdk | 36 (без изменений) |
| Kotlin | 2.0+ |
| Compose BOM | актуальная стабильная на момент реализации |
| JVM target | 17 |
| Размер APK | не превышает текущий более чем на 20% |
| Cold start | < 2 с на mid-range устройстве (Pixel 3a) |
| Recomposition | нет лишних полных recomposition на экране Game при вводе цифры |
| DB schema | version = 1, `fallbackToDestructiveMigration()` — без изменений |
| Тестируемость | ViewModel-логика покрывается unit-тестами без Android-зависимостей |

---

## Dependencies

### Добавляются

```kotlin
// Compose BOM
implementation(platform("androidx.compose:compose-bom:<latest>"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose:<latest>")
implementation("androidx.navigation:navigation-compose:<latest>")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:<latest>")
implementation("androidx.lifecycle:lifecycle-runtime-compose:<latest>")

// Room KTX (coroutines support)
implementation("androidx.room:room-ktx:<same version as room-runtime>")
ksp("androidx.room:room-compiler:<version>")  // или kapt

// Kotlin Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:<latest>")

// Debug
debugImplementation("androidx.compose.ui:ui-tooling")
debugImplementation("androidx.compose.ui:ui-test-manifest")
```

### Удаляются

```
com.github.fornewid:neumorphism
com.intuit.ssp:ssp-android
com.intuit.sdp:sdp-android
androidx.navigation:navigation-fragment (заменён navigation-compose)
androidx.navigation:navigation-ui
```

### Остаются

```
androidx.room:room-runtime
com.google.android.play:review
androidx.appcompat:appcompat (нужен до полного удаления FirstActivity)
```

---

## Migration Plan

| Этап | Ветка | Описание | Java-файлы |
|---|---|---|---|
| 1 | `feature/compose-deps` | Добавить зависимости Compose, kotlin plugin, не трогать Java | Все |
| 2 | `feature/design-system` | Создать `ui/theme/` пакет, `AppTheme`, `ShadowModifier` | Все |
| 3 | `feature/arch-base` | `BaseViewModel`, MVI-контракты, `SudokuNavHost` заглушки | Все |
| 4 | `feature/screen-menu` | `MenuScreen` + `MenuViewModel` | `MainFragment.java`, `MenuViewModel.java` |
| 5 | `feature/screen-game` | `GameScreen`, `SudokuGrid`, `NumberPanel` | `GameFragment.java`, `GameViewModel.java` |
| 6 | `feature/screen-gameover` | `GameOverScreen` | `GameOverFragment.java`, `GameOverViewModel.java` |
| 7 | `feature/screen-statistic` | `StatisticScreen`, `TimeBarChart` | `StatisticFragment.java`, `StatisticViewModel.java` |
| 8 | `feature/screen-settings` | `SettingsScreen` | `SettingsFragment.java` |
| 9 | `feature/screen-howtoplay` | `HowToPlayScreen` | `HowToPlayFragment.java`, `ItemHowFragment.java` |
| 10 | `feature/screen-splash` | `SplashScreen` + Update BottomSheet | `FirstActivity.java` |
| 11 | `feature/model-kotlin` | Перевести model/ на Kotlin (Repository, DAOs, entities) | `Repository.java`, все model/*.java |
| 12 | `feature/cleanup` | Удалить все `.java`, XML layouts, nav_graph.xml | Удаляются |

Правило: каждый этап — отдельный PR. Предыдущий Java-код остаётся рабочим до момента, когда
Kotlin-замена прошла smoke-test на реальном устройстве.

---

## Testing Strategy

### Unit tests (JVM, без Android)
- `GameViewModelTest`: логика хода, undo, обнаружение победы/поражения.
- `StatisticTest`: `Statistic.updateStatistic()` — все ветки.
- `GeneratorTest`: `Generator` возвращает валидное судоку (81 ячейка, все числа 1–9 в строках/столбцах/регионах).
- `UpdateCheckerTest`: парсинг JSON с версией.

### Instrumented tests
- `RoomMigrationTest`: убедиться, что fallbackToDestructiveMigration не ломает свежую установку.
- `NavigationTest`: проверить, что все маршруты NavHost достижимы.

### UI tests (Compose)
- `MenuScreenTest`: проверить наличие кнопки «Новая игра», chips сложности.
- `GameScreenTest`: ввод цифры → ячейка обновляется.
- `StatisticScreenTest`: переключение табов.

### Smoke (ручной / через `mcp__mobile`)
- Полный игровой сценарий: выбор сложности → новая игра → заполнение нескольких ячеек → пауза →
  возобновление → победа (или проигрыш) → переход на главную.
- Смена темы → экран перестраивается без пересоздания Activity.
- Обновление: мок GitHub API возвращает новую версию → BottomSheet → «Позже» → повторный запуск
  не показывает BottomSheet.

---

## Acceptance Criteria

- [ ] Все Java-файлы удалены в финальном PR (этап 12).
- [ ] `./gradlew assembleRelease` проходит без предупреждений о deprecated Java API.
- [ ] `./gradlew testDebugUnitTest` — все unit-тесты зелёные.
- [ ] `./gradlew connectedDebugAndroidTest` — все instrumented-тесты зелёные.
- [ ] На устройстве с minSdk=24 приложение запускается, новая игра запускается, статистика
  отображается.
- [ ] Смена темы в настройках применяется без рестарта Activity.
- [ ] При наличии обновления BottomSheet отображается на сплеш-экране.
- [ ] APK собирается в GitHub Actions workflow без изменений в `.github/workflows/release.yml`.
- [ ] Room-схема не требует миграции (schema version остаётся 1).
- [ ] Нет предупреждений о `@Deprecated` в Compose API (версия BOM — стабильная).
- [ ] `TimeBarChart` отображает данные без крашей при пустой статистике.

---

## Risks & Open Questions

### Риски

| # | Риск | Вероятность | Митигация |
|---|------|------------|-----------|
| R1 | Neumorphic shadow через `drawBehind` выглядит иначе чем оригинал | Средняя | Договориться о допустимом визуальном расхождении до начала этапа 2 |
| R2 | `SudokuGrid` на Canvas — performance на старых устройствах (minSdk 24) | Низкая | Замерить с `recompose highlighter`, использовать `remember` для paint-объектов |
| R3 | Room с `IntArray` + `TypeConverter` в Kotlin data class — equals/hashCode | Высокая | Переопределить equals/hashCode в `Square` и `GameState` или использовать `@Ignore` для diffing |
| R4 | Параллельное существование Java и Kotlin кода в одном модуле — конфликты при сборке | Средняя | Держать этапы изолированными; Java и Kotlin файлы одного класса не могут сосуществовать |
| R5 | Firebase sync через REST без SDK — timeout обработка в coroutines | Низкая | Добавить `withTimeout(10_000)` в `FirebaseSync.kt` |

### Open Questions (RESOLVED 2026-05-02)

| # | Вопрос | Решение |
|---|--------|---------|
| Q1 | Нужна ли новая таблица `GameHistory`? | **Да.** Добавить `GameHistory(id, difficulty, time, date)` для bar chart динамики. |
| Q2 | «Достижения» — фича или заглушка? | **Заглушка.** Карточка-stub, реализация позже. |
| Q3 | «Ежедневная задача» — новая фича? | **Заглушка.** Stub, реализация позже. |
| Q4 | Hilt DI? | **Да**, вводить сразу. |
| Q5 | Kotlin/Compose версии? | **Последние стабильные.** `composeCompiler {}` блок (Kotlin 2.0+). |
| Q6 | Landscape? | **Да, но позже.** Отдельная задача после основной миграции. |

### Уточнённый стек (2026-05-02)

- Coroutines, Room, Retrofit2, Compose Navigation 2, Hilt, KotlinX Serialization
- minSdk **29** (повышен с 24), targetSdk/compileSdk 36+
- Последние стабильные Kotlin + Compose BOM

---

## History

| Дата | Автор | Изменение |
|------|-------|-----------|
| 2026-05-02 | system-analytics | Начальная версия ТЗ |

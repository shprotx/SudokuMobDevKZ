# CLAUDE.md

## Project Overview

Android Sudoku game (100% Kotlin, Jetpack Compose) distributed via APK (GitHub Releases). Package: `ru.shprot.sudokumobdevkz`. Features 3 difficulty levels, statistics tracking with Firebase percentiles, dark/light themes, notes, hints, undo, auto-save, and in-app auto-update.

## Build & Run

```bash
./gradlew assembleDebug          # Build debug APK
./gradlew assembleRelease        # Build release APK (needs keystore in local.properties)
./gradlew testDebugUnitTest      # Run unit tests
./gradlew connectedDebugAndroidTest  # Run instrumented tests
./gradlew clean                  # Clean build artifacts
```

- AGP 8.x, Kotlin 2.3.0, compileSdk/targetSdk 36, minSdk 29, Java 17
- Single `app` module, no multi-module setup

## Architecture

Feature-based modular architecture with MVI and Clean Architecture principles.

### Package Structure

```
ru.shprot.sudokumobdevkz/
├── core/                      # Shared core functionality
│   ├── base/                  # Base classes and utilities
│   │   ├── data/             # Data layer: database/ (dao/, entity/), remote/, repository/
│   │   ├── domain/           # Domain: generator/ (solver/), model/
│   │   └── presentation/     # Base ViewModels, UI contracts, navigation, util
│   ├── theme/                # App theming (AppTheme, colors, typography, dimensions)
│   └── uicommon/             # Reusable UI components: button/, etc.
├── di/                       # Dependency injection modules (Hilt)
├── feature/                  # Feature modules (each has presentation/, domain/ optional)
│   ├── game/                 # Game screen (largest module)
│   ├── menu/                 # Main menu
│   ├── statistic/            # Statistics
│   ├── settings/             # Settings + Privacy Policy
│   ├── gameover/             # Game over screen
│   ├── howtoplay/            # How to play tutorial
│   └── splash/               # Splash screen with animation
└── activity/                 # ComposeActivity, navigation setup
```

### MVI Architecture Pattern

The app uses **MVI (Model-View-Intent)** with the following contracts:

- **UIState**: Immutable data class representing the entire screen state
- **UIEvent**: User interactions and intents sent to ViewModel
- **UIEffect**: One-time side effects (navigation, snackbars, etc.)

Each feature contains:
- `presentation/contract/` — UIState, UIEvent, UIEffect definitions (one class per file)
- `presentation/viewmodel/` — ViewModel extending BaseViewModel
- `presentation/screen/` — Screen composable (thin wrapper: state + effects + delegates to ScreenContent)
- `presentation/components/screencontent/` — ScreenContent composable (pure UI)
- `presentation/components/` — Feature-specific UI components
- `presentation/navigation/` — Route definitions
- `domain/model/` (optional) — Domain models specific to this feature

### Screen / ScreenContent Split

Screen — thin wrapper subscribing to state and effects, delegating rendering to ScreenContent:

```kotlin
@Composable
fun FeatureScreen(
    navController: NavController,
    viewModel: FeatureViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                FeatureUIEffect.NavigateBack ->
                    navController.popBackStack()
            }
        }
    }

    FeatureScreenContent(
        uiState = uiState,
        onEvent = viewModel::setEvent,
    )
}
```

ScreenContent — pure Compose without ViewModel:

```kotlin
@Composable
fun FeatureScreenContent(
    uiState: FeatureUIState,
    onEvent: (FeatureUIEvent) -> Unit,
) { ... }
```

**ScreenContent receives ONLY uiState + onEvent. No navigation callbacks, no ViewModel references.**

### handleUIEvent — data objects first, then data classes

```kotlin
override fun handleUIEvent(event: FeatureUIEvent) =
    when (event) {
        FeatureUIEvent.BackClicked ->
            setEffect(FeatureUIEffect.NavigateBack)

        FeatureUIEvent.SomeAction ->
            handleSomeAction()

        is FeatureUIEvent.ItemClicked ->
            handleItemClick(event.id)
    }
```

### BaseViewModel Pattern

All ViewModels extend `BaseViewModel<Event, State, Effect>`:
- `setEvent(event)` — dispatches events
- `setState(state)` — updates state
- `updateState { copy(...) }` — updates state with transform
- `setEffect(effect)` — emits one-time side effects

## Key Technologies

- **Jetpack Compose** + Material3 — UI
- **Hilt** — Dependency injection
- **Navigation Compose** — Type-safe navigation with `@Serializable` routes
- **Coroutines + Flow** — Async
- **Room** — Local database
- **Retrofit + OkHttp** — Firebase REST API
- **KotlinX Serialization** — JSON serialization
- **DataStore Preferences** — Settings storage
- **Google Play In-App Review** — Rating prompts

## Theme System

Custom theming via `AppTheme` object:
- `AppTheme.colors` — Color palette (light/dark)
- `AppTheme.typography` — Text styles
- `AppTheme.paddings` — Spacing/padding values
- `AppTheme.sizes` — Dimension values

**ALWAYS use `AppTheme` for all styling instead of hardcoded values.**

## Room Database

Tables: `StatisticEntity`, `GameHistoryEntity`, `SavedGameEntity`. DB version 2 with `fallbackToDestructiveMigration()`.

## CI/CD

GitHub Actions workflow (`.github/workflows/release.yml`): on PR merge to master → build release APK → create GitHub Release with tag `vX.Y.Z`.

## Code Patterns to Follow

1. **Always extend BaseViewModel** for new ViewModels
2. **Use MVI contracts** — UIState, UIEvent, UIEffect for every screen
3. **Leverage AppTheme** for all styling
4. **Inject dependencies via Hilt** — constructor injection
5. **Handle errors centrally** via BaseViewModel's exceptionHandler
6. **Follow feature module structure** — keep features self-contained
7. **String resources** — ALL user-facing strings MUST be in `strings.xml`, never hardcode
8. **Trailing commas** — ALL function/constructor parameters MUST end with a trailing comma
9. **Colors from theme** — ALWAYS use `AppTheme.colors.*`, NEVER use Android `Color.*` constants
10. **Padding/size from theme** — ALWAYS use `AppTheme.paddings.*` and `AppTheme.sizes.*`, NEVER hardcode dp values
11. **Navigation args** — use `savedStateHandle.toRoute<RouteClass>()` for type-safe args
12. **System bar insets** — `statusBarsPadding()` on top elements, `navigationBarsPadding()` on bottom elements. No Scaffold in individual screens
13. **Dialogs** — managed through UIState flags + UIEvent dismiss, NOT through `rememberSaveable`
14. **Navigation** — only through UIEffect, handled in Screen's LaunchedEffect

## Anti-patterns (FORBIDDEN)

1. **Hardcoded strings in UI** — all strings via `strings.xml` only
2. **Multiple classes/composables in one file** — one file = one public class/composable. Exception: sealed interface with children
3. **`private` composable functions** — use `internal` instead
4. **Comments in code** — forbidden. Code must be self-documenting
5. **Empty line at end of file** — always delete trailing newline
6. **Hardcoded padding/size values** — only `AppTheme.paddings.*` and `AppTheme.sizes.*`
7. **Mutable collections in UIState** — state is immutable, use `copy()` with immutable collections
8. **Inline type references** — always use proper imports, never `icon: androidx.compose.ui.graphics.vector.ImageVector`
9. **Scaffold in ScreenContent** — no Scaffold in individual screens, use Box/Column + `.background()`
10. **Navigation callbacks in ScreenContent** — ScreenContent receives ONLY `(uiState, onEvent)`, navigation through UIEffect
11. **`model/` directory** — does not exist. Use `core/base/data/`, `core/base/domain/`, or `feature/*/domain/model/`
12. **Data models in `contract/`** — domain models belong in `domain/model/`, not in presentation contracts

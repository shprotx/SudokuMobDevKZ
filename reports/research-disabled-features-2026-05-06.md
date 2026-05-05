# Research: Закрытые и отключённые фичи
Профиль: **Research**

## Summary

Найдено 5 реальных закрытых/отключённых фич: Daily Challenge (готовый UI-компонент не подключён), Achievements (закомментирован в меню), Sounds (бэкенд готов, UI отсутствует), In-App Review (зависимость есть, код не написан), Remove Ads / Support Developer / Rate App (строки в ресурсах, реализации нет).

## Detailed Findings

### 1. Daily Challenge — готовый компонент, не подключён к экрану
**Location**: `app/src/main/java/ru/shprot/sudokumobdevkz/feature/menu/presentation/components/DailyChallengeCard.kt`
**Строки**: `strings.xml:105-107` (`daily_challenge`, `daily_challenge_desc`, `progress`)
**Что есть**: Полностью готовый `DailyChallengeCard` composable — карточка с иконкой огня, заголовком, описанием, счётчиком "0/3" и стрелкой.
**Что отсутствует**: Компонент НЕ используется ни в одном экране. Нет бэкенд-логики (генерация daily puzzle, хранение прогресса, серверная синхронизация).
**Оценка**: UI-прототип, нужна полная backend-реализация.

### 2. Achievements — закомментирован в меню
**Location**: `app/src/main/java/ru/shprot/sudokumobdevkz/feature/menu/presentation/components/MenuNavigationCards.kt:40-46`
**Строки**: `strings.xml:113-114` (`achievements`, `achievements_desc`)
**Что есть**: Закомментированный `MenuNavCard` с иконкой звезды и TODO-комментарием. Строковые ресурсы определены.
**Что отсутствует**: Нет экрана, ViewModel, навигации, модели данных. Только заготовка карточки в меню.
**Оценка**: Нужна полная реализация фичи с нуля.

### 3. Sounds — бэкенд готов, UI скрыт
**Location**:
- Модель: `core/base/domain/model/AppSettings.kt:13` (`soundsEnabled: Boolean = true`)
- Event: `feature/settings/presentation/contract/SettingsUIEvent.kt:21`
- ViewModel: `feature/settings/presentation/viewmodel/SettingsViewModel.kt:85-86`
- Repository: `core/base/data/repository/SettingsRepository.kt:51,67`

**Что есть**: Полный data-flow — поле в модели, event `ToggleSounds`, обработка в ViewModel, сохранение в DataStore.
**Что отсутствует**: Нет UI-тоггла в экране настроек. Нет самих звуков (аудио-файлов, воспроизведения).
**Оценка**: Достаточно добавить тоггл в UI + реализовать SoundPlayer.

### 4. In-App Review — зависимость без кода
**Location**: `app/build.gradle:126` (`com.google.android.play:review:2.0.2`)
**Строки**: `strings.xml:60` (`rate_app`), плюс старые строки rate-диалога
**Что есть**: Gradle-зависимость подключена.
**Что отсутствует**: Ни одного вызова Review API в Kotlin-коде. Нет логики "когда показать", нет ReviewManager.
**Оценка**: Нужна реализация: триггер (после N игр?), ReviewManager flow.

### 5. Строковые заглушки нереализованных фич
**Location**: `app/src/main/res/values/strings.xml`

| Строка | Предполагаемая фича |
|--------|-------------------|
| `remove_ads` (line 61) | Покупка отключения рекламы — рекламы в приложении нет |
| `support_the_developer` (line 58) | Донат/поддержка разработчика — нет реализации |
| `update_available_title`, `update_button`, `update_later`, `update_downloading`, `update_error`, `update_play_protect_hint` (lines 93-98) | In-app update — строки есть, реализация неизвестна |
| `app_language` | Выбор языка — нет UI |

## Observations

1. **Daily Challenge** и **Achievements** — две самые крупные нереализованные фичи, которые могут значительно увеличить retention.
2. **Sounds** — самая "дешёвая" фича для включения: весь pipeline готов, осталось UI + аудио.
3. **In-App Review** — низкий effort, высокий impact на рейтинг в сторе.
4. **Remove Ads / Support Developer** — предполагают монетизацию, которой пока нет.
5. В `strings.xml` накопилось ~50+ неиспользуемых строк от старых/отменённых фич — стоит почистить.
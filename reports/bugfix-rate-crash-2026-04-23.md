# Bug Fix: Краш после нажатия "Позже" в диалоге оценки
Профиль: **Bug Fix**

## Bug Analysis
**Symptom**: Приложение крашится при запуске на Android 16 после того, как пользователь нажал "Позже" в диалоге оценки. На Android 10 проблема не воспроизводится.

**Root cause**: Несовпадение типов в SharedPreferences для ключа `showInterval` (`RATE_PREFS_INTERVAL`):
- `AppRater.app_launched()` (строка 38): читает как `getInt()`
- `AppRater.increaseInterval()` (строка 64): читает как `getLong()` и записывает как `putLong()`

После нажатия "Позже" → `increaseInterval()` сохраняет интервал как `Long`. При следующем запуске → `app_launched()` вызывает `getInt()` на значение типа `Long` → `ClassCastException`.

На старых версиях Android реализация SharedPreferences могла молча обрабатывать несовпадение типов, на новых — строгая проверка и краш.

**Affected flow**: MainActivity.onCreate() → AppRater.app_launched() → SharedPreferences.getInt() → ClassCastException

## Investigation
1. Проверена цепочка вызовов при запуске: MainActivity → AppRater → SharedPreferences
2. Найдено несовпадение типов: `getInt` vs `getLong`/`putLong` для одного ключа
3. Подтверждено, что `noRateClicked()` в MainFragment вызывает `increaseInterval()`, который записывает Long

## Fix Applied
**Files changed**:
- `AppRater.java` — `increaseInterval()`: заменены `getLong`/`putLong` на `getInt`/`putInt`
- `AppRater.java` — `app_launched()` и `increaseInterval()`: добавлен try-catch для ClassCastException с миграцией Long → Int для существующих пользователей

**Diff summary**: Все операции с `RATE_PREFS_INTERVAL` теперь используют единый тип `int`. Для пользователей, у которых уже сохранён Long, добавлена автоматическая миграция через catch ClassCastException.

## Verification
**Compilation**: PASS
**Tests**: Unit-тесты для этого модуля отсутствуют
**Similar patterns checked**: Проверены все остальные SharedPreferences операции в AppRater — остальные ключи используют консистентные типы

## Follow-up
- Неиспользуемые импорты в AppRater.java (review-related: OnCompleteListener, Task, ReviewInfo, ReviewManager, ReviewManagerFactory, NonNull) — не удалены, чтобы не выходить за рамки фикса

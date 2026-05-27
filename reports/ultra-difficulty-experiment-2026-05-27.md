# Ultra Difficulty Experiment — Findings

**Date:** 2026-05-27
**Branch:** `feature/72-ultra-difficulty`
**Issue:** #72
**Status:** EXPERIMENT FAILED — НЕ мерджится в develop. Ветка/коммит сохраняются как документация.

## Что было сделано

1. Добавлен `Difficulty.ULTRA` (firebaseKey=4, dotCount=4, 💎, purple #8B5CF6).
2. `Difficulty.visibleCells: Int → IntRange` для всех уровней.
3. UI:
   - Главное меню: 3 карточки в Row → **2×2 grid** (Easy/Middle сверху, Expert/Ultra снизу).
   - Statistic экран: **4 таба без скролла** (Easy/Middle/Expert/Ultra).
   - Цвет точек: новая ветка для ULTRA в `dotColor()`.
4. `RatingCalculator`: `base(ULTRA) = 1000`, `targetTimeSeconds(ULTRA) = 2400`.
5. `DailyChallengeRepository`: исключение ULTRA из `difficultyForDate`.
6. Локализация: en/ru/kk/fr/es (`difficulty_ultra`, `for_hardcore`).
7. Тесты:
   - `UltraGeneratorTest` — distribution + timing diagnostic + unique-solution check.
   - `DailyChallengeRepositoryDifficultyTest` — ULTRA не выбирается на 365 дней.
   - `RatingCalculatorTest` — extended для ULTRA branch.
8. Generator: попытка aggressive multi-attempt dig (100 random restarts для ULTRA).

## Почему провалилось

**Целевой диапазон ULTRA = 17..20 visible cells (договорённость 2026-05-27). Реальность — 21..22.**

### Замер на 20 seeds (с aggressive dig 100 restarts)
```
Distribution: {21=4, 22=16}
Timings:      min=3062ms, max=3247ms, avg=3112ms
20/20 seeds  → visibleCount outside target 17..20
```

### Лимит алгоритма

`SudokuGenerator.digForExpert` — greedy one-pass через DancingLinks: удаляет cells в random order пока сохраняется уникальное решение. Попадает в **локальный минимум ~21-22 cells**. Random restart с разными shuffles понижает min с 22 до 21 occasionally, но **17 теоретически и практически недостижимо**.

**Известный факт:** 17-cell Sudoku puzzles очень редкие — всего ~50000 known в мире, найдены exhaustive computer search за годы (Royle 2012). Random-restart greedy физически не может их сгенерировать. Нужен либо deep backtracking search (hours-days per puzzle), либо hardcoded dataset (text-файл с готовыми 17-cell пазлами + random transforms).

### Бонус-эффект
HARD теоретически мог бы стать 21..24 (договорённый range), но реально digForExpert single-pass даёт 22-27, average ≈24. С aggressive dig (30 restarts для HARD) опускается до 21..22 — **становится визуально неотличим от ULTRA**. Без агрессии HARD остаётся 23..26.

## Возможные дальнейшие пути

| Подход | Min visible | Реалистичность | Effort |
|--------|-------------|-----------------|--------|
| Текущий single-pass | 22 | actual | done |
| Aggressive 100-restart | 21 | actual | done (in this commit) |
| Aggressive 1000-restart | 21 (вряд ли 20) | unlikely 20 | trivial — bump const |
| Deep backtracking search | 17-19 теоретически | hours per puzzle | XL |
| **Hardcoded 17-cell dataset** | **17** | YES | M — load asset, parser, random transforms (rotate/swap bands/digits) |

## Рекомендация

Закрыть #72 как не реализуемое в текущем виде. Когда (если) понадобится true ULTRA — открыть новую задачу: **dataset-based generator (load asset 1MB pre-computed 17-cell collection + transforms)**. Это другой подход и другой scope.

## Файлы в ветке (для справки)

См. `git log` ветки `feature/72-ultra-difficulty`. Все code-изменения консистентны и проходят build/tests. Не мерджатся в develop потому что **не достигается целевой эффект** — ULTRA по сложности неотличима от HARD, переусложнение архитектуры (IntRange, 2×2 grid, 4 tabs) без видимой пользы для юзера.

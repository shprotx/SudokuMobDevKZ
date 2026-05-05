# Research: Expert difficulty — biased cell distribution pattern
Профиль: **Research**

## Summary

Метод `digForExpert` обходит ячейки в детерминированном порядке (row-by-row) с 4 зеркальными вариантами. Ячейки, посещённые первыми, почти гарантированно удаляются, а последние — остаются. Это создаёт систематический перекос: одна зона поля всегда получает больше открытых цифр. Решение: полная рандомизация порядка обхода.

## Detailed Findings

### Текущий алгоритм: `digForExpert`
**Location**: `Generator.java:83-105`

Алгоритм работает так:
1. Цикл `for (i = 0; i < 81; i++)` — **линейный обход**
2. `calcX(i, strategy)` / `calcY(i, strategy)` преобразуют линейный индекс в координаты с зеркалированием:
   - strategy 0: (i%9, i/9) — нормальный порядок
   - strategy 1: (8-i%9, 8-i/9) — полная инверсия
   - strategy 2: (i%9, 8-i/9) — вертикальное отражение
   - strategy 3: (8-i%9, i/9) — горизонтальное отражение
3. `i % 4 == 0` — каждая 4-я ячейка дополнительно зеркалится в (8-x, 8-y)
4. Для каждой ячейки: убрать значение → проверить DancingLinks → если решение неуникально, вернуть значение

### Почему возникает bias

Когда сетка **полная** (начало обхода), удаление почти любой ячейки сохраняет уникальность решения — окружающих ограничений достаточно. Когда сетка **разрежена** (конец обхода), удаление создаёт неоднозначность — ячейка остаётся видимой.

Результат:
- Зона, с которой начинается обход → **мало открытых цифр** (почти все удалены)
- Зона, где обход заканчивается → **много открытых цифр** (удаление невозможно)

4 стратегии — это лишь 4 ротации одного паттерна. Игрок замечает: "всегда проще начинать с определённого угла/стороны".

### `calcX` / `calcY`
**Location**: `Generator.java:156-172`

Эти методы реализуют зеркалирование, но не рандомизацию. Все 4 варианта — детерминированные.

### Константы
**Location**: `Library.java:31`

`VISIBLE_SQUARES_EXPERT = 27` — для expert уровня остаётся 27 видимых ячеек из 81.

## Flow

```
generate(diff=3)
  → initBasicThings()      — подготовка массивов
  → generateGrid()         — заполнение полной сетки (backtracking)
  → parseGridArray()       — копирование в grid[][]
  → dig(diff=3)
      → strategy = random(0..3)            — 4 варианта обхода
      → digForExpert(strategy, 3, zeros)   — линейный обход с зеркалированием
          → for i=0..80:
              → calcX/calcY(i, strategy)   — детерминированные координаты
              → DancingLinks.tryToSolve()  — проверка уникальности
      → (diff=3, значит openSomeCells не вызывается)
  → makeSquaresInvisible() — проставление visible=false по grid[][]
```

## Предложение по исправлению

Заменить линейный обход на **случайно перемешанный список ячеек**:

```java
private int digForExpert(int strategy, int diff, ArrayList<Integer> zeros) {
    // Создаём список всех позиций и перемешиваем
    ArrayList<int[]> positions = new ArrayList<>(NUMBER_OF_CELLS);
    for (int row = 0; row < SUBLIST_SIZE; row++)
        for (int col = 0; col < SUBLIST_SIZE; col++)
            positions.add(new int[]{row, col});
    Collections.shuffle(positions);

    int emptyCellsCounter = 0;
    for (int i = 0; i < positions.size(); i++) {
        int y = positions.get(i)[0];
        int x = positions.get(i)[1];
        int temp = grid[y][x];
        grid[y][x] = 0;
        DancingLinks.solutionsCount = 0;
        DancingLinksAlgorithm.tryToSolve(grid);
        isUnicSolution = DancingLinks.solutionsCount == 1;
        if (!isUnicSolution)
            grid[y][x] = temp;
        else {
            emptyCellsCounter++;
            if (diff < 3) zeros.add(y * SUBLIST_SIZE + x);
        }
    }
    return emptyCellsCounter;
}
```

Это полностью убирает:
- `strategy` и все 4 зеркальных варианта
- `calcX` / `calcY`
- `i % 4 == 0` трюк

Каждая генерация будет давать **непредсказуемое распределение** открытых цифр по всему полю.

## Observations

- `calcX`/`calcY` можно удалить, если `openSomeCells` тоже переписать (он использует линейный индекс i)
- Для `openSomeCells` (easy/medium) bias менее критичен, т.к. ячейки добавляются обратно из перемешанного `zeros`, но `zeros` заполняется в том же biased порядке
- Генерация полной сетки (`generateGrid`) тоже идёт сверху→вниз, но это не влияет на паттерн — полная сетка всегда валидна
- `DancingLinks.solutionsCount` — статическое поле, потенциальная проблема при многопоточности (не критично сейчас, но стоит помнить)

# GameViewModel Test Cases

## Стандартный/упрощённый режим

| # | Сценарий | Ожидание |
|---|----------|----------|
| 1 | Стандартный режим, победа | updateStatistic(isWin=true), deleteSavedGame |
| 2 | Стандартный режим, поражение (3 ошибки) | updateStatistic(isWin=false), deleteSavedGame |
| 3 | unlimitedHints=true → победа | incrementCasualGames, НЕ updateStatistic |
| 4 | unlimitedErrors=true → старт | isStandardMode=false, maxErrors=MAX_VALUE |
| 5 | checkErrors=false → старт | isStandardMode=false |

## Проверка ошибок

| # | Сценарий | Ожидание |
|---|----------|----------|
| 6 | checkErrors=false, неверная цифра | value=wrong, isError=false, errors=0 |
| 7 | checkErrors=true, неверная цифра | isError=true, errors++ |

## Защита ячеек

| # | Сценарий | Ожидание |
|---|----------|----------|
| 8 | Попытка перезаписать given-ячейку | Значение не меняется |
| 9 | Попытка перезаписать верно угаданную ячейку | Значение не меняется |
| 10 | Попытка стереть given-ячейку | Ничего не происходит |
| 11 | Попытка стереть верно угаданную ячейку | Ничего не происходит |

## Board completion

| # | Сценарий | Ожидание |
|---|----------|----------|
| 12 | Все ячейки заполнены верно | isWin=true, NavigateToGameOver |
| 13 | Все ячейки заполнены, есть неверные (checkErrors=false) | isWin=false |

## Сохранение / брошенные игры

| # | Сценарий | Ожидание |
|---|----------|----------|
| 14 | Начал стандартную → закрыл → новая игра | Старая = поражение (updateStatistic isWin=false) |
| 15 | Начал casual → закрыл → новая игра | incrementCasualGames |
| 16 | Продолжение: restoreGame | Восстановлен difficulty, time, errors, hints, cells |
| 17 | gameOver → saved game deleted | deleteSavedGame вызван |
| 18 | ON_STOP → saveGameStateSync | Состояние записано в Room |

## Навигация

| # | Сценарий | Ожидание |
|---|----------|----------|
| 19 | Победа → effect | NavigateToGameOver(isWin=true) |
| 20 | Поражение → effect | NavigateToGameOver(isWin=false) |
| 21 | Кнопка "Назад" | NavigateBack effect |
| 22 | "Начать новую" из диалога | NavigateToNewGame(difficulty) effect |

## Настройки

| # | Сценарий | Ожидание |
|---|----------|----------|
| 23 | difficulty из route | Difficulty.HARD при ordinal=2 |
| 24 | Выбранная сложность запоминается | DataStore обновлён |
| 25 | Активная стандартная партия → настройки | Чувствительные переключатели заблокированы |

## Подсветка

| # | Сценарий | Ожидание |
|---|----------|----------|
| 26 | Тап на ячейку с цифрой 5 | highlightedNumber=5 |
| 27 | Ввод верной цифры 7 | highlightedNumber=7 |
| 28 | Ввод неверной цифры | highlightedNumber=0 |
| 29 | Deselect | highlightedNumber=0, selectedRow=-1 |

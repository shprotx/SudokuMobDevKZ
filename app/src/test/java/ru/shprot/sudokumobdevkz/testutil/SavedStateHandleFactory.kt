package ru.shprot.sudokumobdevkz.testutil

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes

fun createGameSavedStateHandle(
    difficultyOrdinal: Int = 0,
    continueGame: Boolean = false,
): SavedStateHandle = SavedStateHandle.invoke(
    GameRoutes.GameScreen(
        difficultyOrdinal = difficultyOrdinal,
        continueGame = continueGame,
    ),
)

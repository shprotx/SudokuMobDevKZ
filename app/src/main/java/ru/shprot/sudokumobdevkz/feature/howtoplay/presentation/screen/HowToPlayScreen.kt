package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.screencontent.HowToPlayScreenContent

@Composable
fun HowToPlayScreen(navController: NavController) {
    HowToPlayScreenContent(onNavigateBack = { navController.popBackStack() })
}

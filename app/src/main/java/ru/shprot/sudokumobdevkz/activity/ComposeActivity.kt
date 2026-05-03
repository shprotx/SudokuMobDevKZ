package ru.shprot.sudokumobdevkz.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import ru.shprot.sudokumobdevkz.activity.navigation.SudokuNavHost
import ru.shprot.sudokumobdevkz.core.theme.SudokuTheme
import ru.shprot.sudokumobdevkz.model.repository.SettingsRepository
import javax.inject.Inject

@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settings by settingsRepository.settings.collectAsStateWithLifecycle(
                initialValue = settingsRepository.currentSettings,
            )

            SudokuTheme(darkTheme = settings.isDarkTheme) {
                SudokuNavHost()
            }
        }
    }
}

package ru.shprot.sudokumobdevkz.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import ru.shprot.sudokumobdevkz.activity.navigation.SudokuNavHost
import ru.shprot.sudokumobdevkz.core.theme.SudokuTheme

@AndroidEntryPoint
class ComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SudokuTheme {
                SudokuNavHost()
            }
        }
    }
}

package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.TipCard
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.TutorialStepCard

@Composable
fun HowToPlayScreen(onNavigateBack: () -> Unit) {
    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            // Toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.paddings.small,
                        vertical = AppTheme.paddings.medium,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = AppTheme.colors.text,
                    )
                }

                Text(
                    modifier = Modifier.weight(1f),
                    text = "Как играть",
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.text,
                    textAlign = TextAlign.Center,
                )

                // Placeholder for symmetry
                IconButton(onClick = { }, enabled = false) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = AppTheme.colors.background,
                        modifier = Modifier.size(AppTheme.sizes.iconMedium),
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {

                // Header
                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    text = "Освой судоку за 3 шага",
                    style = AppTheme.typography.h2,
                    color = AppTheme.colors.text,
                )

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.small),
                    text = "Простые правила — бесконечные комбинации",
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.textSecondary,
                )

                // Step 1
                TutorialStepCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                    stepNumber = 1,
                    title = "Никаких повторов",
                    description = "Заполните каждую ячейку цифрой от 1 до 9.\n\n" +
                            "Одна и та же цифра не может встречаться дважды " +
                            "в строке, столбце или блоке 3×3.",
                    imageRes = R.drawable.howone,
                )

                // Step 2
                TutorialStepCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    stepNumber = 2,
                    title = "Решение всегда уникально",
                    description = "У каждой головоломки ровно одно решение. " +
                            "Нажмите на пустую ячейку и выберите цифру под полем.\n\n" +
                            "Ищите ячейки, куда подходит только одна цифра.",
                    imageRes = R.drawable.howtwo,
                )

                // Step 3
                TutorialStepCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    stepNumber = 3,
                    title = "Используй заметки",
                    description = "Используйте заметки, чтобы отмечать возможные варианты " +
                            "в ячейке. Включите режим заметок кнопкой под полем.\n\n" +
                            "Заметки исчезают автоматически, когда вы вписываете итоговую цифру.",
                    imageRes = R.drawable.howthree,
                )

                // Tips section
                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.xxxl),
                    text = "Полезные советы",
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.text,
                )

                TipCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    icon = Icons.Filled.Lightbulb,
                    title = "Начинай с простого",
                    description = "Сначала ищи строки и столбцы, где не хватает 1–2 цифр. Это самые лёгкие ячейки.",
                )

                TipCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    icon = Icons.Filled.AutoFixHigh,
                    title = "Метод исключения",
                    description = "Если цифра уже есть в строке и столбце — исключи её из вариантов для пересечения.",
                )

                TipCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    icon = Icons.Filled.Speed,
                    title = "Не гадай",
                    description = "В судоку всегда можно вывести ответ логически. Если приходится гадать — ищи другую ячейку.",
                )

                TipCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    icon = Icons.Filled.Timer,
                    title = "Тренируй скорость",
                    description = "С опытом ты будешь решать головоломки быстрее. Следи за временем в статистике.",
                )

                // CTA
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = AppTheme.paddings.xxxl,
                            bottom = AppTheme.paddings.xxxl,
                        ),
                    onClick = onNavigateBack,
                    shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
                    colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
                ) {
                    Text(
                        text = "Понятно, играть!",
                        style = AppTheme.typography.button,
                        fontWeight = FontWeight.SemiBold,
                        color = AppTheme.colors.textOnPrimary,
                    )
                }
            }
        }
    }
}

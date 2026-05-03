package ru.shprot.sudokumobdevkz.feature.settings.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun PrivacyPolicyScreen(onNavigateBack: () -> Unit) {
    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
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
                    text = "Политика конфиденциальности",
                    style = AppTheme.typography.h4,
                    color = AppTheme.colors.text,
                    textAlign = TextAlign.Center,
                )

                IconButton(onClick = { }, enabled = false) {}
            }

            Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {
                Section("Сбор данных", SECTION_DATA_COLLECTION)

                Section("Использование данных", SECTION_DATA_USAGE)

                Section("Хранение данных", SECTION_DATA_STORAGE)

                Section("Права пользователя", SECTION_USER_RIGHTS)

                Section("Изменения политики", SECTION_CHANGES)

                Text(
                    modifier = Modifier.padding(
                        top = AppTheme.paddings.xxl,
                        bottom = AppTheme.paddings.xxxl,
                    ),
                    text = "Последнее обновление: май 2026",
                    style = AppTheme.typography.caption1,
                    color = AppTheme.colors.textSecondary,
                )
            }
        }
    }
}

@Composable
private fun Section(title: String, body: String) {
    Text(
        modifier = Modifier.padding(top = AppTheme.paddings.xxl),
        text = title,
        style = AppTheme.typography.h4,
        color = AppTheme.colors.text,
    )

    Text(
        modifier = Modifier.padding(top = AppTheme.paddings.medium),
        text = body,
        style = AppTheme.typography.body3,
        color = AppTheme.colors.textSecondary,
    )
}

private const val SECTION_DATA_COLLECTION =
    "Приложение собирает минимальный набор данных, необходимый для работы игровой статистики: " +
            "время прохождения, количество ошибок и результаты игр. " +
            "Данные привязаны к анонимному идентификатору устройства и не содержат персональной информации."

private const val SECTION_DATA_USAGE =
    "Собранные данные используются исключительно для:\n" +
            "• Отображения вашей игровой статистики\n" +
            "• Расчёта вашего процентиля среди других игроков\n" +
            "Данные не передаются третьим лицам и не используются в рекламных целях."

private const val SECTION_DATA_STORAGE =
    "Игровая статистика хранится локально на устройстве и синхронизируется с Firebase Realtime Database " +
            "для обеспечения функции сравнения с другими игроками. " +
            "Вы можете удалить свои данные в любой момент через раздел «Настройки» → «Сбросить статистику»."

private const val SECTION_USER_RIGHTS =
    "Вы имеете право:\n" +
            "• Удалить все свои данные через настройки приложения\n" +
            "• Отключить синхронизацию статистики\n" +
            "• Запросить удаление данных с сервера, связавшись с разработчиком"

private const val SECTION_CHANGES =
    "Мы можем обновлять данную политику. Актуальная версия всегда доступна в приложении."

package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText

@Composable
internal fun SaveThemeDialog(
    initialName: String,
    showNameError: Boolean,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by rememberSaveable(initialName) { mutableStateOf(initialName) }
    val hasError = showNameError && name.isBlank()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusXL),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.paddings.xxl),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(AppTheme.sizes.buttonHeight)
                        .clip(CircleShape)
                        .background(AppTheme.colors.primaryLight),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(AppTheme.sizes.iconLarge),
                        imageVector = Icons.Filled.Palette,
                        contentDescription = null,
                        tint = AppTheme.colors.primary,
                    )
                }

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    text = stringResource(R.string.theme_builder_save_dialog_title),
                    style = AppTheme.typography.h2,
                    color = AppTheme.colors.text,
                )

                BasicTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.extraLarge)
                        .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
                        .background(AppTheme.colors.background)
                        .border(
                            width = AppTheme.sizes.dividerThickness,
                            color = if (hasError) AppTheme.colors.error else AppTheme.colors.divider,
                            shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium),
                        )
                        .padding(AppTheme.paddings.large),
                    value = name,
                    onValueChange = { if (it.length <= MAX_NAME_LENGTH) name = it },
                    textStyle = AppTheme.typography.body1.copy(color = AppTheme.colors.text),
                    cursorBrush = SolidColor(AppTheme.colors.primary),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (name.isEmpty()) {
                            Text(
                                text = stringResource(R.string.theme_builder_name_hint),
                                style = AppTheme.typography.body1,
                                color = AppTheme.colors.textSecondary,
                            )
                        }
                        innerTextField()
                    },
                )

                if (hasError) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.paddings.small),
                        text = stringResource(R.string.theme_builder_name_empty_error),
                        style = AppTheme.typography.body3,
                        color = AppTheme.colors.error,
                    )
                }

                ButtonDefault(
                    modifier = Modifier.padding(top = AppTheme.paddings.xxl),
                    text = stringResource(R.string.theme_builder_save),
                    onClick = { onConfirm(name) },
                )

                ButtonText(
                    modifier = Modifier.padding(top = AppTheme.paddings.medium),
                    text = stringResource(R.string.cancel),
                    onClick = onDismiss,
                )
            }
        }
    }
}

private const val MAX_NAME_LENGTH = 32
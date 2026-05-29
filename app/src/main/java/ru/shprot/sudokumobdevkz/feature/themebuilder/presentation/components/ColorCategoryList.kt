package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.viewmodel.ThemeBuilderViewModel

@Composable
internal fun ColorCategoryList(
    colors: ThemeColors,
    onColorKeyClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundCard, RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge))
            .padding(horizontal = AppTheme.paddings.large, vertical = AppTheme.paddings.medium),
    ) {
        ThemeBuilderViewModel.colorKeys.forEach { (key, labelRes) ->
            ColorRow(
                label = stringResource(labelRes),
                colorValue = getColorByKeyInternal(colors, key),
                onClick = { onColorKeyClick(key) },
            )
        }
    }
}

@Composable
internal fun ColorRow(
    label: String,
    colorValue: Long,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.paddings.default),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = AppTheme.typography.body1,
            color = AppTheme.colors.text,
        )

        Box(
            modifier = Modifier
                .size(AppTheme.sizes.iconMedium)
                .background(Color(colorValue), CircleShape),
            content = {},
        )
    }
}

internal fun getColorByKeyInternal(colors: ThemeColors, key: String): Long = when (key) {
    "primary" -> colors.primary
    "primaryDark" -> colors.primaryDark
    "primaryLight" -> colors.primaryLight
    "secondary" -> colors.secondary
    "background" -> colors.background
    "backgroundCard" -> colors.backgroundCard
    "backgroundCardAccent" -> colors.backgroundCardAccent
    "surface" -> colors.surface
    "text" -> colors.text
    "textSecondary" -> colors.textSecondary
    "textOnPrimary" -> colors.textOnPrimary
    "textAccent" -> colors.textAccent
    "error" -> colors.error
    "errorLight" -> colors.errorLight
    "success" -> colors.success
    "warning" -> colors.warning
    "gridLine" -> colors.gridLine
    "gridLineBold" -> colors.gridLineBold
    "cellSelected" -> colors.cellSelected
    "cellHighlight" -> colors.cellHighlight
    "cellSameNumber" -> colors.cellSameNumber
    "cellError" -> colors.cellError
    "cellFixed" -> colors.cellFixed
    "cellEditable" -> colors.cellEditable
    "draftText" -> colors.draftText
    "divider" -> colors.divider
    "iconTint" -> colors.iconTint
    "bottomNavSelected" -> colors.bottomNavSelected
    "bottomNavUnselected" -> colors.bottomNavUnselected
    "chipSelected" -> colors.chipSelected
    "chipUnselected" -> colors.chipUnselected
    "chipTextSelected" -> colors.chipTextSelected
    "chipTextUnselected" -> colors.chipTextUnselected
    "progressTrack" -> colors.progressTrack
    "progressIndicator" -> colors.progressIndicator
    "barChart" -> colors.barChart
    "barChartLabel" -> colors.barChartLabel
    else -> 0xFFF8F9FAL
}
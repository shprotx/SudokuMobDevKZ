package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun TutorialStepCardHorizontal(
    modifier: Modifier,
    stepNumber: Int,
    title: String,
    description: String,
    @DrawableRes imageRes: Int? = null,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        elevation = CardDefaults.cardElevation(defaultElevation = AppTheme.sizes.elevationSmall),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
        ) {
            if (imageRes != null) {
                val imageShape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium)

                Image(
                    modifier = Modifier
                        .width(180.dp)
                        .clip(imageShape)
                        .border(1.dp, AppTheme.colors.divider, imageShape),
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    contentScale = ContentScale.FillWidth,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = AppTheme.paddings.large),
            ) {
                Text(
                    text = stringResource(R.string.step_n, stepNumber),
                    style = AppTheme.typography.caption1,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.primary,
                )

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.small),
                    text = title,
                    style = AppTheme.typography.h4,
                    color = AppTheme.colors.text,
                )

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    text = description,
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.textSecondary,
                )
            }
        }
    }
}

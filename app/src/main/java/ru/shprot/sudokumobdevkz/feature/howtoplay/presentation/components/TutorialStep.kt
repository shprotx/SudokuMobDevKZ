package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun TutorialStepCard(
    modifier: Modifier = Modifier,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
        ) {
            Text(
                text = "Шаг $stepNumber",
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

            if (imageRes != null) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.default)
                        .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium)),
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    contentScale = ContentScale.FillWidth,
                )
            }

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.default),
                text = description,
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )
        }
    }
}

package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardData
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText

@Composable
internal fun LeaderboardPreviewCard(
    modifier: Modifier,
    isSignedIn: Boolean,
    isLoading: Boolean,
    data: LeaderboardData?,
    onOpenFull: () -> Unit,
    onSignInCta: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Icon(
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                    imageVector = Icons.Filled.EmojiEvents,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                )

                Text(
                    modifier = Modifier.padding(start = AppTheme.paddings.medium),
                    text = stringResource(R.string.leaderboard_preview_title),
                    style = AppTheme.typography.body1,
                    color = AppTheme.colors.text,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            when {
                !isSignedIn ->
                    SignInCtaBlock(onSignInCta = onSignInCta)

                isLoading && data == null ->
                    LoadingBlock()

                data == null || data.topRows.isEmpty() ->
                    EmptyBlock()

                else ->
                    LeaderboardBlock(data = data, onOpenFull = onOpenFull)
            }
        }
    }
}

@Composable
private fun SignInCtaBlock(onSignInCta: () -> Unit) {

    Column(modifier = Modifier.padding(top = AppTheme.paddings.default)) {

        Text(
            text = stringResource(R.string.leaderboard_sign_in_cta),
            style = AppTheme.typography.body3,
            color = AppTheme.colors.textSecondary,
        )

        ButtonText(
            modifier = Modifier.padding(top = AppTheme.paddings.small),
            text = stringResource(R.string.leaderboard_open_settings),
            textColor = AppTheme.colors.primary,
            onClick = onSignInCta,
        )
    }
}

@Composable
private fun LoadingBlock() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.paddings.large),
        contentAlignment = Alignment.Center,
    ) {

        CircularProgressIndicator(
            modifier = Modifier.size(AppTheme.sizes.iconMedium),
            color = AppTheme.colors.primary,
            strokeWidth = AppTheme.sizes.dividerThickness * 2,
        )
    }
}

@Composable
private fun EmptyBlock() {

    Text(
        modifier = Modifier.padding(top = AppTheme.paddings.default),
        text = stringResource(R.string.leaderboard_empty),
        style = AppTheme.typography.body3,
        color = AppTheme.colors.textSecondary,
    )
}

@Composable
private fun LeaderboardBlock(
    data: LeaderboardData,
    onOpenFull: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppTheme.paddings.default),
        verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
    ) {

        data.topRows.take(3).forEach { row ->
            LeaderboardPreviewRow(
                rank = row.rank,
                name = row.displayName,
                score = row.displayScore,
                isCurrentPlayer = row.isCurrentPlayer,
            )
        }

        val playerRank = data.playerScore?.rank
        if (playerRank != null && playerRank > 3) {
            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.small),
                text = stringResource(
                    R.string.leaderboard_your_place,
                    playerRank,
                    data.playerScore.displayScore,
                ),
                style = AppTheme.typography.body3,
                color = AppTheme.colors.primary,
            )
        }

        ButtonText(
            modifier = Modifier.padding(top = AppTheme.paddings.small),
            text = stringResource(R.string.leaderboard_open_full),
            textColor = AppTheme.colors.primary,
            onClick = onOpenFull,
        )
    }
}

@Composable
private fun LeaderboardPreviewRow(
    rank: Long,
    name: String,
    score: String,
    isCurrentPlayer: Boolean,
) {

    val nameColor =
        if (isCurrentPlayer) AppTheme.colors.primary
        else AppTheme.colors.text

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Text(
            modifier = Modifier.padding(end = AppTheme.paddings.default),
            text = "#$rank",
            style = AppTheme.typography.body3,
            color = AppTheme.colors.textSecondary,
        )

        Text(
            modifier = Modifier.weight(1f),
            text = name,
            style = AppTheme.typography.body2,
            color = nameColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = if (isCurrentPlayer) FontWeight.SemiBold else FontWeight.Normal,
        )

        Text(
            text = score,
            style = AppTheme.typography.body2,
            color = AppTheme.colors.text,
            fontWeight = FontWeight.Medium,
        )
    }
}

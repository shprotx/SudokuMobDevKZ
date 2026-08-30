package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardMappers.ownRowOutsideTop
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardMappers.toPlayerScore
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardMappers.toRow
import ru.shprot.sudokumobdevkz.core.base.data.remote.LeaderboardEntryDto

class LeaderboardMappersTest {

    @Test
    fun ownRowOutsideTop_playerOutsideTopWithRank_returnsCurrentPlayerRow() {
        val data = LeaderboardData(
            topRows = listOf(
                LeaderboardRow(
                    rank = 1,
                    displayName = "Leader",
                    avatarUrl = null,
                    rawScore = 100,
                    displayScore = "100",
                    isCurrentPlayer = false,
                ),
            ),
            playerScore = PlayerScore(
                rank = 25,
                rawScore = 40,
                displayScore = "40",
                displayName = "Me",
                avatarUrl = "https://avatar",
            ),
        )

        val row = data.ownRowOutsideTop()

        assertEquals(
            LeaderboardRow(
                rank = 25,
                displayName = "Me",
                avatarUrl = "https://avatar",
                rawScore = 40,
                displayScore = "40",
                isCurrentPlayer = true,
            ),
            row,
        )
    }

    @Test
    fun ownRowOutsideTop_playerAlreadyInTop_returnsNull() {
        val data = LeaderboardData(
            topRows = listOf(
                LeaderboardRow(
                    rank = 3,
                    displayName = "Me",
                    avatarUrl = null,
                    rawScore = 90,
                    displayScore = "90",
                    isCurrentPlayer = true,
                ),
            ),
            playerScore = PlayerScore(
                rank = 3,
                rawScore = 90,
                displayScore = "90",
                displayName = "Me",
                avatarUrl = null,
            ),
        )

        assertNull(data.ownRowOutsideTop())
    }

    @Test
    fun ownRowOutsideTop_noRank_returnsNull() {
        val data = LeaderboardData(
            topRows = emptyList(),
            playerScore = PlayerScore(
                rank = null,
                rawScore = 0,
                displayScore = "0",
                displayName = "Me",
                avatarUrl = null,
            ),
        )

        assertNull(data.ownRowOutsideTop())
    }

    @Test
    fun ownRowOutsideTop_noPlayerScore_returnsNull() {
        val data = LeaderboardData(
            topRows = emptyList(),
            playerScore = null,
        )

        assertNull(data.ownRowOutsideTop())
    }

    @Test
    fun ownRowOutsideTop_playerHasAchievementsCount_carriesItThrough() {
        val data = LeaderboardData(
            topRows = emptyList(),
            playerScore = PlayerScore(
                rank = 10,
                rawScore = 40,
                displayScore = "40",
                displayName = "Me",
                avatarUrl = null,
                achievementsCount = 7,
            ),
        )

        assertEquals(7, data.ownRowOutsideTop()?.achievementsCount)
    }

    @Test
    fun toRow_entryWithAchievementsCount_mapsAllFields() {
        val entry = LeaderboardEntryDto(
            displayName = "Leader",
            avatarUrl = "https://avatar",
            score = 120,
            achievementsCount = 12,
        )

        val row = entry.toRow(rank = 1, isCurrentPlayer = true)

        assertEquals(
            LeaderboardRow(
                rank = 1,
                displayName = "Leader",
                avatarUrl = "https://avatar",
                rawScore = 120,
                displayScore = "120",
                isCurrentPlayer = true,
                achievementsCount = 12,
            ),
            row,
        )
    }

    @Test
    fun toRow_entryWithoutAchievementsCount_mapsNull() {
        val entry = LeaderboardEntryDto(displayName = "Leader", score = 50)

        val row = entry.toRow(rank = 2, isCurrentPlayer = false)

        assertNull(row.achievementsCount)
    }

    @Test
    fun toRow_blankDisplayName_fallsBackToAnonymous() {
        val entry = LeaderboardEntryDto(displayName = "  ", score = 10)

        val row = entry.toRow(rank = 3, isCurrentPlayer = false)

        assertEquals("Anonymous", row.displayName)
    }

    @Test
    fun toPlayerScore_entryWithAchievementsCount_mapsAllFields() {
        val entry = LeaderboardEntryDto(
            displayName = "Me",
            avatarUrl = "https://avatar",
            score = 90,
            achievementsCount = 3,
        )

        val playerScore = entry.toPlayerScore(rank = 5)

        assertEquals(
            PlayerScore(
                rank = 5,
                rawScore = 90,
                displayScore = "90",
                displayName = "Me",
                avatarUrl = "https://avatar",
                achievementsCount = 3,
            ),
            playerScore,
        )
    }

    @Test
    fun toPlayerScore_entryWithoutAchievementsCount_mapsNull() {
        val entry = LeaderboardEntryDto(displayName = "Me", score = 30)

        val playerScore = entry.toPlayerScore(rank = 8)

        assertNull(playerScore.achievementsCount)
    }
}

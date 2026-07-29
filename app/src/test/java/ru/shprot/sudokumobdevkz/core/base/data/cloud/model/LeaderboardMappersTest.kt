package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.LeaderboardMappers.ownRowOutsideTop

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
}

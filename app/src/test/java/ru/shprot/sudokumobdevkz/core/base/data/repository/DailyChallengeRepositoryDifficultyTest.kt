package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.DailyChallengeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.DailyChallengeEntity
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import java.time.LocalDate

class DailyChallengeRepositoryDifficultyTest {

    private val repo = makeDailyChallengeRepository()

    @Test
    fun `difficultyForDate never returns ULTRA for day 1`() {
        val date = LocalDate.ofYearDay(2026, 1).toString()
        assertNotEquals(Difficulty.ULTRA, repo.difficultyForDate(date))
    }

    @Test
    fun `difficultyForDate never returns ULTRA for day 100`() {
        val date = LocalDate.ofYearDay(2026, 100).toString()
        assertNotEquals(Difficulty.ULTRA, repo.difficultyForDate(date))
    }

    @Test
    fun `difficultyForDate never returns ULTRA for day 200`() {
        val date = LocalDate.ofYearDay(2026, 200).toString()
        assertNotEquals(Difficulty.ULTRA, repo.difficultyForDate(date))
    }

    @Test
    fun `difficultyForDate never returns ULTRA for day 300`() {
        val date = LocalDate.ofYearDay(2026, 300).toString()
        assertNotEquals(Difficulty.ULTRA, repo.difficultyForDate(date))
    }

    @Test
    fun `difficultyForDate never returns ULTRA for any day of year`() {
        for (day in 1..365) {
            val date = LocalDate.ofYearDay(2026, day).toString()
            assertNotEquals(
                "day $day should not yield ULTRA",
                Difficulty.ULTRA,
                repo.difficultyForDate(date),
            )
        }
    }

    @Test
    fun `difficultyForDate only returns non-Ultra difficulties`() {
        val allowed = Difficulty.entries.filter { it != Difficulty.ULTRA }.toSet()
        for (day in 1..365) {
            val date = LocalDate.ofYearDay(2026, day).toString()
            val diff = repo.difficultyForDate(date)
            assertTrue(
                "day $day returned $diff which is not in allowed set",
                diff in allowed,
            )
        }
    }

    @Test
    fun `difficultyForDate cycles through all non-ultra difficulties`() {
        val seen = mutableSetOf<Difficulty>()
        for (day in 1..365) {
            val date = LocalDate.ofYearDay(2026, day).toString()
            seen.add(repo.difficultyForDate(date))
        }
        val expected = Difficulty.entries.filter { it != Difficulty.ULTRA }.toSet()
        assertTrue(
            "Expected all non-Ultra difficulties to appear, saw: $seen",
            seen.containsAll(expected),
        )
    }
}

private fun makeDailyChallengeRepository(): DailyChallengeRepository {
    val fakeDao = object : DailyChallengeDao {
        override suspend fun getByDate(dateKey: String): DailyChallengeEntity? = null
        override suspend fun upsert(entity: DailyChallengeEntity) = Unit
        override suspend fun getRecentCompleted(limit: Int): List<DailyChallengeEntity> = emptyList()
        override suspend fun getAllCompletedAsc(): List<DailyChallengeEntity> = emptyList()
        override fun observeAllCompleted(): Flow<List<DailyChallengeEntity>> = flowOf(emptyList())
        override suspend fun getAllCompleted(): List<DailyChallengeEntity> = emptyList()
    }
    return DailyChallengeRepository(fakeDao, NoOpSyncToCloudUseCase)
}
package ru.shprot.sudokumobdevkz.core.base.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.repository.ReviewRepository

private class FakeReviewRepository(
    private var sessionWon: Boolean = false,
    private var lastRequestTs: Long = 0L,
) : ReviewRepository {
    override suspend fun markSessionWon() { sessionWon = true }
    override suspend fun clearSessionWon() { sessionWon = false }
    override suspend fun wasSessionWon(): Boolean = sessionWon
    override suspend fun markReviewRequested() { lastRequestTs = System.currentTimeMillis() }
    override suspend fun lastReviewRequestedAt(): Long = lastRequestTs
}

class ShouldRequestReviewUseCaseTest {

    @Test
    fun `returns false when sessionWon is false`() = runTest {
        val repo = FakeReviewRepository(sessionWon = false)
        val useCase = ShouldRequestReviewUseCase(
            reviewRepository = repo,
            totalWinsProvider = { 100 },
            now = { 0L },
        )
        assertFalse(useCase())
    }

    @Test
    fun `returns false when totalWins under threshold`() = runTest {
        val repo = FakeReviewRepository(sessionWon = true, lastRequestTs = 0L)
        val useCase = ShouldRequestReviewUseCase(
            reviewRepository = repo,
            totalWinsProvider = { 9 },
            now = { 0L },
        )
        assertFalse(useCase())
    }

    @Test
    fun `returns true on first attempt when sessionWon and totalWins above threshold`() = runTest {
        val repo = FakeReviewRepository(sessionWon = true, lastRequestTs = 0L)
        val useCase = ShouldRequestReviewUseCase(
            reviewRepository = repo,
            totalWinsProvider = { 10 },
            now = { 0L },
        )
        assertTrue(useCase())
    }

    @Test
    fun `returns false when cooldown is active`() = runTest {
        val now = 100_000_000_000L
        val cooldownMs = 30L * 24 * 60 * 60 * 1000
        val repo = FakeReviewRepository(sessionWon = true, lastRequestTs = now - cooldownMs + 1)
        val useCase = ShouldRequestReviewUseCase(
            reviewRepository = repo,
            totalWinsProvider = { 100 },
            now = { now },
        )
        assertFalse(useCase())
    }

    @Test
    fun `returns true when cooldown has elapsed exactly`() = runTest {
        val now = 100_000_000_000L
        val cooldownMs = 30L * 24 * 60 * 60 * 1000
        val repo = FakeReviewRepository(sessionWon = true, lastRequestTs = now - cooldownMs)
        val useCase = ShouldRequestReviewUseCase(
            reviewRepository = repo,
            totalWinsProvider = { 100 },
            now = { now },
        )
        assertTrue(useCase())
    }
}
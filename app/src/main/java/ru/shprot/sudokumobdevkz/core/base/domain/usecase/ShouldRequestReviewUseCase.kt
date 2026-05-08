package ru.shprot.sudokumobdevkz.core.base.domain.usecase

import ru.shprot.sudokumobdevkz.core.base.data.repository.ReviewRepository
import ru.shprot.sudokumobdevkz.core.base.data.review.ReviewConfig
import javax.inject.Inject

class ShouldRequestReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val totalWinsProvider: suspend () -> Int,
    private val now: () -> Long,
) {

    suspend operator fun invoke(): Boolean {
        if (!reviewRepository.wasSessionWon()) return false
        if (totalWinsProvider() < ReviewConfig.MIN_TOTAL_WINS) return false

        val lastTs = reviewRepository.lastReviewRequestedAt()
        if (lastTs == 0L) return true

        val cooldownMs = ReviewConfig.COOLDOWN_DAYS * 24L * 60L * 60L * 1000L
        return now() - lastTs >= cooldownMs
    }
}
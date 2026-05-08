package ru.shprot.sudokumobdevkz.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.shprot.sudokumobdevkz.core.base.data.repository.ReviewRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ReviewRepositoryImpl
import ru.shprot.sudokumobdevkz.core.base.data.repository.SudokuRepository
import ru.shprot.sudokumobdevkz.core.base.domain.usecase.ShouldRequestReviewUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewModule {

    @Binds
    @Singleton
    abstract fun bindReviewRepository(impl: ReviewRepositoryImpl): ReviewRepository

    companion object {
        @Provides
        @Singleton
        fun provideShouldRequestReviewUseCase(
            reviewRepository: ReviewRepository,
            sudokuRepository: SudokuRepository,
        ): ShouldRequestReviewUseCase = ShouldRequestReviewUseCase(
            reviewRepository = reviewRepository,
            totalWinsProvider = { sudokuRepository.totalWins() },
            now = { System.currentTimeMillis() },
        )
    }
}
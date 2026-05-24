package ru.shprot.sudokumobdevkz.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.shprot.sudokumobdevkz.core.base.data.database.SudokuComposeDatabase
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.AchievementUnlockedDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.DailyChallengeDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.StatisticDao
import ru.shprot.sudokumobdevkz.core.base.data.database.migration.MIGRATION_3_4
import ru.shprot.sudokumobdevkz.core.base.data.database.migration.MIGRATION_4_5
import ru.shprot.sudokumobdevkz.core.base.data.database.migration.MIGRATION_5_6
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SudokuComposeDatabase =
        Room.databaseBuilder(context, SudokuComposeDatabase::class.java, "sudoku_compose_db")
            .addMigrations(MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideStatisticDao(db: SudokuComposeDatabase): StatisticDao = db.statisticDao()

    @Provides
    fun provideGameHistoryDao(db: SudokuComposeDatabase): GameHistoryDao = db.gameHistoryDao()

    @Provides
    fun provideSavedGameDao(db: SudokuComposeDatabase): SavedGameDao = db.savedGameDao()

    @Provides
    fun provideDailyChallengeDao(db: SudokuComposeDatabase): DailyChallengeDao = db.dailyChallengeDao()

    @Provides
    fun provideAchievementUnlockedDao(db: SudokuComposeDatabase): AchievementUnlockedDao =
        db.achievementUnlockedDao()
}
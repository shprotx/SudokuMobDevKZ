package ru.shprot.sudokumobdevkz.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.shprot.sudokumobdevkz.model.database.SudokuComposeDatabase
import ru.shprot.sudokumobdevkz.model.database.dao.GameHistoryDao
import ru.shprot.sudokumobdevkz.model.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.model.database.dao.StatisticDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SudokuComposeDatabase =
        Room.databaseBuilder(context, SudokuComposeDatabase::class.java, "sudoku_compose_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideStatisticDao(db: SudokuComposeDatabase): StatisticDao = db.statisticDao()

    @Provides
    fun provideGameHistoryDao(db: SudokuComposeDatabase): GameHistoryDao = db.gameHistoryDao()

    @Provides
    fun provideSavedGameDao(db: SudokuComposeDatabase): SavedGameDao = db.savedGameDao()
}

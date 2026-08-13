package ru.shprot.sudokumobdevkz.core.base.data.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.shprot.sudokumobdevkz.core.base.domain.model.VisitStreak
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private val Context.visitStreakDataStore by preferencesDataStore(name = "sudoku_visit_streak")

@Singleton
class VisitStreakRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : IVisitStreakRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _streak: StateFlow<VisitStreak> = context.visitStreakDataStore.data
        .map { it.toVisitStreak() }
        .stateIn(scope, SharingStarted.Eagerly, VisitStreak())

    override val streak: Flow<VisitStreak> = _streak

    override val currentStreak: VisitStreak get() = _streak.value

    override suspend fun recordVisit(): VisitStreak {
        val today = LocalDate.now().toString()
        val updated = context.visitStreakDataStore.edit { prefs ->
            val previous = prefs.toVisitStreak()
            if (previous.lastVisitDate != today) {
                val nextCurrent = VisitStreakCalculator.nextCurrentStreak(
                    today = today,
                    lastVisitDate = previous.lastVisitDate,
                    currentStreak = previous.currentStreak,
                )
                prefs[Keys.CURRENT_STREAK] = nextCurrent
                prefs[Keys.BEST_STREAK] = maxOf(previous.bestStreak, nextCurrent)
                prefs[Keys.LAST_VISIT_DATE] = today
            }
        }
        return updated.toVisitStreak()
    }

    override suspend fun mergeFromCloud(
        cloudCurrentStreak: Int,
        cloudBestStreak: Int,
        cloudLastVisitDate: String?,
    ) {
        context.visitStreakDataStore.edit { prefs ->
            val local = prefs.toVisitStreak()
            val (mergedCurrent, mergedLastVisitDate) = VisitStreakCalculator.mergedCurrentStreak(
                localCurrent = local.currentStreak,
                localLastVisitDate = local.lastVisitDate,
                cloudCurrent = cloudCurrentStreak,
                cloudLastVisitDate = cloudLastVisitDate,
            )
            prefs[Keys.CURRENT_STREAK] = mergedCurrent
            prefs[Keys.BEST_STREAK] = maxOf(local.bestStreak, cloudBestStreak)
            mergedLastVisitDate?.let { prefs[Keys.LAST_VISIT_DATE] = it }
        }
    }

    private fun Preferences.toVisitStreak() = VisitStreak(
        currentStreak = this[Keys.CURRENT_STREAK] ?: 0,
        bestStreak = this[Keys.BEST_STREAK] ?: 0,
        lastVisitDate = this[Keys.LAST_VISIT_DATE],
    )

    private object Keys {
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val BEST_STREAK = intPreferencesKey("best_streak")
        val LAST_VISIT_DATE = stringPreferencesKey("last_visit_date")
    }
}
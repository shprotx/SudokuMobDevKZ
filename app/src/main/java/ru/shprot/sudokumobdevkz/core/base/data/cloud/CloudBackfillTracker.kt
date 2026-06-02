package ru.shprot.sudokumobdevkz.core.base.data.cloud

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.cloudBackfillDataStore by preferencesDataStore("cloud_backfill")

@Singleton
class CloudBackfillTracker @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val achievementsKey = stringSetPreferencesKey(KEY_BACKFILLED_PLAYERS)
    private val leaderboardKey = booleanPreferencesKey(KEY_LEADERBOARD_BACKFILLED)

    suspend fun wasBackfilledFor(playerId: String): Boolean {
        val current = context.cloudBackfillDataStore.data.first()[achievementsKey] ?: emptySet()
        return playerId in current
    }

    suspend fun markBackfilledFor(playerId: String) {
        context.cloudBackfillDataStore.edit { prefs ->
            val current = prefs[achievementsKey] ?: emptySet()
            prefs[achievementsKey] = current + playerId
        }
    }

    suspend fun wasLeaderboardBackfilled(): Boolean =
        context.cloudBackfillDataStore.data.first()[leaderboardKey] ?: false

    suspend fun markLeaderboardBackfilled() {
        context.cloudBackfillDataStore.edit { prefs ->
            prefs[leaderboardKey] = true
        }
    }

    private companion object {
        const val KEY_BACKFILLED_PLAYERS = "backfilled_players_v2"
        const val KEY_LEADERBOARD_BACKFILLED = "leaderboard_backfilled"
    }
}
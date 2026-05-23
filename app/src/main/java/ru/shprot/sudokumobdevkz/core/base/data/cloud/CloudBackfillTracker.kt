package ru.shprot.sudokumobdevkz.core.base.data.cloud

import android.content.Context
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

    private val key = stringSetPreferencesKey(KEY_BACKFILLED_PLAYERS)

    suspend fun wasBackfilledFor(playerId: String): Boolean {
        val current = context.cloudBackfillDataStore.data.first()[key] ?: emptySet()
        return playerId in current
    }

    suspend fun markBackfilledFor(playerId: String) {
        context.cloudBackfillDataStore.edit { prefs ->
            val current = prefs[key] ?: emptySet()
            prefs[key] = current + playerId
        }
    }

    private companion object {
        const val KEY_BACKFILLED_PLAYERS = "backfilled_players"
    }
}

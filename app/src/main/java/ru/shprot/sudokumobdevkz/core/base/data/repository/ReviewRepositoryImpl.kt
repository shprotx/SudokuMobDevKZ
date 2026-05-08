package ru.shprot.sudokumobdevkz.core.base.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.reviewDataStore by preferencesDataStore(name = "review_state")

@Singleton
class ReviewRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : ReviewRepository {

    @Inject
    constructor(@ApplicationContext context: Context) : this(context.reviewDataStore)

    override suspend fun markSessionWon() {
        dataStore.edit { prefs ->
            prefs[Keys.SESSION_WON] = true
        }
    }

    override suspend fun clearSessionWon() {
        dataStore.edit { prefs ->
            prefs[Keys.SESSION_WON] = false
        }
    }

    override suspend fun wasSessionWon(): Boolean {
        val prefs = dataStore.data.first()
        return prefs[Keys.SESSION_WON] ?: false
    }

    override suspend fun markReviewRequested() {
        dataStore.edit { prefs ->
            prefs[Keys.LAST_REQUEST_TS] = System.currentTimeMillis()
        }
    }

    override suspend fun lastReviewRequestedAt(): Long {
        val prefs = dataStore.data.first()
        return prefs[Keys.LAST_REQUEST_TS] ?: 0L
    }

    private object Keys {
        val SESSION_WON = booleanPreferencesKey("session_won")
        val LAST_REQUEST_TS = longPreferencesKey("last_request_ts")
    }
}
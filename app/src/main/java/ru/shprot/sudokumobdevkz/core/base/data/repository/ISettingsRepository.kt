package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.flow.Flow
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings

interface ISettingsRepository {
    val settings: Flow<AppSettings>
    val currentSettings: AppSettings
    fun update(transform: AppSettings.() -> AppSettings)
    fun setNotificationsEnabled(enabled: Boolean) {
        update { copy(notificationsEnabled = enabled) }
    }
    fun isLeaderboardNamePromptShown(): Flow<Boolean>
    fun markLeaderboardNamePromptShown()
}
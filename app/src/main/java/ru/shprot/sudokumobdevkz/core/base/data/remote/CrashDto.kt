package ru.shprot.sudokumobdevkz.core.base.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class CrashDto(
    val timestamp: String,
    val versionName: String,
    val versionCode: Int,
    val device: String,
    val android: Int,
    val stacktrace: String,
)

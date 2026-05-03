package ru.shprot.sudokumobdevkz.model.remote

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

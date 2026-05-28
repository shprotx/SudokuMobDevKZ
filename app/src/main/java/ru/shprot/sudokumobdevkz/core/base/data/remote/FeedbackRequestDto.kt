package ru.shprot.sudokumobdevkz.core.base.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackRequestDto(
    val text: String,
    val appVersion: String,
    val deviceModel: String,
    val androidSdk: Int,
    val locale: String,
    val isPgsSignedIn: Boolean,
)
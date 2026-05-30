package ru.shprot.sudokumobdevkz.core.base.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class GameContextDto(
    val difficulty: Int,
    val timeSeconds: Int,
    val errors: Int,
    val hintsUsed: Int,
    val isDaily: Boolean,
)
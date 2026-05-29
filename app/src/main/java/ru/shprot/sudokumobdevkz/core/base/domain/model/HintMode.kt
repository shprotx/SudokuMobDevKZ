package ru.shprot.sudokumobdevkz.core.base.domain.model

enum class HintMode {
    SINGLE_SHOT,
    TOGGLE;

    companion object {
        fun from(value: String?): HintMode =
            value?.let { runCatching { valueOf(it) }.getOrNull() } ?: SINGLE_SHOT
    }
}

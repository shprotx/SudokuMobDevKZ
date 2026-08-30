package ru.shprot.sudokumobdevkz.core.base.domain.model

enum class StatusItemId {
    DIFFICULTY,
    ERRORS,
    LIVES,
    TIMER,
    ;

    companion object {
        val DEFAULT_ORDER: List<StatusItemId> = listOf(DIFFICULTY, ERRORS, LIVES, TIMER)

        fun parseOrder(raw: String?): List<StatusItemId> {
            if (raw.isNullOrBlank()) return DEFAULT_ORDER
            val parsed = raw.split(",").mapNotNull { name ->
                entries.firstOrNull { it.name == name.trim() }
            }
            return if (parsed.toSet() == entries.toSet()) parsed else DEFAULT_ORDER
        }

        fun serializeOrder(order: List<StatusItemId>): String =
            order.joinToString(",") { it.name }
    }
}

package ru.shprot.sudokumobdevkz.core.base.domain.model

enum class ActionButtonId {
    UNDO,
    ERASE,
    NOTE,
    HINT,
    ;

    companion object {
        val DEFAULT_ORDER: List<ActionButtonId> = listOf(UNDO, ERASE, NOTE, HINT)

        fun parseOrder(raw: String?): List<ActionButtonId> {
            if (raw.isNullOrBlank()) return DEFAULT_ORDER
            val parsed = raw.split(",").mapNotNull { name ->
                entries.firstOrNull { it.name == name.trim() }
            }
            return if (parsed.toSet() == entries.toSet()) parsed else DEFAULT_ORDER
        }

        fun serializeOrder(order: List<ActionButtonId>): String =
            order.joinToString(",") { it.name }
    }
}

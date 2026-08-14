package ru.shprot.sudokumobdevkz.core.base.domain.model

enum class GameBlockId {
    STATUS_BAR,
    GRID,
    NUMBER_PAD,
    ACTIONS_BAR,
    ;

    companion object {
        val DEFAULT_ORDER: List<GameBlockId> = listOf(STATUS_BAR, GRID, NUMBER_PAD, ACTIONS_BAR)

        fun parseOrder(raw: String?): List<GameBlockId> {
            if (raw.isNullOrBlank()) return DEFAULT_ORDER
            val parsed = raw.split(",").mapNotNull { name ->
                entries.firstOrNull { it.name == name.trim() }
            }
            return if (parsed.toSet() == entries.toSet()) parsed else DEFAULT_ORDER
        }

        fun serializeOrder(order: List<GameBlockId>): String =
            order.joinToString(",") { it.name }
    }
}

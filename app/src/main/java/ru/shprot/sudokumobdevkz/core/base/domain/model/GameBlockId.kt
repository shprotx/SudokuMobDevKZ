package ru.shprot.sudokumobdevkz.core.base.domain.model

enum class GameBlockId {
    STATUS_BAR,
    GRID,
    NUMBER_PAD,
    ACTIONS_BAR,
    SPACER_1,
    SPACER_2,
    SPACER_3,
    ;

    val isSpacer: Boolean get() = this == SPACER_1 || this == SPACER_2 || this == SPACER_3

    companion object {
        val DEFAULT_ORDER: List<GameBlockId> = listOf(
            SPACER_1,
            STATUS_BAR,
            GRID,
            SPACER_2,
            NUMBER_PAD,
            SPACER_3,
            ACTIONS_BAR,
        )

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

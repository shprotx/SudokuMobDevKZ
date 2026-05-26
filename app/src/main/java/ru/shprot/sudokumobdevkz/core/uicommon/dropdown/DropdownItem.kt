package ru.shprot.sudokumobdevkz.core.uicommon.dropdown

import androidx.compose.runtime.Composable

interface DropdownItem {
    val id: String

    @Composable
    fun title(): String
}
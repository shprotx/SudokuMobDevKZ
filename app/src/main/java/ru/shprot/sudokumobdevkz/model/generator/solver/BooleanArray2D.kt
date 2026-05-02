package ru.shprot.sudokumobdevkz.model.generator.solver

internal class BooleanArray2D(val rows: Int, val cols: Int) {
    private val data = BooleanArray(rows * cols)

    operator fun get(row: Int, col: Int): Boolean = data[row * cols + col]

    operator fun set(row: Int, col: Int, value: Boolean) {
        data[row * cols + col] = value
    }

    fun fillRow(row: Int, value: Boolean) {
        val offset = row * cols
        for (i in 0 until cols) {
            data[offset + i] = value
        }
    }
}

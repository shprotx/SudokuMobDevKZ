package ru.shprot.sudokumobdevkz.model.generator.solver

import java.util.LinkedList

internal class DancingLinks(cover: BooleanArray2D) {

    private val header: ColumnNode = makeDLXBoard(cover)
    private var answer: MutableList<DancingNode> = LinkedList()

    var solutionsCount: Int = 0
        private set

    fun runSolver() {
        answer = LinkedList()
        solutionsCount = 0
        search(0)
    }

    private fun search(k: Int) {
        if (solutionsCount >= 2) return
        if (header.R == header) {
            solutionsCount++
            return
        }

        val c = selectColumnNodeHeuristic() ?: return
        c.cover()

        var r = c.D
        while (r != c) {
            answer.add(r)

            var j = r.R
            while (j != r) {
                (j.C as ColumnNode).cover()
                j = j.R
            }

            search(k + 1)

            r = answer.removeAt(answer.size - 1)
            val col = r.C as ColumnNode

            j = r.L
            while (j != r) {
                (j.C as ColumnNode).uncover()
                j = j.L
            }

            r = r.D
        }

        c.uncover()
    }

    private fun selectColumnNodeHeuristic(): ColumnNode? {
        var min = Int.MAX_VALUE
        var ret: ColumnNode? = null
        var c = header.R.C as? ColumnNode
        while (c != null && c != header) {
            if (c.size < min) {
                min = c.size
                ret = c
            }
            c = c.R.C as? ColumnNode
        }
        return ret
    }

    private fun makeDLXBoard(grid: BooleanArray2D): ColumnNode {
        val cols = grid.cols

        var headerNode = ColumnNode("header")
        val columnNodes = mutableListOf<ColumnNode>()

        for (i in 0 until cols) {
            val n = ColumnNode(i.toString())
            columnNodes.add(n)
            headerNode = headerNode.hookRight(n).C as ColumnNode
        }
        headerNode = headerNode.R.C as ColumnNode

        for (row in 0 until grid.rows) {
            var prev: DancingNode? = null
            for (j in 0 until cols) {
                if (grid[row, j]) {
                    val col = columnNodes[j]
                    val newNode = DancingNode(col)
                    if (prev == null) prev = newNode
                    col.U.hookDown(newNode)
                    prev = prev.hookRight(newNode)
                    col.size++
                }
            }
        }

        headerNode.size = cols
        return headerNode
    }
}

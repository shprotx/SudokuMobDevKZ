package ru.shprot.sudokumobdevkz.model.generator.solver

internal open class DancingNode(var C: ColumnNode? = null) {
    var L: DancingNode = this
    var R: DancingNode = this
    var U: DancingNode = this
    var D: DancingNode = this

    fun hookDown(node: DancingNode): DancingNode {
        node.D = D
        node.D.U = node
        node.U = this
        D = node
        return node
    }

    fun hookRight(node: DancingNode): DancingNode {
        node.R = R
        node.R.L = node
        node.L = this
        R = node
        return node
    }

    fun unlinkLR() {
        L.R = R
        R.L = L
    }

    fun relinkLR() {
        R.L = this
        L.R = this
    }

    fun unlinkUD() {
        U.D = D
        D.U = U
    }

    fun relinkUD() {
        D.U = this
        U.D = this
    }
}

package ru.shprot.sudokumobdevkz.core.base.domain.generator.solver

internal class ColumnNode(val name: String) : DancingNode() {
    var size: Int = 0

    init {
        C = this
    }

    fun cover() {
        unlinkLR()
        var i = D
        while (i != this) {
            var j = i.R
            while (j != i) {
                j.unlinkUD()
                j.C!!.size--
                j = j.R
            }
            i = i.D
        }
    }

    fun uncover() {
        var i = U
        while (i != this) {
            var j = i.L
            while (j != i) {
                j.C!!.size++
                j.relinkUD()
                j = j.L
            }
            i = i.U
        }
        relinkLR()
    }
}

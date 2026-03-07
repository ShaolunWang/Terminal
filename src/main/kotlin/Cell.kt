package org.example

data class CellAttr(
    val fg: TermColor = TermColor.Default,
    val bg: TermColor = TermColor.Default,
    val style: StyleFlags = StyleFlags.NONE
)

data class Cell(
    val char: Char? = null,
    var attr: CellAttr = CellAttr()

) {
    companion object {
        val EMPTY = Cell()
    }
}
package org.example

class Line(val width: Int) {
    private val cells: Array<Cell> = Array(width) { Cell.EMPTY }
    operator fun get(col: Int): Cell = cells[col]
    operator fun set(col: Int, cell: Cell) {
        cells[col] = cell
    }
}

class TerminalBuffer(val width: Int, val height: Int, val maxScrollBack: Int = 5000) {
    init {
        require(width > 0) { "Width is negative: $width" }
        require(height > 0) { "Height is negative: $height" }
        require(maxScrollBack > 0) { "Max scroll back is negative: $maxScrollBack" }
    }

    // using array since the screen is fixed size (for now!)
    private val screen: Array<Line> = Array(height) { Line(width) }
    private val scrollback: ArrayDeque<Line> = ArrayDeque()
    private var cursorCol: Int = 0
    private var cursorRow: Int = 0
    var currentAttrs: CellAttr = CellAttr()
        private set
}
package org.example

class Line(val width: Int) {
    public val cells: ArrayList<Cell> = ArrayList(MutableList(width) { Cell.EMPTY })
    operator fun get(col: Int): Cell = cells[col]
    operator fun set(col: Int, cell: Cell) {
        cells[col] = cell
    }


    override fun toString(): String {
        return buildString {
            for (cell in cells) {
                // use Unicode null if cell is null
                append(cell.char ?: '\u0000')
            }
        }
    }

}

class TerminalBuffer(val width: Int, val height: Int, val maxScrollBack: Int = 5000) {
    init {
        require(width > 0) { "Width is negative: $width" }
        require(height > 0) { "Height is negative: $height" }
        require(maxScrollBack > 0) { "Max scroll back is negative: $maxScrollBack" }
    }

    // NOTE: I think for both of them intuitively ringbuffer sounds like a
    // more intuitive data structure,
    // However for the sake of simplicity I'd use ArrayDeque

    private val screen: ArrayDeque<Line> = ArrayDeque<Line>(height).apply {
        repeat(height) {
            add(Line(width))
        }
    }
    private val scrollback: ArrayDeque<Line> = ArrayDeque(maxScrollBack);
    public fun getLineAsString(i: Int): String {
        return screen[i].toString()
    }

    public fun getScrollBackSize(): Int {
        return scrollback.size
    }

    public fun getScrollBackLineAsString(i: Int): String {
        return scrollback[i].toString()
    }

    var currentAttrs: CellAttr = CellAttr()
        private set
    private val cursor = Cursor(width, height)


    // Cursor functions
    public fun getCursorPosition(): Pair<Int, Int> {
        return Pair(cursor.column, cursor.row)
    }
}

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


    public fun getScrollBackSize(): Int {
        return scrollback.size
    }

    public fun getScrollBackLineAsString(i: Int): String {
        return scrollback[i].toString()
    }

    var currentAttrs: CellAttr = CellAttr()
        private set
    private val cursor = Cursor(width, height)

    /// scroll up function. Does not update cursor
    private fun scrollUp() {
        // it's ok if our scrollback buffer has nothing there
        scrollback.removeFirstOrNull();
        val line = screen.removeFirst();
        // need to allocate a new line to the buffer
        screen.addLast(Line(width))
        scrollback.addLast(line)
        // update the write cursor
        cursor.setWriterPos(cursor.writeCol, cursor.writeRow - 1)
        cursor.syncRender()

    }


    /* Cursor functions */
    public fun getWriteCursorPosition(): Pair<Int, Int> {
        return cursor.writePosition()
    }

    public fun setWriteCursorPosition(x: Int, y: Int) {
        cursor.setWriterPos(x, y)
    }

    public fun getCursorPosition(): Pair<Int, Int> {
        return cursor.renderPosition()
    }

    /* edit functions */
    // fill a line with current set attr
    public fun fillLine(row: Int, char: Char) {
        for (col in 0 until width) {
            screen[row][col] = Cell(char, currentAttrs)
        }
    }

    // insert a character at latest cursor position
    public fun writeChar(ch: Char) {
        if (cursor.writeCol >= 0 && cursor.writeRow >= height) {
            scrollUp()
        }
        cursor.syncRender()
        val col = cursor.writeCol
        val row = cursor.writeRow

        screen[row][col] = Cell(ch, currentAttrs)
        val (newCol, newRow) = cursor.advanceWrite()
    }


    // insert char at current cursor position
    // NOTE: changing our implementation to ringbuffer
    // would significantly reduce the cost of this function
    fun insertChar(ch: Char) {
        // need to check for scrollup because
        // render
        if (cursor.writeCol >= 0 && cursor.writeRow >= height) {
            scrollUp()
        }
        var col = cursor.writeCol
        var row = cursor.writeRow

        // tempCell holds the character to insert on each line
        var tempCell = Cell.EMPTY

        // current line requires special handling
        val currentLine = screen[cursor.writeRow]
        currentLine.cells.add(col, Cell(ch, currentAttrs))
        val hasEmptyCellCurrentLine = currentLine.cells.any { it.char == '\u0000' }
        if (!hasEmptyCellCurrentLine) {
            tempCell = currentLine.cells.removeLast()
        } else {
            currentLine.cells.removeLast()
            return
        }


        for (row in (cursor.writeRow + 1) until screen.size) {
            val line = screen[row]
            if (tempCell != Cell.EMPTY) {
                line.cells.add(0, tempCell)
                // first we check whether there are nullchars in the line
                // if there are we can call it a day
                val hasEmptyCell = line.cells.any { it.char == '\u0000' }
                if (hasEmptyCell) {
                    tempCell = Cell.EMPTY
                    line.cells.removeLast()
                    break
                } else {
                    tempCell = line.cells.removeLast()
                }
            }

        }
        // this means that we need to scrollup
        if (tempCell != Cell.EMPTY) {
            scrollUp()
            val newLine = Line(width)
            newLine.cells[0] = tempCell
            screen.add(newLine)
        } else {
            cursor.advanceWrite()
        }
        // NOTE: we don't update the cursor since it's in place insert
    }

    /* cursor independent functions */
    public fun insertBlankLine() {
        scrollUp()
    }

    /* cursor functions */
    fun moveCursorUp() {
        cursor.moveUp()
    }

    fun moveCursorDown() {
        cursor.moveDown()
    }

    fun moveCursorLeft() {
        cursor.moveLeft()
    }

    fun moveCursorRight() {
        cursor.moveRight()
    }


    public fun setAttr(attr: CellAttr) {
        this.currentAttrs = attr
    }

    public fun screenToString(): String {
        return screen.joinToString("\n") { line ->
            line.toString()
        }
    }


    public fun scrollbackToString(): String {
        return scrollback.joinToString("\n") { it.toString() }
    }

    public fun getLineAsString(i: Int): String {
        return screen[i].toString()
    }

    public fun charAtScreen(x: Int, y: Int): Char? {
        return screen[y][x].char
    }

    public fun charAtScrollBack(x: Int, y: Int): Char? {
        return scrollback[y][x].char
    }

    public fun attrAtScreen(x: Int, y: Int): CellAttr {
        return screen[y][x].attr
    }

    public fun attrAtScrollBack(x: Int, y: Int): CellAttr {
        return scrollback[y][x].attr
    }

    override fun toString(): String =
        buildString {
            append(scrollbackToString())
            append("\n")
            append(screenToString())
        }


    public fun clear() {
        clearScreen()
        clearScrollBack()
    }

    public fun clearScreen() {
        screen.clear()
        screen.apply {
            repeat(height) {
                add(Line(width))
            }
        }
    }

    public fun clearScrollBack() {
        // it's ok to clear this
        // since I *think* terminals usually renders by
        // available scrollbacks
        scrollback.clear()
    }
}

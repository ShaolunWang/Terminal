package org.example

class Cursor(
    private val width: Int,
    private val height: Int,
) {
    var writeCol: Int = 0
        private set
    var writeRow: Int = 0
        private set

    var renderCol: Int = 0
        private set
    var renderRow: Int = 0
        private set

    fun set(col: Int, row: Int, syncRender: Boolean = true): Boolean {
        val inCol = col in 0 until width
        val inRow = row in 0 until height

        writeCol = col.coerceIn(0, width - 1)
        writeRow = row.coerceIn(0, height - 1)

        if (syncRender) {
            renderCol = writeCol
            renderRow = writeRow
        }

        return inCol && inRow
    }

    fun advanceWrite(): Pair<Int, Int> {
        writeCol++
        if (writeCol >= width) {
            writeCol = 0
            writeRow++
        }
        // NOTE: no need to update the row yet,
        // we do that on termbuffer because this is related to scroll up
        return Pair(writeCol, writeRow)
        // render cursor is NOT advanced automatically
    }

    fun syncRender() {
        renderCol = writeCol
        renderRow = writeRow
    }


    /** Move render cursor up n lines, optionally updating write cursor */
    fun moveUp(n: Int = 1, updateWrite: Boolean = true): Boolean {
        val oldRow = renderRow
        renderRow = (renderRow - n).coerceAtLeast(0)
        if (updateWrite) writeRow = renderRow
        return (oldRow - n) >= 0
    }

    fun moveDown(n: Int = 1, updateWrite: Boolean = true): Boolean {
        val oldRow = renderRow
        renderRow = (renderRow + n).coerceAtMost(height - 1)
        if (updateWrite) writeRow = renderRow
        return (oldRow + n) < height
    }

    fun moveLeft(n: Int = 1, updateWrite: Boolean = true): Boolean {
        val oldCol = renderCol
        renderCol = (renderCol - n).coerceAtLeast(0)
        if (updateWrite) writeCol = renderCol + 1
        return (oldCol - n) >= 0
    }

    fun moveRight(n: Int = 1, updateWrite: Boolean = true): Boolean {
        val oldCol = renderCol
        renderCol = (renderCol + n).coerceAtMost(width - 1)
        if (updateWrite) writeCol = renderCol + 1
        return (oldCol + n) < width
    }

    /** Set both cursors explicitly */
    fun set(col: Int, row: Int): Boolean {
        val inCol = col in 0 until width
        val inRow = row in 0 until height
        writeCol = col.coerceIn(0, width - 1)
        writeRow = row.coerceIn(0, height - 1)
        renderCol = writeCol
        renderRow = writeRow
        return inCol && inRow
    }

    fun setWriterPos(col: Int, row: Int): Boolean {
        val inCol = col in 0 until width
        val inRow = row in 0 until height
        writeCol = col.coerceIn(0, width - 1)
        writeRow = row.coerceIn(0, height - 1)
        return inCol && inRow
    }

    fun reset(syncRender: Boolean = true) {
        writeCol = 0
        writeRow = 0
        if (syncRender) {
            renderCol = 0
            renderRow = 0
        }
    }

    fun writePosition(): Pair<Int, Int> = Pair(writeCol, writeRow)
    fun renderPosition(): Pair<Int, Int> = Pair(renderCol, renderRow)
}
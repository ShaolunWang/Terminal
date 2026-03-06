package org.example

class Cursor(
    private val width: Int,
    private val height: Int,
) {
    var column: Int = 0
        private set
    var row: Int = 0
        private set

    // coerce clamp the cursor movement

    fun set(column: Int, row: Int): Boolean {
        val inColumn = column in 0 until width
        val inRow = row in 0 until height

        this.column = column.coerceIn(0, width - 1)
        this.row = row.coerceIn(0, height - 1)

        return inColumn && inRow
    }

    fun moveUp(n: Int = 1): Boolean {
        val inBounds = row - n >= 0
        row = (row - n).coerceAtLeast(0)
        return inBounds
    }

    fun moveDown(n: Int = 1): Boolean {
        val inBounds = row + n < height
        row = (row + n).coerceAtMost(height - 1)
        return inBounds
    }

    fun moveLeft(n: Int = 1): Boolean {
        val inBounds = column - n >= 0
        column = (column - n).coerceAtLeast(0)
        return inBounds
    }

    fun moveRight(n: Int = 1): Boolean {
        val inBounds = column + n < width
        column = (column + n).coerceAtMost(width - 1)
        return inBounds
    }

    fun reset() {
        column = 0
        row = 0
    }
}
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

    fun set(row: Int, column: Int) {
        this.column = column.coerceIn(0, width - 1)
        this.row = row.coerceIn(0, height - 1)
    }

    fun moveUp(n: Int = 1) {
        row = (row - n).coerceAtLeast(0)
    }

    fun moveLeft(n: Int = 1) {
        column = (column - n).coerceAtLeast(0)
    }

    fun moveRight(n: Int = 1) {
        column = (column + n).coerceAtMost(width - 1)
    }


    fun moveDown(n: Int = 1) {
        row = (row + n).coerceAtMost(height - 1)
    }

    fun reset() {
        column = 0
        row = 0
    }
}
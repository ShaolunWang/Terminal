package org.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CursorTest {

    private lateinit var cursor: Cursor

    @BeforeEach
    fun setup() {
        cursor = Cursor(width = 10, height = 5)
    }

    @Test
    fun `cursor starts at origin`() {
        assertEquals(0, cursor.column)
        assertEquals(0, cursor.row)
    }

    @Test
    fun `set position within bounds`() {
        cursor.set(3, 2)

        assertEquals(3, cursor.column)
        assertEquals(2, cursor.row)
    }

    @Test
    fun `set values outside bounds for clamping`() {
        cursor.set(50, -10)
        // width - 1
        assertEquals(9, cursor.column)
        assertEquals(0, cursor.row)
    }

    @Test
    fun `move right`() {
        cursor.moveRight(3)

        assertEquals(3, cursor.column)
        assertEquals(0, cursor.row)
    }

    @Test
    fun `move right with clamping`() {
        cursor.moveRight(100)

        assertEquals(9, cursor.column)
    }

    @Test
    fun `move left`() {
        cursor.set(5, 0)
        cursor.moveLeft(3)

        assertEquals(2, cursor.column)
    }

    @Test
    fun `move left clamping`() {
        cursor.moveLeft(5)

        assertEquals(0, cursor.column)
    }

    @Test
    fun `move down`() {
        cursor.moveDown(2)

        assertEquals(2, cursor.row)
    }

    @Test
    fun `move down clamping`() {
        cursor.moveDown(100)
        // height - 1
        assertEquals(4, cursor.row)
    }

    @Test
    fun `move up`() {
        cursor.set(0, 3)
        cursor.moveUp(2)

        assertEquals(1, cursor.row)
    }

    @Test
    fun `move up clamping`() {
        cursor.moveUp(5)

        assertEquals(0, cursor.row)
    }

    @Test
    fun reset() {
        cursor.set(7, 4)

        cursor.reset()

        assertEquals(0, cursor.column)
        assertEquals(0, cursor.row)
    }
}

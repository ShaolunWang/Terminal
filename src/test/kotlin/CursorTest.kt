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
    fun `set cursor within bounds returns true`() {
        val result = cursor.set(3, 2)
        assertTrue(result)
        assertEquals(3, cursor.column)
        assertEquals(2, cursor.row)
    }

    @Test
    fun `set cursor outside bounds returns false and clamps`() {
        val result = cursor.set(50, -10)
        assertFalse(result)
        assertEquals(9, cursor.column)
        assertEquals(0, cursor.row)
    }

    @Test
    fun `move right within bounds returns true`() {
        val result = cursor.moveRight(3)
        assertTrue(result)
        assertEquals(3, cursor.column)
    }

    @Test
    fun `move right exceeding bounds returns false and clamps`() {
        val result = cursor.moveRight(100)
        assertFalse(result)
        assertEquals(9, cursor.column)
    }

    @Test
    fun `move left within bounds returns true`() {
        cursor.set(5, 0)
        val result = cursor.moveLeft(3)
        assertTrue(result)
        assertEquals(2, cursor.column)
    }

    @Test
    fun `move left exceeding bounds returns false and clamps`() {
        val result = cursor.moveLeft(5)
        assertFalse(result)
        assertEquals(0, cursor.column)
    }

    @Test
    fun `move down within bounds returns true`() {
        val result = cursor.moveDown(2)
        assertTrue(result)
        assertEquals(2, cursor.row)
    }

    @Test
    fun `move down exceeding bounds returns false and clamps`() {
        val result = cursor.moveDown(100)
        assertFalse(result)
        assertEquals(4, cursor.row)
    }

    @Test
    fun `move up within bounds returns true`() {
        cursor.set(0, 3)
        val result = cursor.moveUp(2)
        assertTrue(result)
        assertEquals(1, cursor.row)
    }

    @Test
    fun `move up exceeding bounds returns false and clamps`() {
        val result = cursor.moveUp(5)
        assertFalse(result)
        assertEquals(0, cursor.row)
    }

    @Test
    fun `reset returns cursor to origin`() {
        cursor.set(7, 4)
        cursor.reset()
        assertEquals(0, cursor.column)
        assertEquals(0, cursor.row)
    }
}

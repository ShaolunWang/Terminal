package org.example


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TerminalBufferRenderCursorMoveTest {

    @Test
    fun `test cursor move updates write and render position correctly`() {
        val buf = TerminalBuffer(width = 5, height = 3)

        buf.writeChar('A')
        buf.writeChar('B')

        buf.moveCursorLeft()
        // Render and Write positions should be in sync
        assertEquals(Pair(0, 0), buf.getCursorPosition()) // Render position
        assertEquals(Pair(1, 0), buf.getWriteCursorPosition()) // Write position

        // Move right
        buf.moveCursorRight()
        assertEquals(Pair(1, 0), buf.getCursorPosition()) // Render position
        assertEquals(Pair(2, 0), buf.getWriteCursorPosition()) // Write position

        // Move down
        buf.moveCursorDown()
        assertEquals(Pair(1, 1), buf.getCursorPosition()) // Render position
        assertEquals(Pair(2, 1), buf.getWriteCursorPosition()) // Write position

        // Move up
        buf.moveCursorUp()
        assertEquals(Pair(1, 0), buf.getCursorPosition()) // Render position
        assertEquals(Pair(2, 0), buf.getWriteCursorPosition()) // Write position

        // doesn't wrap, doesn't scroll
        buf.moveCursorRight()
        buf.moveCursorRight()
        buf.moveCursorRight()
        buf.moveCursorDown()

        assertEquals(Pair(4, 1), buf.getCursorPosition()) // Render position at start of new line
        assertEquals(Pair(5, 1), buf.getWriteCursorPosition()) // Write position moved to next column
    }
}

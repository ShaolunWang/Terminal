package org.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TerminalBufferTest {

    private val nullString = "\u0000"

    @Test
    fun `writeChar overwrites line without wrapping`() {
        val buf = TerminalBuffer(width = 5, height = 3)

        // write "ABC"
        buf.writeChar('A')
        buf.writeChar('B')
        buf.writeChar('C')

        assertEquals("ABC" + nullString.repeat(2), buf.getLineAsString(0))
        assertEquals(Pair(3, 0), buf.getWriteCursorPosition())

        buf.writeChar('D')
        buf.writeChar('E')

        assertEquals("ABCDE", buf.getLineAsString(0))
        assertEquals(Pair(0, 1), buf.getWriteCursorPosition())
    }

    @Test
    fun `writeChar wraps to next line`() {
        val buf = TerminalBuffer(width = 5, height = 3)

        val text = "ABCDEFG"
        for (ch in text) {
            buf.writeChar(ch)
        }

        assertEquals("ABCDE", buf.getLineAsString(0))
        assertEquals("FG" + nullString.repeat(3), buf.getLineAsString(1))
        assertEquals(Pair(2, 1), buf.getWriteCursorPosition())
    }

    @Test
    fun `writeChar scrolls when bottom is reached`() {
        val buf = TerminalBuffer(width = 5, height = 2, maxScrollBack = 10)

        val text = "ABCDEFGHIJ" // fills both lines
        for (ch in text) {
            buf.writeChar(ch)
        }

        // write more chars to trigger scroll
        buf.writeChar('K')
        buf.writeChar('L')
        buf.writeChar('M')

        // scrollback test
        assertEquals(1, buf.getScrollBackSize())
        assertEquals("ABCDE", buf.getScrollBackLineAsString(0))
        // second entry should have KLM+2*nullstring
        assertEquals("KLM" + nullString.repeat(2), buf.getLineAsString(1))
        // cursor should at line 2 (0-indexed), pos 3(0-indexed)
        assertEquals(Pair(3, 1), buf.getWriteCursorPosition())
    }
}

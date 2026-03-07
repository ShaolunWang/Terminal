package org.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TerminalBufferEditTest {

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

    @Test
    fun `insertChar shifts and wraps within a line`() {
        val buf = TerminalBuffer(width = 5, height = 2)

        // initial content: ABCDE
        buf.writeChar('A')
        buf.writeChar('B')
        buf.writeChar('C')
        buf.writeChar('D')
        buf.writeChar('E')

        // move cursor to col=2, row=0
        buf.setWriteCursorPosition(2, 0)

        // insert 'X'
        buf.insertChar('X')

        // expected:
        // line 0: ABXCD
        // line 1: E<empty><empty><empty><empty>
        assertEquals("ABXCD", buf.getLineAsString(0))
        assertEquals("E" + nullString.repeat(4), buf.getLineAsString(1))
        assertEquals(Pair(3, 0), buf.getWriteCursorPosition())
    }

    @Test
    fun `insertChar wraps to next line when end of line reached`() {
        val buf = TerminalBuffer(width = 5, height = 2)

        // fill first line completely
        buf.writeChar('A')
        buf.writeChar('B')
        buf.writeChar('C')
        buf.writeChar('D')
        buf.writeChar('E')

        // cursor at col=4, row=0
        buf.setWriteCursorPosition(4, 0)

        buf.insertChar('X')

        // expected:
        // line 0: ABCDX
        // line 1: E<null>...
        assertEquals("ABCDX", buf.getLineAsString(0))
        assertEquals("E" + nullString.repeat(4), buf.getLineAsString(1))
        assertEquals(Pair(0, 1), buf.getWriteCursorPosition()) // wrapped to next line
    }

    @Test
    fun `insertChar scrolls when bottom is reached`() {
        val buf = TerminalBuffer(width = 5, height = 2, maxScrollBack = 10)

        // fill screen completely
        buf.writeChar('A')
        buf.writeChar('B')
        buf.writeChar('C')
        buf.writeChar('D')
        buf.writeChar('E') // line 0
        buf.writeChar('F')
        buf.writeChar('G')
        buf.writeChar('H')
        buf.writeChar('I')
        buf.writeChar('J') // line 1

        // writer cursor at col=2, row=1
        // buf.setCursorPosition(2, 1)

        // insert 'X'
        buf.insertChar('X')

        // NOTE: I will admit this is very confusing
        // After insertion first line should scroll into scrollback
        // since we have 2 full lines, we trigger scrollback on insertion
        // THEN we insert a newchar on line 1
        // writer cursor then should be right after the newchar
        assertEquals(1, buf.getScrollBackSize())
        assertEquals("FGHIJ", buf.getLineAsString(0))
        assertEquals("X" + nullString.repeat(4), buf.getLineAsString(1))
        assertEquals(Pair(1, 1), buf.getWriteCursorPosition())

        buf.setWriteCursorPosition(1, 0)
        buf.insertChar('X')
        assertEquals("FXGHI", buf.getLineAsString(0))
        assertEquals("JX" + nullString.repeat(3), buf.getLineAsString(1))

        assertEquals(Pair(2, 0), buf.getWriteCursorPosition())
    }
}

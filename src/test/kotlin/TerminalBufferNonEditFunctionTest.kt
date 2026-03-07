package org.example


import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TerminalBufferNonEditFunctionTest {


    class TerminalBufferScreenTest {

        @Test
        fun `charAtScreen returns correct character`() {
            val buffer = TerminalBuffer(5, 3)

            buffer.writeChar('A')

            assertEquals('A', buffer.charAtScreen(0, 0))
        }

        @Test
        fun `attrAtScreen returns correct attributes`() {
            val buffer = TerminalBuffer(5, 3)

            val bufAttr = CellAttr(TermColor.BrightBlue, TermColor.Red, StyleFlags(StyleFlags.BOLD))
            buffer.setAttr(bufAttr)
            buffer.writeChar('A')

            val attr = buffer.attrAtScreen(0, 0)
            assertEquals(bufAttr.fg, attr.fg)
            assertEquals(bufAttr.bg, attr.bg)
            assertEquals(bufAttr.style, attr.style)
        }
    }

    class TerminalBufferScrollbackTest {

        @Test
        fun `AtScrollBack returns attribute and char`() {
            val buffer = TerminalBuffer(5, 2)

            buffer.setAttr(CellAttr(fg = TermColor.BrightBlue))
            buffer.writeChar('Z')
            //set to nothing
            buffer.setAttr(CellAttr())
            repeat(2) {
                buffer.writeChar('A')
                buffer.writeChar('B')
                buffer.writeChar('C')
                buffer.writeChar('D')
                buffer.writeChar('E')
            }

            val attr = buffer.attrAtScrollBack(0, 0)
            val ch = buffer.charAtScrollBack(0, 0)

            assertEquals(TermColor.BrightBlue, attr.fg)
            assertEquals(ch, 'Z')
        }
    }


    class TerminalBufferStringTest {

        @Test
        fun `toString screen `() {
            val buffer = TerminalBuffer(5, 2)

            buffer.writeChar('H')
            buffer.writeChar('i')

            val output = buffer.screenToString()

            // it contains nullchars
            assertTrue(output.contains("Hi"))
        }

        @Test
        fun `toString scrollback`() {
            val buffer = TerminalBuffer(5, 2)

            repeat(5) {
                buffer.writeChar('A')
                buffer.writeChar('B')
                buffer.writeChar('C')
                buffer.writeChar('D')
                buffer.writeChar('E')
            }

            val output = buffer.scrollbackToString()

            assertEquals(output, "ABCDE")
        }

        @Test
        fun `toString all`() {
            val buffer = TerminalBuffer(5, 2)

            repeat(5) {
                buffer.writeChar('A')
                buffer.writeChar('B')
                buffer.writeChar('C')
                buffer.writeChar('D')
                buffer.writeChar('E')
            }

            val output = buffer.toString()

            assertEquals(output, "ABCDE\nABCDE\nABCDE")
        }
    }

    class TerminalBufferInsertEmptyLineTest {

        private val nullString = "\u0000"

        @Test
        fun `InsertBlankLine test`() {
            val buf = TerminalBuffer(width = 5, height = 2)

            // Initial content
            buf.writeChar('1')
            buf.writeChar('2')
            buf.writeChar('3')
            buf.writeChar('4')
            buf.writeChar('5')

            buf.writeChar('6')
            buf.writeChar('7')
            buf.writeChar('8')
            buf.writeChar('9')
            buf.writeChar('0')

            buf.insertBlankLine()
            assertEquals("67890", buf.getLineAsString(0))
            assertEquals(nullString.repeat(5), buf.getLineAsString(1))
        }
    }

}

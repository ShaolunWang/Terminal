package org.example

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StyleFlagsTest {

    @Test
    fun `NONE has no styles`() {
        val style = StyleFlags.NONE

        assertFalse(style.bold)
        assertFalse(style.italic)
        assertFalse(style.underline)
        assertEquals(0, style.bits)
    }

    @Test
    fun `add bold`() {
        val style = StyleFlags.NONE + StyleFlags.BOLD

        assertTrue(style.bold)
        assertFalse(style.italic)
        assertFalse(style.underline)
    }

    @Test
    fun `add italic`() {
        val style = StyleFlags.NONE + StyleFlags.ITALIC

        assertFalse(style.bold)
        assertTrue(style.italic)
        assertFalse(style.underline)
    }

    @Test
    fun `add underline`() {
        val style = StyleFlags.NONE + StyleFlags.UNDERLINE

        assertFalse(style.bold)
        assertFalse(style.italic)
        assertTrue(style.underline)
    }

    @Test
    fun `combine multiple flags`() {
        val style = StyleFlags.NONE +
                StyleFlags.BOLD +
                StyleFlags.UNDERLINE

        assertTrue(style.bold)
        assertFalse(style.italic)
        assertTrue(style.underline)
    }

    @Test
    fun `remove flag`() {
        var style = StyleFlags.NONE +
                StyleFlags.BOLD +
                StyleFlags.UNDERLINE

        style -= StyleFlags.BOLD

        assertFalse(style.bold)
        assertFalse(style.italic)
        assertTrue(style.underline)
    }

    @Test
    fun `remove multiple flags`() {
        var style = StyleFlags.NONE +
                StyleFlags.BOLD +
                StyleFlags.ITALIC +
                StyleFlags.UNDERLINE

        style -= StyleFlags.BOLD
        style -= StyleFlags.ITALIC

        assertFalse(style.bold)
        assertFalse(style.italic)
        assertTrue(style.underline)
    }

    @Test
    fun `style operations return new instance`() {
        val original = StyleFlags.NONE
        val modified = original + StyleFlags.BOLD

        assertFalse(original.bold)
        assertTrue(modified.bold)
    }
}


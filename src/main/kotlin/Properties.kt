package org.example


enum class TermColor {
    Black, Red, Green, Yellow, Blue, Magenta, Cyan, White,
    BrightBlack, BrightRed, BrightGreen, BrightYellow, BrightBlue, BrightMagenta, BrightCyan, BrightWhite,
    Default
}

@JvmInline
value class StyleFlags(val bits: Int) {

    val bold: Boolean
        get() {
            return bits and BOLD != 0; }
    val italic: Boolean
        get() {
            return bits and ITALIC != 0; }
    val underline: Boolean
        get() {
            return bits and UNDERLINE != 0; }

    operator fun plus(flag: Int) = StyleFlags(bits or flag)

    // NOTE: minus is basically plus but with flag.inv()
    operator fun minus(flag: Int) = StyleFlags(bits and flag.inv())

    companion object {
        const val BOLD = 0b001
        const val ITALIC = 0b010
        const val UNDERLINE = 0b100
        val NONE = StyleFlags(0b000)
    }
}


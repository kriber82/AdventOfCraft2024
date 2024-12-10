package delivery

object Building {
    fun whichFloor(instructions: String): Int {
        var result = 0

        val inputContainsElf = instructions.contains("🧝")
        val floorChangeOnOpeningParenthesis = floorChangeOpenParen(inputContainsElf)
        val floorChangeOnClosingParenthesise = floorChangeClosingParen(inputContainsElf)

        for (i in instructions.indices) {
            val c = instructions[i]

            val j: Int = when (c) {
                    '(' -> floorChangeOnOpeningParenthesis
                    ')' -> floorChangeOnClosingParenthesise
                    else -> 0
                }
            result += j
        }

        return result
    }

    private fun floorChangeOpenParen(inputContainsElf: Boolean) = if (inputContainsElf) -2 else 1
    private fun floorChangeClosingParen(inputContainsElf: Boolean) = if (inputContainsElf) 3 else -1
}

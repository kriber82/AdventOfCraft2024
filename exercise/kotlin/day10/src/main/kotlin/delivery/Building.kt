package delivery

object Building {
    fun whichFloor(instructions: String): Int {
        var result = 0

        for (i in instructions.indices) {
            val c = instructions[i]

            val inputContainsElf = instructions.contains("🧝")
            val j: Int = when (c) {
                    '(' -> floorChangeOpenParen(inputContainsElf)
                    ')' -> floorChangeClosingParen(inputContainsElf)
                    else -> 0
                }
            result += j
        }

        return result
    }

    private fun floorChangeOpenParen(inputContainsElf: Boolean) = if (inputContainsElf) -2 else 1
    private fun floorChangeClosingParen(inputContainsElf: Boolean) = if (inputContainsElf) 3 else -1
}

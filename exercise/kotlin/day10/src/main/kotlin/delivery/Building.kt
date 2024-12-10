package delivery

object Building {
    fun whichFloor(instructions: String): Int {
        var result = 0

        val inputContainsElf = instructions.contains("🧝")
        val floorChangeOnOpeningParenthesis = if (inputContainsElf) -2 else 1
        val floorChangeOnClosingParenthesise = if (inputContainsElf) 3 else -1

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

}

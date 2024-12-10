package delivery

object Building {
    val floorChangeByCharacterWithElfPresent = mapOf(
        '(' to -2,
        ')' to 3
    )

    val floorChangeByCharacterWithoutElfPresent = mapOf(
        '(' to 1,
        ')' to -1
    )

    fun whichFloor(instructions: String): Int {
        var result = 0

        val floorChangeByCharacter = if (instructions.contains("🧝"))
            floorChangeByCharacterWithElfPresent
        else
            floorChangeByCharacterWithoutElfPresent

        val inputContainsElf = instructions.contains("🧝")
        val floorChangeOnOpeningParenthesis = if (inputContainsElf) -2 else 1
        val floorChangeOnClosingParenthesise = if (inputContainsElf) 3 else -1

        for (i in instructions.indices) {
            val c = instructions[i]

            var j: Int = when (c) {
                '(' -> floorChangeOnOpeningParenthesis
                ')' -> floorChangeOnClosingParenthesise
                else -> 0
            }
            j = floorChangeByCharacter[c] ?: 0
            result += j
        }

        return result
    }

}

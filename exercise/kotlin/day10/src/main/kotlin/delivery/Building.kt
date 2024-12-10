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
        val floorChangeByCharacter = if (instructions.contains("🧝"))
            floorChangeByCharacterWithElfPresent
        else
            floorChangeByCharacterWithoutElfPresent

        var result = 0
        for (char in instructions) {
            val j = floorChangeByCharacter[char] ?: 0
            result += j
        }
        result = instructions
            .map { floorChangeByCharacter[it] ?: 0 }
            .sum()
        return result
    }

}

package delivery

object Building {
    private val floorChangeByCharacterWithElfPresent = mapOf(
        '(' to -2,
        ')' to 3
    )

    private val floorChangeByCharacterWithoutElfPresent = mapOf(
        '(' to 1,
        ')' to -1
    )

    fun whichFloor(instructions: String): Int {
        val floorChangeByCharacter = if (instructions.contains("🧝"))
            floorChangeByCharacterWithElfPresent
        else
            floorChangeByCharacterWithoutElfPresent

        return instructions
            .map { floorChangeByCharacter[it] ?: 0 }
            .sum()
    }

}

package delivery

object Building {
    private val floorChangeByCharacterWithElfPresent = mapOf(
        '(' to -2,
        ')' to 3
    )

    private val floorChangeByCharacterDefault = mapOf(
        '(' to 1,
        ')' to -1
    )

    fun whichFloor(instructions: String): Int {
        val applyElfRules = instructions.contains("🧝")
        val floorChangeByCharacter = when {
            applyElfRules -> floorChangeByCharacterWithElfPresent
            else -> floorChangeByCharacterDefault
        }

        return instructions
            .map { floorChangeByCharacter[it] ?: 0 }
            .sum()
    }

}

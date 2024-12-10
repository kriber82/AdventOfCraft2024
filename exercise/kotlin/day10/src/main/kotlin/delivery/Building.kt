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

        for (i in instructions.indices) {
            val c = instructions[i]
            val j = floorChangeByCharacter[c] ?: 0
            result += j
        }

        return result
    }

}

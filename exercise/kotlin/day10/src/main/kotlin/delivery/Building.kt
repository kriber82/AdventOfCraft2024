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
        for (i in instructions.indices) {
            val c = instructions[i]
            val j = floorChangeByCharacter[c] ?: 0
            result += j
        }
        return result
    }

}

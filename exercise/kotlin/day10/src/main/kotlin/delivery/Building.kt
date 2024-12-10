package delivery

object Building {
    fun whichFloor(instructions: String): Int {
        var result = 0

        for (i in instructions.indices) {
            val c = instructions[i]

            val inputContainsElf = instructions.contains("🧝")
            val j: Int = if (inputContainsElf) {
                when (c) {
                    '(' -> -2
                    ')' -> 3
                    else -> 0
                }
            } else {
                when (c) {
                    '(' -> 1
                    ')' -> -1
                    else -> 0
                }
            }
            result += j
        }

        return result
    }
}

package delivery

object Building {
    fun whichFloor(instructions: String): Int {
        var result = 0

        for (i in instructions.indices) {
            val c = instructions[i]

            val inputContainsElf = instructions.contains("🧝")
            if (inputContainsElf) {
                val j = when (c) {
                    '(' -> -2
                    ')' -> 3
                    else -> 0
                }
                result += j
            } else {
                val j = when (c) {
                    '(' -> 1
                    ')' -> -1
                    else -> 0
                }
                result += j
            }
        }

        return result
    }
}

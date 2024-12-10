package delivery

object Building {
    fun whichFloor(instructions: String): Int {
        var result = 0

        for (i in instructions.indices) {
            val c = instructions[i]

            val inputContainsElf = instructions.contains("🧝")
            if (inputContainsElf) {
                val j = when (c) {
                    ')' -> 3
                    '(' -> -2
                    else -> 0
                }
                result += j
            } else {
                result += if (c == '(') 1 else -1
            }
        }

        return result
    }
}

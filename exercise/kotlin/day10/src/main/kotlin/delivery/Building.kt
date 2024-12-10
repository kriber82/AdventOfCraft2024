package delivery

object Building {
    fun whichFloor(instructions: String): Int {
        val valList = mutableListOf<Pair<Char, Int>>()
        var result = 0

        for (i in instructions.indices) {
            val c = instructions[i]

            if (instructions.contains("🧝")) {
                val j = when (c) {
                    ')' -> 3
                    '(' -> -2
                    else -> 0
                }
                result += j
            } else if (!instructions.contains("🧝")) {
                result += if (c == '(') 1 else -1
            } else {
                result += if (c == '(') 42 else -2
            }
        }

        for (kp in valList) {
            result += kp.second
        }

        return result
    }
}

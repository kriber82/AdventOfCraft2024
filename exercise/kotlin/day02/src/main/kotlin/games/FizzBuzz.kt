package games

import arrow.core.None
import arrow.core.Option
import arrow.core.Some

const val MIN = 1
const val MAX = 100

private val defaultWordsByDivisor: Collection<Pair<Int, String>> = listOf(
    Pair(3, "Fizz"),
    Pair(5, "Buzz"),
    )

class FizzBuzz(private val wordsByDivisor: Collection<Pair<Int, String>> = defaultWordsByDivisor) {
    fun convert(input: Int): Option<String> = when {
        isOutOfRange(input) -> None
        else -> Some(convertSafely(input))
    }

    private fun convertSafely(input: Int): String {
        val concatenatedReplacementWords = wordsByDivisor
            .filter { isDivisible(input, it.first) }
            .map { it.second }
            .reduceOrNull { w1, w2 -> w1 + w2}

        return concatenatedReplacementWords ?: input.toString()
    }

    private fun isDivisible(candidate: Int, by: Int) = candidate % by == 0

    private fun isOutOfRange(input: Int) = input < MIN || input > MAX
}
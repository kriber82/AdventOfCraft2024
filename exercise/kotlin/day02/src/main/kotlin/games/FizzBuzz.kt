package games

import arrow.core.None
import arrow.core.Option
import arrow.core.Some

const val MIN = 1
const val MAX = 100

class FizzBuzzReplacement(public val first: Int, public val second: String)

public val defaultReplacementWordsByDivisor: Collection<FizzBuzzReplacement> = listOf(
    FizzBuzzReplacement(3, "Fizz"),
    FizzBuzzReplacement(5, "Buzz"),
)

public val santaVersionReplacementWordsByDivisor: Collection<FizzBuzzReplacement> =
    defaultReplacementWordsByDivisor +
            listOf(
                FizzBuzzReplacement(7, "Whizz"),
                FizzBuzzReplacement(11, "Bang"),
            )

class FizzBuzz(private val replacementWordsByDivisor: Collection<FizzBuzzReplacement> = defaultReplacementWordsByDivisor) {
    fun convert(input: Int): Option<String> = when {
        isOutOfRange(input) -> None
        else -> Some(convertSafely(input))
    }

    private fun convertSafely(input: Int): String {
        val concatenatedReplacementWords = replacementWordsByDivisor
            .filter { isDivisible(input, it.first) }
            .map { it.second }
            .reduceOrNull { w1, w2 -> w1 + w2 }

        return concatenatedReplacementWords ?: input.toString()
    }

    private fun isDivisible(candidate: Int, by: Int) = candidate % by == 0

    private fun isOutOfRange(input: Int) = input < MIN || input > MAX
}
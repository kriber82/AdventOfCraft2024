package games

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import java.util.Collections

const val MIN = 1
const val MAX = 100
private const val FIZZBUZZ = 15
private const val FIZZ = 3
private const val BUZZ = 5

private val wordsByDivisor: Collection<Pair<Int, String>> = listOf(
    Pair(3, "Fizz"),
    Pair(5, "Buzz"),
    )

object FizzBuzz {
    fun convert(input: Int): Option<String> = when {
        isOutOfRange(input) -> None
        else -> Some(convertSafely(input))
    }

    private fun convertSafely(input: Int): String {
        return when {

            `is`(FIZZBUZZ, input) -> "FizzBuzz"
            `is`(FIZZ, input) -> "Fizz"
            `is`(BUZZ, input) -> "Buzz"
            else -> input.toString()
        }
    }

    private fun `is`(divisor: Int, input: Int): Boolean = input % divisor == 0
    private fun isOutOfRange(input: Int) = input < MIN || input > MAX
}
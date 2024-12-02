import games.FizzBuzz
import games.MAX
import games.MIN
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.exhaustive.ints
import io.kotest.property.forAll

class FizzBuzzProperties : StringSpec({
    "convert returns either given number or combination of replacement words for numbers between 1 and 100" {
        forAll(Exhaustive.ints (MIN..MAX)) { x ->
            FizzBuzz.convert(x).isSome {
                val isParsedInput = it == x.toString()
                // the regex ensures the following: string contains nothing but expected replacement words, replacement words are in correct order, replacement words are not repeated
                val replacementWordsMatchesExpectedRegex = Regex("(Fizz)?(Buzz)?").matches(it)
                val isValidReplacementWord = replacementWordsMatchesExpectedRegex && it.isNotEmpty()
                isParsedInput || isValidReplacementWord
            }
        }
    }

    "parse fail for numbers out of range" {
        forAll(Arb.int().filter { i -> i < MIN || i > MAX }) { x ->
            FizzBuzz.convert(x).isNone()
        }
    }
})
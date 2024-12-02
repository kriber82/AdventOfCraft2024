import games.FizzBuzz
import games.MAX
import games.MIN
import games.santaVersionReplacementWordsByDivisor
import io.kotest.core.spec.style.StringSpec
import io.kotest.property.Arb
import io.kotest.property.PropertyContext
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.forAll

class FizzBuzzProperties : StringSpec({
    "parse fail for numbers out of range" {
        forAll(Arb.int().filter { i -> i < MIN || i > MAX }) { x ->
            FizzBuzz().convert(x).isNone()
        }
    }

    "default replacements: convert returns either given number or combination of replacement words for numbers between 1 and 100" {
        forAll(Arb.int (MIN..MAX),
            checkFizzBuzzReturnsValidAnswersInAllowedRange(FizzBuzz(), "(Fizz)?(Buzz)?"))
    }

    "santa replacements: convert returns either given number or combination of replacement words for numbers between 1 and 100" {
        forAll(Arb.int (MIN..MAX),
            checkFizzBuzzReturnsValidAnswersInAllowedRange(FizzBuzz(santaVersionReplacementWordsByDivisor), "(Fizz)?(Buzz)?(Whizz)?(Bang)?"))
    }

})

private fun checkFizzBuzzReturnsValidAnswersInAllowedRange(
    tested: FizzBuzz,
    validReplacementsRegex: String
): suspend PropertyContext.(Int) -> Boolean = { x ->
    tested.convert(x).isSome {
        val isParsedInput = it == x.toString()
        // the regex ensures the following: string contains nothing but expected replacement words, replacement words are in correct order, replacement words are not repeated
        val replacementWordsMatchesExpectedRegex = Regex(validReplacementsRegex).matches(it)
        val isValidReplacementWord = replacementWordsMatchesExpectedRegex && it.isNotEmpty()
        isParsedInput || isValidReplacementWord
    }
}
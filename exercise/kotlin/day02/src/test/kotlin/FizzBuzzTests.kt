import games.FizzBuzz
import games.santaVersionReplacementWordsByDivisor
import io.kotest.assertions.arrow.core.shouldBeNone
import io.kotest.assertions.arrow.core.shouldBeSome
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData

class FizzBuzzTests : FunSpec({
    context("default variant: returns its numbers representation") {
        withData(
            ValidInput(1, "1"),
            ValidInput(67, "67"),
            ValidInput(82, "82"),
            ValidInput(3, "Fizz"),
            ValidInput(6, "Fizz"),
            ValidInput(5, "Buzz"),
            ValidInput(50, "Buzz"),
            ValidInput(15, "FizzBuzz"),
            ValidInput(30, "FizzBuzz"),
            ValidInput(7, "Whizz"),
            ValidInput(21, "FizzWhizz"),
            ValidInput(42, "FizzWhizz"),
            ValidInput(35, "BuzzWhizz"),
            ValidInput(11, "Bang"),
            ValidInput(33, "FizzBang"),
            ValidInput(55, "BuzzBang"),
            ValidInput(77, "WhizzBang"),
        ) { (input, expectedResult) ->
            FizzBuzz(santaVersionReplacementWordsByDivisor).convert(input).shouldBeSome(expectedResult)
        }
    }

    context("santa variant: returns its numbers representation") {
        withData(
            ValidInput(1, "1"),
            ValidInput(67, "67"),
            ValidInput(82, "82"),
            ValidInput(3, "Fizz"),
            ValidInput(66, "Fizz"),
            ValidInput(99, "Fizz"),
            ValidInput(5, "Buzz"),
            ValidInput(50, "Buzz"),
            ValidInput(85, "Buzz"),
            ValidInput(15, "FizzBuzz"),
            ValidInput(30, "FizzBuzz"),
            ValidInput(45, "FizzBuzz")
        ) { (input, expectedResult) ->
            FizzBuzz().convert(input).shouldBeSome(expectedResult)
        }
    }

    context("fails for numbers out of range") {
        withData(0, -1, 101) { x ->
            FizzBuzz().convert(x).shouldBeNone()
        }
    }
})

data class ValidInput(val input: Int, val expectedResult: String)
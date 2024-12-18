package eid

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure

class EID private constructor(
    val eid: String,
    val gender: ElfGender,
    val year: Int,
    val serialNumber: Int,
    val controlKey: Int
) {


    companion object {
        private const val VALID_EID_LENGTH = 8

        fun parse(eidCandidate: String): Either<ParsingError, EID> {
            return either {
                ensureHasValidLength(eidCandidate)

                val gender = parseGender(eidCandidate).bind()
                val year = parseYear(eidCandidate).bind()
                val serialNumber = parseSerialNumber(eidCandidate).bind()
                val parsedControlKey = parseControlKey(eidCandidate).bind()

                ensureControlKeyMatchesContent(eidCandidate, parsedControlKey)

                EID(eidCandidate, gender, year, serialNumber, parsedControlKey)
            }
        }

        private fun Raise<ParsingError>.ensureHasValidLength(eidCandidate: String) {
            ensure(eidCandidate.length >= VALID_EID_LENGTH) {
                ParsingError.InputTooShort()
            }
            ensure(eidCandidate.length <= VALID_EID_LENGTH) {
                ParsingError.InputTooLong()
            }
        }

        private fun parseGender(eidCandidate: String): Either<ParsingError, ElfGender> {
            return when (val genderSubstring = eidCandidate.substring(0, 1)) {
                "1" -> Either.Right(ElfGender.Sloubi)
                "2" -> Either.Right(ElfGender.Gagna)
                "3" -> Either.Right(ElfGender.Catact)
                else -> Either.Left(ParsingError.InvalidElfGender(genderSubstring))
            }
        }

        private fun parseYear(eidCandidate: String): Either<ParsingError, Int> {
            return parseIntFieldWithErrorProvider(
                eidCandidate.substring(1..2),
                0..99
            ) { ParsingError.InvalidYear(it) }
        }

        private fun parseSerialNumber(eidCandidate: String): Either<ParsingError, Int> {
            return parseIntFieldWithErrorProvider(
                eidCandidate.substring(3..5),
                1..999
            ) { ParsingError.InvalidSerialNumber(it) }
        }

        private fun parseControlKey(eidCandidate: String): Either<ParsingError, Int> {
            return parseIntFieldWithErrorProvider(
                eidCandidate.substring(6..7),
                1..97
            ) { ParsingError.InvalidControlKey(it) }
        }

        private fun Raise<ParsingError>.ensureControlKeyMatchesContent(
            eidCandidate: String,
            parsedControlKey: Int
        ) {
            val calculatedControlKey = calculateControlKey(eidCandidate).bind()
            ensure(parsedControlKey == calculatedControlKey) {
                ParsingError.ControlDoesNotMatch(calculatedControlKey, parsedControlKey)
            }
        }

        private fun containsOnlyDigits(yearCandidate: String) = yearCandidate.matches("[0-9]+".toRegex())

        private fun parseIntFieldWithErrorProvider(
            eidSubstring: String,
            validRange: IntRange,
            errorProvider: (String) -> ParsingError
        ): Either<ParsingError, Int> {
            return either {
                ensure(containsOnlyDigits(eidSubstring)) {
                    errorProvider(eidSubstring)
                }
                val parsedField = Either
                    .catch { eidSubstring.toInt() }
                    .mapLeft { errorProvider(eidSubstring) }
                    .bind()
                ensure(parsedField in validRange) {
                    errorProvider(eidSubstring)
                }
                parsedField
            }
        }

        fun calculateControlKey(eidCandidate: String): Either<ParsingError, Int> {
            return either {
                val firstSixDigits = eidCandidate.substring(0..5)
                val firstSixDigitsAsNumber = Either
                    .catch { firstSixDigits.toInt() }
                    .mapLeft { ParsingError.CouldNotCalculateControlKey(firstSixDigits) }
                    .bind()
                val modulo97 = firstSixDigitsAsNumber % 97
                val complementTo97 = 97 - modulo97
                complementTo97
            }
        }

    }

    override fun toString(): String {
        return eid
    }

}

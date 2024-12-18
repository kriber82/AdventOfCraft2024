package eid

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

class EID private constructor(val eid: String, val gender: ElfGender, val year: Int, val serialNumber: Int) {

    companion object {
        private const val VALID_EID_LENGTH = 8

        fun parse(eidCandidate: String): Either<ParsingError, EID> {
            return either {
                ensure(eidCandidate.length >= VALID_EID_LENGTH) {
                    ParsingError.InputTooShort()
                }
                ensure(eidCandidate.length <= VALID_EID_LENGTH) {
                    ParsingError.InputTooLong()
                }

                val gender = parseGender(eidCandidate).bind()
                val year = parseYear(eidCandidate).bind()
                val serialNumber = parseSerialNumber(eidCandidate).bind()

                val calculateControlKey = calculateControlKey(eidCandidate).bind()
                val parsedControlKey = parseControlKey(eidCandidate).bind()
                ensure(parsedControlKey == calculateControlKey) {
                    ParsingError.ControlDoesNotMatch(calculateControlKey, parsedControlKey)
                }

                EID(eidCandidate, gender, year, serialNumber)
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
                0
            ) { ParsingError.InvalidYear(it) }
        }

        private fun parseSerialNumber(eidCandidate: String): Either<ParsingError, Int> {
            return parseIntFieldWithErrorProvider(
                eidCandidate.substring(3..5),
                1
            ) { ParsingError.InvalidSerialNumber(it) }
        }

        private fun parseControlKey(eidCandidate: String): Either<ParsingError, Int> {
            return parseIntFieldWithErrorProvider(
                eidCandidate.substring(6..7),
                1 //TODO upper bound
            ) { ParsingError.InvalidControlKey(it) }
        }

        private fun containsOnlyDigits(yearCandidate: String) = yearCandidate.matches("[0-9]+".toRegex())

        private fun parseIntFieldWithErrorProvider(
            eidSubstring: String,
            minValue: Int,
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
                ensure(parsedField >= minValue) {
                    errorProvider(eidSubstring)
                }
                parsedField
            }
        }

        fun calculateControlKey(eidCandidate: String): Either<ParsingError, Int> {
            //control key = complement to 97 of the number formed by the first 6 digits of the EID modulo 97
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

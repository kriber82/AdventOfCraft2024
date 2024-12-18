package eid

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

class EID private constructor(val eid: String, val gender: ElfGender, val year: Int, val serialNumber: Int) {

    companion object {
        const val validEidLength = 8

        fun parse(eidCandidate: String): Either<ParsingError, EID> {
            return either {
                ensure(eidCandidate.length >= validEidLength) {
                    ParsingError.InputTooShort()
                }
                ensure(eidCandidate.length <= validEidLength) {
                    ParsingError.InputTooLong()
                }

                val gender = parseGender(eidCandidate).bind()
                val year = parseYear(eidCandidate).bind()
                val serialNumber = parseSerialNumber(eidCandidate).bind()

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

    }

    override fun toString(): String {
        return eid
    }

}

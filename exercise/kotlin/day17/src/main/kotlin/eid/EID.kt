package eid

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.raise.catch
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
            return either {
                val yearCandidate = eidCandidate.substring(1, 3)
                ensure(containsOnlyDigits(yearCandidate)) {
                    ParsingError.InvalidYear(yearCandidate)
                }
                val parsedYear = Either
                    .catch { yearCandidate.toInt() }
                    .mapLeft { ParsingError.InvalidYear(yearCandidate) }
                    .bind()
                ensure(parsedYear >= 0) {
                    ParsingError.InvalidYear(yearCandidate)
                }
                parsedYear
            }
        }

        private fun parseSerialNumber(eidCandidate: String): Either<ParsingError, Int> {
            return either {
                val serialCandidate = eidCandidate.substring(3, 6)
                ensure(containsOnlyDigits(serialCandidate)) {
                    ParsingError.InvalidSerialNumber(serialCandidate)
                }
                val parsedSerial = Either
                    .catch { serialCandidate.toInt() }
                    .mapLeft { ParsingError.InvalidSerialNumber(serialCandidate) }
                    .bind()
                ensure(parsedSerial >= 1) {
                    ParsingError.InvalidSerialNumber(serialCandidate)
                }
                parsedSerial
            }
        }

        private fun containsOnlyDigits(yearCandidate: String) = yearCandidate.matches("[0-9]+".toRegex())

    }

    override fun toString(): String {
        return eid
    }

}

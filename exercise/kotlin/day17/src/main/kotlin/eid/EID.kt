package eid

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

class EID private constructor (val eid: String, val gender: ElfGender, val year: Int, val serialNumber: Int) {

    companion object {
        const val validEidLength = 8

        fun parse(eidCandidate: String): Either<ParsingError, EID> {
            return either {
                ensure (eidCandidate.length >= validEidLength) {
                    ParsingError.InputTooShort()
                }
                ensure (eidCandidate.length <= validEidLength) {
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
            val yearCandidate = eidCandidate.substring(1, 3)
            if (!yearCandidate.matches("[0-9]+".toRegex())) {
                return Either.Left(ParsingError.InvalidYear(yearCandidate))
            }
            return Either.catch {
                val parsedYear = yearCandidate.toInt()
                return if (parsedYear < 0) {
                    Either.Left(ParsingError.InvalidYear(yearCandidate))
                } else {
                    Either.Right(parsedYear)
                }
            }.mapLeft {
                ParsingError.InvalidYear(yearCandidate)
            }
        }

        private fun parseSerialNumber(eidCandidate: String): Either<ParsingError, Int> {
            val serialCandidate = eidCandidate.substring(3, 6)
            if (!serialCandidate.matches("[0-9]+".toRegex())) {
                return Either.Left(ParsingError.InvalidSerialNumber(serialCandidate))
            }
            return Either.catch {
                val parsedSerial = serialCandidate.toInt()
                return if (parsedSerial < 1) {
                    Either.Left(ParsingError.InvalidSerialNumber(serialCandidate))
                } else {
                    Either.Right(parsedSerial)
                }
            }.mapLeft {
                ParsingError.InvalidSerialNumber(serialCandidate)
            }
        }

    }

    override fun toString(): String {
        return eid
    }

}

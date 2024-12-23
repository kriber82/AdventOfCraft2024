package eid

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right

class EID private constructor(
    val gender: ElfGender,
    val year: Int,
    val serialNumber: Int,
) {

    private val payload: String
    val controlKey: Int

    init {
        payload = "${gender.intValue}${year.toZeroPaddedString(2)}${serialNumber.toZeroPaddedString(3)}"
        controlKey = calculateControlKey(payload).getOrNull()!!
    }

    companion object {
        private const val VALID_EID_LENGTH = 8

        fun parse(eidCandidate: String): Either<ParsingError, EID> {
            return either {
                ensureHasValidLength(eidCandidate)

                EID(
                    gender = parseGender(eidCandidate).bind(),
                    year = parseYear(eidCandidate).bind(),
                    serialNumber = parseSerialNumber(eidCandidate).bind()
                ).also {
                    ensureControlKeyIsOk(eidCandidate)
                }
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
                "1" -> ElfGender.Sloubi.right()
                "2" -> ElfGender.Gagna.right()
                "3" -> ElfGender.Catact.right()
                else -> ParsingError.InvalidElfGender(genderSubstring).left()
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

        private fun Raise<ParsingError>.ensureControlKeyIsOk(
            eidCandidate: String
        ) {
            val providedControlKey = parseControlKey(eidCandidate).bind()
            val calculatedControlKey = calculateControlKey(eidCandidate).bind()
            ensure(providedControlKey == calculatedControlKey) {
                ParsingError.ControlKeyDoesNotMatch(calculatedControlKey, providedControlKey)
            }
        }

        private fun parseIntFieldWithErrorProvider(
            eidSubstring: String,
            validRange: IntRange,
            createErrorForEidSubstring: (String) -> ParsingError
        ): Either<ParsingError, Int> {
            return either {
                ensure(containsOnlyDigits(eidSubstring)) {
                    createErrorForEidSubstring(eidSubstring)
                }
                val parsedField = Either
                    .catch { eidSubstring.toInt() }
                    .mapLeft { createErrorForEidSubstring(eidSubstring) }
                    .bind()
                ensure(parsedField in validRange) {
                    createErrorForEidSubstring(eidSubstring)
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
                ensure(firstSixDigitsAsNumber >= 0) {
                    ParsingError.CouldNotCalculateControlKey(firstSixDigits)
                }
                val modulo97 = firstSixDigitsAsNumber % 97
                val complementTo97 = 97 - modulo97
                complementTo97
            }
        }

        private fun containsOnlyDigits(str: String) = str.matches("[0-9]+".toRegex())

    }

    override fun toString(): String {
        return "$payload${controlKey.toZeroPaddedString(2)}"
    }

}

fun Int.toZeroPaddedString(length: Int) = toString().padStart(length, '0')

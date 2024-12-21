package eid

sealed interface ParsingError {
    val message: String

    data class InputTooShort(override val message: String = "Input is too short for an EID (must be 8 characters long)") : ParsingError
    data class InputTooLong(override val message: String = "Input is too long for an EID (must be 8 characters long)") : ParsingError

    data class InvalidElfGender(private val genderSubstring: String): ParsingError {
        override val message = "$genderSubstring dose not represent a valid gender (must be 1, 2 or 3)"
    }

    data class InvalidYear(private val yearSubstring: String): ParsingError {
        override val message = "$yearSubstring dose not represent a valid year (must be between 00 and 99)"
    }

    class InvalidSerialNumber(serialSubstring: String) : ParsingError {
        override val message = "$serialSubstring dose not represent a valid serial number (must be between 000 and 999)"
    }

    class CouldNotCalculateControlKey(firstSixDigits: String) : ParsingError {
        override val message = "Cannot calculate control key from $firstSixDigits (must be a number between 000000 to 999999)"
    }

    class ControlKeyDoesNotMatch(calculatedControlKey: Int, parsedControlKey: Int): ParsingError {
        override val message = "Control key $parsedControlKey does not match calculated control key $calculatedControlKey"
    }

    class InvalidControlKey(controlKeySubstring: String) : ParsingError {
        override val message = "$controlKeySubstring does not represent a valid control key (must be between 01 and 97)"
    }

}

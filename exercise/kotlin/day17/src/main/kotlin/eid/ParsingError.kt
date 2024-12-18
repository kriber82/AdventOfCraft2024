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
}

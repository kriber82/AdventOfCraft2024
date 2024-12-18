package eid

sealed interface ParsingError {
    val message: String

    data class InputTooShort(override val message: String = "Input is too short for an EID (must be 8 characters long)") : ParsingError
}

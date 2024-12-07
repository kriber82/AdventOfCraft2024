class EID(val elfIdentifier: Int) {
    fun getPayloadDigits(): Int { // TODO fix primitive obsession?
        return elfIdentifier.toString().slice(0..5).toInt()
    }

    fun getControlDigits(): Int {
        return elfIdentifier.toString().slice(6..7).toInt()
    }

    fun getGenderPart(): Int {
        return elfIdentifier.toString().slice(0..0).toInt()
    }

    override fun toString(): String {
        return elfIdentifier.toString()
    }

    override fun equals(other: Any?): Boolean {
        return other is EID && other.elfIdentifier == elfIdentifier
    }

    companion object {
        fun fromParts(singleDigitSex: Int, twoDigitYear: Int, threeDigitsSerialNumber: Int, twoDigitControlKey: Int): EID {
            val sexString = singleDigitSex.toString()
            val yearString = twoDigitYear.toString().padStart(2, '0')
            val serialNumberString = threeDigitsSerialNumber.toString().padStart(3, '0')
            val controlKeyString = twoDigitControlKey.toString().padStart(2, '0')
            val eidString = sexString + yearString + serialNumberString + controlKeyString
            return EID(eidString.toInt())
        }
    }
}

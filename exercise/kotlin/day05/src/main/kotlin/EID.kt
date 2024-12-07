
class EID(elfIdentifier: Int) {
    val payload: EidPayload = EidPayload(elfIdentifier.toString().slice(0..5).toInt())
    val controlKey: Int = elfIdentifier.toString().slice(6..7).toInt()

    fun getPayloadDigits(): Int {
        return payload.payload
    }

    fun getControlDigits(): Int {
        return controlKey
    }

    override fun toString(): String {
        return payload.toString() + controlKey.toString().padStart(2, '0')
    }

    override fun equals(other: Any?): Boolean {
        return other is EID && other.payload == payload && other.controlKey == controlKey
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

class EidPayload(val payload: Int) {
    /* TODO evaluate Copilot suggestion
    init {
        require(payload in 0..999999) { "Payload must be a six-digit number" }
    }
     */

    fun getGenderPart(): Int {
        return payload.toString().slice(0..0).toInt()
    }

    override fun toString(): String {
        return payload.toString().padStart(6, '0')
    }

    override fun equals(other: Any?): Boolean {
        return other is EidPayload && other.payload == payload
    }

    override fun hashCode(): Int {
        return payload
    }

}
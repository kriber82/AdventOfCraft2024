class EID(val payload: EidPayload, val controlKey: EidControlKey) {

    override fun toString(): String {
        return payload.toString() + controlKey.toString().padStart(2, '0')
    }

    override fun equals(other: Any?): Boolean {
        return other is EID && other.payload == payload && other.controlKey == controlKey
    }

    companion object {
        fun fromCompleteIdentifier(elfIdentifier: Int): EID {
            val payload = EidPayload(elfIdentifier.toString().slice(0..5).toInt())
            val controlKey = EidControlKey(elfIdentifier.toString().slice(6..7).toInt())
            return EID(payload, controlKey)
        }

        fun fromParts(
            singleDigitSex: Int,
            twoDigitYear: Int,
            threeDigitsSerialNumber: Int,
            twoDigitControlKey: Int? = null
        ): EID {
            val payload = EidPayload.fromParts(singleDigitSex, twoDigitYear, threeDigitsSerialNumber)
            val controlKey = twoDigitControlKey?.let { EidControlKey(it) } ?: payload.computeValidControlKey()
            return EID(payload, controlKey)
        }
    }
}

class EidPayload(val payload: Int) {
    /* TODO evaluate this Copilot suggestion
    init {
        require(payload in 0..999999) { "Payload must be a six-digit number" }
    }
     */

    fun getGenderPart(): Int {
        return payload.toString().slice(0..0).toInt()
    }

    fun computeValidControlKey(): EidControlKey {
        val payloadModulo = payload % 97
        val complementOfPayloadModulo = 97 - payloadModulo
        return EidControlKey(complementOfPayloadModulo)
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

    companion object {
        fun fromParts(singleDigitSex: Int, twoDigitYear: Int, threeDigitsSerialNumber: Int): EidPayload {
            val sexString = singleDigitSex.toString()
            val yearString = twoDigitYear.toString().padStart(2, '0')
            val serialNumberString = threeDigitsSerialNumber.toString().padStart(3, '0')
            val payloadString = sexString + yearString + serialNumberString
            return EidPayload(payloadString.toInt())
        }
    }
}

class EidControlKey(val controlKey: Int) {
    /* TODO evaluate this Copilot suggestion
    init {
        require(controlKey in 0..99) { "Control key must be a two-digit number" }
    }
     */

    override fun toString(): String {
        return controlKey.toString().padStart(2, '0')
    }

    override fun equals(other: Any?): Boolean {
        return other is EidControlKey && other.controlKey == controlKey
    }

    override fun hashCode(): Int {
        return controlKey
    }

}
class EID(val payload: EidPayload, val controlKey: EidControlKey) {

    override fun toString(): String {
        return payload.toString() + controlKey.toString().padStart(2, '0')
    }

    override fun equals(other: Any?): Boolean {
        return other is EID && other.payload == payload && other.controlKey == controlKey
    }

    companion object {
        fun fromCompleteIdentifier(elfIdentifier: Int): EID {
            val eidString = elfIdentifier.toString()
            val payload = EidPayload(
                EidSex(eidString.slice(0..0).toInt()),
                EidBirthYear(eidString.slice(1..2).toInt()),
                EidSerialNumber(eidString.slice(3..5).toInt()))
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

class EidPayload(val sex: EidSex, val birthYear: EidBirthYear, val serialNumber: EidSerialNumber) {
    val payload = (sex.toString() + birthYear.toString() + serialNumber.toString()).toInt()

    /* TODO evaluate this Copilot suggestion
    init {
        require(payload in 0..999999) { "Payload must be a six-digit number" }
    }
     */

    fun getGenderPart(): Int {
        return sex.key
    }

    fun computeValidControlKey(): EidControlKey {
        val payloadModulo = payload % 97
        val complementOfPayloadModulo = 97 - payloadModulo
        return EidControlKey(complementOfPayloadModulo)
    }

    override fun toString(): String {
        return sex.toString() + birthYear.toString() + serialNumber.toString()
    }

    override fun equals(other: Any?): Boolean {
        return other is EidPayload && other.sex == sex && other.birthYear == birthYear && other.serialNumber == serialNumber
    }

    override fun hashCode(): Int {
        return sex.key + birthYear.year + serialNumber.birthOrder
    }

    companion object {
        fun fromParts(singleDigitSex: Int, twoDigitYear: Int, threeDigitsSerialNumber: Int): EidPayload {
            return EidPayload(EidSex(singleDigitSex), EidBirthYear(twoDigitYear), EidSerialNumber(threeDigitsSerialNumber))
        }
    }
}

class EidSex(val key: Int) {
    override fun toString(): String {
        return key.toString()
    }

    override fun equals(other: Any?): Boolean {
        return other is EidSex && other.key == key
    }

    override fun hashCode(): Int {
        return key
    }
}

class EidBirthYear(val year: Int) {
    override fun toString(): String {
        return year.toString().padStart(2, '0')
    }

    override fun equals(other: Any?): Boolean {
        return other is EidBirthYear && other.year == year
    }

    override fun hashCode(): Int {
        return year
    }
}

class EidSerialNumber(val birthOrder: Int) {
    override fun toString(): String {
        return birthOrder.toString().padStart(3, '0')
    }

    override fun equals(other: Any?): Boolean {
        return other is EidSerialNumber && other.birthOrder == birthOrder
    }

    override fun hashCode(): Int {
        return birthOrder
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
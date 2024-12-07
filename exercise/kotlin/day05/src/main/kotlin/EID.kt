class EID(val payload: EidPayload, val controlKey: EidControlKey) {

    override fun toString(): String {
        return "$payload $controlKey"
    }

    override fun equals(other: Any?): Boolean {
        return other is EID && other.payload == payload && other.controlKey == controlKey
    }

    companion object {
        fun fromCompleteIdentifier(elfIdentifier: UInt): EID {
            val eidString = elfIdentifier.toString()
            val payload = EidPayload(
                EidSex(eidString.slice(0..0).toUInt()),
                EidBirthYear(eidString.slice(1..2).toUInt()),
                EidSerialNumber(eidString.slice(3..5).toUInt()))
            val controlKey = EidControlKey(elfIdentifier.toString().slice(6..7).toUInt())
            return EID(payload, controlKey)
        }

        fun fromParts(
            singleDigitSex: UInt,
            twoDigitYear: UInt,
            threeDigitsSerialNumber: UInt,
            twoDigitControlKey: UInt? = null
        ): EID {
            val payload = EidPayload.fromParts(singleDigitSex, twoDigitYear, threeDigitsSerialNumber)
            val controlKey = twoDigitControlKey?.let { EidControlKey(it) } ?: payload.computeValidControlKey()
            return EID(payload, controlKey)
        }
    }
}

class EidPayload(val sex: EidSex, val birthYear: EidBirthYear, val serialNumber: EidSerialNumber) {

    /* TODO evaluate this Copilot suggestion
    init {
        require(payload in 0..999999) { "Payload must be a six-digit number" }
    }
     */

    fun computeValidControlKey(): EidControlKey {
        val payload = (sex.toString() + birthYear.toString() + serialNumber.toString()).toUInt()
        val payloadModulo = payload % 97U
        val complementOfPayloadModulo = 97U - payloadModulo
        return EidControlKey(complementOfPayloadModulo)
    }

    override fun toString(): String {
        return "$sex $birthYear $serialNumber"
    }

    override fun equals(other: Any?): Boolean {
        return other is EidPayload && other.sex == sex && other.birthYear == birthYear && other.serialNumber == serialNumber
    }

    override fun hashCode(): Int {
        return sex.hashCode() + birthYear.hashCode() + serialNumber.hashCode()
    }

    companion object {
        fun fromParts(singleDigitSex: UInt, twoDigitYear: UInt, threeDigitsSerialNumber: UInt): EidPayload {
            return EidPayload(EidSex(singleDigitSex), EidBirthYear(twoDigitYear), EidSerialNumber(threeDigitsSerialNumber))
        }
    }
}

class EidSex(val key: UInt) {
    override fun toString(): String {
        return key.toString()
    }

    override fun equals(other: Any?): Boolean {
        return other is EidSex && other.key == key
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}

class EidBirthYear(val year: UInt) {
    override fun toString(): String {
        return year.toString().padStart(2, '0')
    }

    override fun equals(other: Any?): Boolean {
        return other is EidBirthYear && other.year == year
    }

    override fun hashCode(): Int {
        return year.hashCode()
    }
}

class EidSerialNumber(val birthOrder: UInt) {
    override fun toString(): String {
        return birthOrder.toString().padStart(3, '0')
    }

    override fun equals(other: Any?): Boolean {
        return other is EidSerialNumber && other.birthOrder == birthOrder
    }

    override fun hashCode(): Int {
        return birthOrder.hashCode()
    }
}

class EidControlKey(val controlKey: UInt) {
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
        return controlKey.hashCode()
    }

}
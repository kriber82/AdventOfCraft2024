class EidValidator {
    companion object {
        fun isValid(eid: EID): Boolean {
            if (eid.getGenderPart() > 3) {
                return false
            }

            return eid.getControlDigits() == computeValidControlKey(eid)
        }

        fun computeValidControlKey(eid: EID): Int { // TODO should parameter be eid payload?
            val payload = eid.getPayloadDigits()
            val payloadModulo = payload % 97
            val complementOfPayloadModulo = 97 - payloadModulo
            return complementOfPayloadModulo
        }
    }
}

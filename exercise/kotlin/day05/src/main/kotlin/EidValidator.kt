class EidValidator {
    companion object {
        fun isValid(eid: EID): Boolean {
            return true
        }

        fun computeValidControlKey(eidPayloadDigits: Int): Int {
            return 67
        }
    }
}

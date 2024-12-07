class EidValidator {
    companion object {
        fun isValid(eid: EID): Boolean {
            if (eid.payload.sex.key > 3U) {
                return false
            }
            if (eid.payload.sex.key == 0U) {
                return false
            }

            if (eid.payload.birthYear.year > 99U) {
                return false
            }
            if (eid.payload.birthYear.year < 0U) {
                return false
            }

            return eid.controlKey == eid.payload.computeValidControlKey()
        }

    }
}

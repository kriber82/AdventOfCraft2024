class EidValidator {
    companion object {
        fun isValid(eid: EID): Boolean {
            if (eid.payload.getGenderPart() > 3) {
                return false
            }
            if (eid.payload.getGenderPart() == 0) {
                return false
            }

            if (eid.payload.birthYear.year > 99) {
                return false
            }
            if (eid.payload.birthYear.year < 0) {
                return false
            }

            return eid.controlKey == eid.payload.computeValidControlKey()
        }

    }
}

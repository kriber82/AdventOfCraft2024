class EidValidator {
    companion object {
        fun isValid(eid: EID): Boolean {
            if (eid.payload.getGenderPart() > 3) {
                return false
            }

            return eid.getControlDigits() == eid.payload.computeValidControlKey()
        }

    }
}

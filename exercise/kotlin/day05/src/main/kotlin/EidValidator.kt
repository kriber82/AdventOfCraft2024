class EidValidator {
    companion object {
        fun isValid(eid: EID): Boolean {
            return isPayloadValid(eid.payload) && isControlKeyValid(eid)
        }

        private fun isPayloadValid(eidPayload: EidPayload): Boolean {
            return isSexValid(eidPayload.sex) &&
                    isBirthYearValid(eidPayload.birthYear) &&
                    isSerialNumberValid(eidPayload.serialNumber)
        }

        private fun isSerialNumberValid(eidSerialNumber: EidSerialNumber): Boolean {
            if (eidSerialNumber.birthOrder > 999u) {
                return false
            }

            if (eidSerialNumber.birthOrder == 0u) {
                return false
            }

            return true
        }

        private fun isBirthYearValid(eidBirthYear: EidBirthYear): Boolean {
            if (eidBirthYear.year > 99u) {
                return false
            }

            return true
        }

        private fun isSexValid(eidSex: EidSex): Boolean {
            if (eidSex.key > 3U) {
                return false
            }
            if (eidSex.key == 0U) {
                return false
            }

            return true
        }

        private fun isControlKeyValid(eid: EID) = eid.controlKey == eid.payload.computeValidControlKey()

    }
}

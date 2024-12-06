class EID(val elfIdentifier: Int) {
    fun getPayloadDigits(): Int { // TODO fix primitive obsession?
        return elfIdentifier.toString().slice(0..5).toInt()
    }

    fun getControlDigits(): Int {
        return elfIdentifier.toString().slice(6..7).toInt()
    }
}

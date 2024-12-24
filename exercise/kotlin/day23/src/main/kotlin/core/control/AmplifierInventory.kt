package core.control

class AmplifierInventory(val divineAmplifiers: Int, val blessedAmplifiers: Int) {
    private var usedDivineAmplifiers = 0
    private var usedBlessedAmplifiers = 0

    val divineAmplifiersLeft: Int
        get() = divineAmplifiers - usedDivineAmplifiers

    val blessedAmplifiersLeft: Int
        get() = blessedAmplifiers - usedBlessedAmplifiers

    fun takeBestAvailableAmplifier(): AmplifierType {
        return when {
            divineAmplifiersLeft > 0 -> {
                usedDivineAmplifiers++
                AmplifierType.DIVINE
            }
            blessedAmplifiersLeft > 0 -> {
                usedBlessedAmplifiers++
                AmplifierType.BLESSED
            }
            else -> AmplifierType.BASIC
        }
    }
}

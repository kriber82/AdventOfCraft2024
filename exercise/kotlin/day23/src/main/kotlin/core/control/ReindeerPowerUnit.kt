package core.control

import external.deer.Reindeer

class ReindeerPowerUnit(
    val reindeer: Reindeer,
    internal val amplifier: MagicPowerAmplifier = MagicPowerAmplifier(AmplifierType.BASIC)) {

    fun harnessMagicPower(): Float {
        return if (!reindeer.needsRest()) {
            reindeer.timesHarnessing++
            amplifier.amplify(reindeer.magicPower)
        } else {
            0f
        }
    }

    fun checkMagicPower(): Float =
        if (reindeer.sick || reindeer.needsRest())
            0.0f
        else
            amplifier.amplify(reindeer.magicPower)
}

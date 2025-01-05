package core.control

import adapters.stable.ReindeersFromMagicStable
import external.deer.Reindeer
import external.stable.MagicStable

class ControlSystem(
    private val magicStable: ForGettingReindeer = ReindeersFromMagicStable(MagicStable()),
    private val amplifierInventory: AmplifierInventory = AmplifierInventory(2, 1))
{
    //The Xmas spirit is 40 magic power unit
    private val xmasSpirit = 40
    private val dashboard = Dashboard()
    internal val reindeerPowerUnits = bringAllReindeers()
    var status: SleighEngineStatus = SleighEngineStatus.OFF
    var action: SleighAction = SleighAction.PARKED

    private fun bringAllReindeers(): List<ReindeerPowerUnit> {
            val result = mutableListOf<ReindeerPowerUnit>()

            val reindeerBySpirit = magicStable.allReindeers
                .filter { !it.sick }
                .sortedByDescending { it.magicPower }

            for (reindeerWithHighestSpirit in reindeerBySpirit) {
                val bestAmplifier = MagicPowerAmplifier(amplifierInventory.takeBestAvailableAmplifier())
                result.add(ReindeerPowerUnit(reindeerWithHighestSpirit, bestAmplifier))
            }

            return result
    }

    fun startSystem() {
        dashboard.displayStatus("Starting the sleigh...")

        status = SleighEngineStatus.ON
        dashboard.displayStatus("System ready.")
    }

    @Throws(ReindeersNeedRestException::class, SleighNotStartedException::class)
    fun ascend() {
        var controlMagicPower = 0f
        if (status == SleighEngineStatus.ON) {
            //debugOutputPowerDiagnostics(controlMagicPower)

            for (reindeerPowerUnit in reindeerPowerUnits) {
                controlMagicPower += reindeerPowerUnit.harnessMagicPower()
            }

            if (checkReindeerStatus(controlMagicPower)) {
                dashboard.displayStatus("Ascending...")
                action = SleighAction.FLYING
                controlMagicPower = 0f
            } else throw ReindeersNeedRestException()
        } else {
            throw SleighNotStartedException()
        }
    }

    private fun debugOutputPowerDiagnostics(controlMagicPower: Float) {
        val reindeerPower = checkAvailablePower()
        dashboard.displayStatus("Reindeer power available: $reindeerPower")
    }

    @Throws(SleighNotStartedException::class)
    fun descend() {
        if (status == SleighEngineStatus.ON) {
            dashboard.displayStatus("Descending...")
            action = SleighAction.HOVERING
        } else throw SleighNotStartedException()
    }

    @Throws(SleighNotStartedException::class)
    fun park() {
        if (status == SleighEngineStatus.ON) {
            dashboard.displayStatus("Parking...")

            for (reindeerPowerUnit in reindeerPowerUnits) {
                // The reindeer rests so the times to harness his magic power resets
                reindeerPowerUnit.reindeer.timesHarnessing = 0
            }

            action = SleighAction.PARKED
        } else throw SleighNotStartedException()
    }

    fun stopSystem() {
        dashboard.displayStatus("Stopping the sleigh...")

        status = SleighEngineStatus.OFF
        dashboard.displayStatus("System shutdown.")
    }

    private fun checkReindeerStatus(controlMagicPower: Float) = controlMagicPower >= xmasSpirit
    fun checkAvailablePower(): Float {
        return reindeerPowerUnits.map { it.checkMagicPower() }.sum()
    }
}
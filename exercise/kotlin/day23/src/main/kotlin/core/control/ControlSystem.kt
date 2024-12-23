package core.control

import external.deer.Reindeer
import external.stable.MagicStable

class ControlSystem {
    //The Xmas spirit is 40 magic power unit
    private val xmasSpirit = 40
    private val dashboard = Dashboard()
    private val magicStable = MagicStable()
    private val reindeerPowerUnits = bringAllReindeers()
    var status: SleighEngineStatus = SleighEngineStatus.OFF
    var action: SleighAction = SleighAction.PARKED

    private fun bringAllReindeers() = magicStable.allReindeers.map { attachPowerUnit(it) }

    fun attachPowerUnit(reindeer: Reindeer) = ReindeerPowerUnit(reindeer)

    fun startSystem() {
        dashboard.displayStatus("Starting the sleigh...")

        status = SleighEngineStatus.ON
        dashboard.displayStatus("System ready.")
    }

    @Throws(ReindeersNeedRestException::class, SleighNotStartedException::class)
    fun ascend() {
        var controlMagicPower = 0f
        if (status == SleighEngineStatus.ON) {
            val sleighPower = controlMagicPower
            val reindeerPower = reindeerPowerUnits.map { it.checkMagicPower() }.sum()
            val totalPower = sleighPower + reindeerPower
            dashboard.displayStatus("Energy levels: Sleigh: $sleighPower, Reindeer: $reindeerPower, Total: $totalPower")

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
}
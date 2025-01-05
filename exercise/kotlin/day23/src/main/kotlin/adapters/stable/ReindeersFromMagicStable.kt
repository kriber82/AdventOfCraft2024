package adapters.stable

import core.control.ForGettingReindeer
import external.deer.Reindeer
import external.stable.MagicStable

class ReindeersFromMagicStable(private val magicStable: MagicStable) : ForGettingReindeer {
    override val allReindeers: List<Reindeer> = magicStable.allReindeers
}
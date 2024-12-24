package core.control

import external.deer.Reindeer

class MagicStableFake(private val reindeers: List<Reindeer>) : ForGettingReindeer {
    override val allReindeers: List<Reindeer> = reindeers
}
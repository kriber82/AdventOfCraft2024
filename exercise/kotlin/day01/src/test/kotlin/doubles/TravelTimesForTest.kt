package communication.doubles

import communication.ForGettingTravelTimes
import location.Location
import kotlin.collections.HashMap

class TravelTimesForTest: ForGettingTravelTimes {
    val answers: MutableMap<Pair<Location, Location>, Int> = HashMap()

    override fun getTravelTimeInDays(from: Location, to: Location): Int {
        return answers[Pair(from, to)]!!
    }
}
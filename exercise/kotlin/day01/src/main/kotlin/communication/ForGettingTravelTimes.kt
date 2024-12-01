package communication

import location.Location

interface ForGettingTravelTimes {
    fun getTravelTimeInDays(from: Location, to: Location): Int

}

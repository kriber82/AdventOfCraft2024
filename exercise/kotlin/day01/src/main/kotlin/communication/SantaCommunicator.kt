package communication

import location.Location
import java.time.Duration
import java.time.LocalDate

class SantaCommunicator(
    private val numberOfDaysToRest: Int,
    private val logger: Logger,
    private val today: LocalDate,
    private val christmasDay: LocalDate,
    private val secretBase: Location,
    private val travelTimes: ForGettingTravelTimes
) {

    fun composeMessage(
        reindeer: Reindeer
    ): String {
        val daysBeforeReturn = daysBeforeReturn(reindeer)
        return "Dear ${reindeer.name}, please return from ${reindeer.currentLocation} in $daysBeforeReturn day(s) to be ready and rest before Christmas."
    }

    fun isOverdue(
        reindeer: Reindeer
    ): Boolean {
        if (daysBeforeReturn(reindeer) <= 0) {
            this.logger.log("Overdue for ${reindeer.name} located ${reindeer.currentLocation}.")
            return true
        }
        return false
    }

    private fun daysBeforeReturn(reindeer: Reindeer): Int {
        val numberOfDaysBeforeChristmas = Duration.between(today.atStartOfDay(), christmasDay.atStartOfDay()).toDays().toInt()
        val numbersOfDaysForComingBack = travelTimes.getTravelTimeInDays(reindeer.currentLocation, secretBase)
        return numberOfDaysBeforeChristmas - numbersOfDaysForComingBack - numberOfDaysToRest
    }
}
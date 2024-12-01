package communication

import location.Location
import java.time.Duration
import java.time.LocalDate

class SantaCommunicator(
    private val numberOfDaysToRestBetweenArrivalAndChristmas: Int,
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
        if (arrivalWhenDepartingToday(reindeer) >= startOfRestingPeriod()) {
            this.logger.log("Overdue for ${reindeer.name} located ${reindeer.currentLocation}.")
            return true
        }
        return false
    }

    private fun daysBeforeReturn(reindeer: Reindeer): Int {
        return Duration.between(
            arrivalWhenDepartingToday(reindeer).atStartOfDay(),
            startOfRestingPeriod().atStartOfDay()).toDays().toInt()
    }

    private fun arrivalWhenDepartingToday(reindeer: Reindeer): LocalDate {
        val numbersOfDaysForComingBack = travelTimes.getTravelTimeInDays(reindeer.currentLocation, secretBase)
        return today.plusDays(numbersOfDaysForComingBack.toLong())
    }

    private fun startOfRestingPeriod(): LocalDate {
        return christmasDay.minusDays(numberOfDaysToRestBetweenArrivalAndChristmas.toLong())
    }
}
package communication

import java.time.Duration
import java.time.LocalDate

class SantaCommunicator(private val numberOfDaysToRest: Int, private val logger: Logger, private val today: LocalDate, private val christmasDay: LocalDate) {

    fun composeMessage(
        reindeer: Reindeer,
        numbersOfDaysForComingBack: Int
    ): String {
        val daysBeforeReturn = daysBeforeReturn(numbersOfDaysForComingBack)
        return "Dear ${reindeer.name}, please return from ${reindeer.currentLocation} in $daysBeforeReturn day(s) to be ready and rest before Christmas."
    }

    fun isOverdue(
        reindeer: Reindeer,
        numbersOfDaysForComingBack: Int
    ): Boolean {
        if (daysBeforeReturn(numbersOfDaysForComingBack) <= 0) {
            this.logger.log("Overdue for ${reindeer.name} located ${reindeer.currentLocation}.")
            return true
        }
        return false
    }

    private fun daysBeforeReturn(numbersOfDaysForComingBack: Int): Int {
        val numberOfDaysBeforeChristmas = Duration.between(today.atStartOfDay(), christmasDay.atStartOfDay()).toDays().toInt()
        return numberOfDaysBeforeChristmas - numbersOfDaysForComingBack - numberOfDaysToRest
    }
}
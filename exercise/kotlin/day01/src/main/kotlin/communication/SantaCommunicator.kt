package communication

class SantaCommunicator(private val numberOfDaysToRest: Int, private val logger: Logger) {

    fun composeMessage(
        reindeer: Reindeer,
        numbersOfDaysForComingBack: Int,
        numberOfDaysBeforeChristmas: Int
    ): String {
        val daysBeforeReturn = daysBeforeReturn(numbersOfDaysForComingBack, numberOfDaysBeforeChristmas)
        return "Dear ${reindeer.name}, please return from ${reindeer.currentLocation} in $daysBeforeReturn day(s) to be ready and rest before Christmas."
    }

    fun isOverdue(
        reindeer: Reindeer,
        numbersOfDaysForComingBack: Int,
        numberOfDaysBeforeChristmas: Int
    ): Boolean {
        if (daysBeforeReturn(numbersOfDaysForComingBack, numberOfDaysBeforeChristmas) <= 0) {
            this.logger.log("Overdue for ${reindeer.name} located ${reindeer.currentLocation}.")
            return true
        }
        return false
    }

    private fun daysBeforeReturn(numbersOfDaysForComingBack: Int, numberOfDaysBeforeChristmas: Int): Int {
        return numberOfDaysBeforeChristmas - numbersOfDaysForComingBack - numberOfDaysToRest
    }
}
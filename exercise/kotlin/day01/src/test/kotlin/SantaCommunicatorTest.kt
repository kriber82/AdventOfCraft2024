package communication

import communication.doubles.TravelTimesForTest
import doubles.TestLogger
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import location.Location
import java.time.LocalDate

class SantaCommunicatorTest : DescribeSpec({
    val numberOfDaysToRest = 2
    val logger = TestLogger()

    val today = LocalDate.of(2024, 11, 30)
    val christmasDay = LocalDate.of(2024, 12, 24)
    val numberOfDayBeforeChristmas = 24

    val northPole = Location("North Pole")
    val secretBase = Location("Secret Base")

    lateinit var travelTimes: TravelTimesForTest
    lateinit var communicator: SantaCommunicator

    beforeEach {
        travelTimes = TravelTimesForTest()
        communicator = SantaCommunicator(numberOfDaysToRest, logger, today, christmasDay, secretBase, travelTimes)
    }

    describe("composeMessage") {
        it("should compose correct message") {
            travelTimes.answers[Pair(northPole, secretBase)] = 5
            communicator
                .composeMessage(
                    Reindeer("Dasher", northPole)
                ) shouldBe "Dear Dasher, please return from North Pole in 17 day(s) to be ready and rest before Christmas."
        }
    }

    describe("isOverdue") {
        it("should detect reindeer arriving after christmas") {
            travelTimes.answers[Pair(northPole, secretBase)] = numberOfDayBeforeChristmas
            val overdue = communicator.isOverdue(
                Reindeer("Dasher", northPole),
            )

            overdue shouldBe true
            logger.getLog() shouldBe "Overdue for Dasher located North Pole."
        }

        it("should detect reindeer arriving after start of resting period before christmas") {
            travelTimes.answers[Pair(northPole, secretBase)] = numberOfDayBeforeChristmas - numberOfDaysToRest
            val overdue = communicator.isOverdue(
                Reindeer("Dasher", northPole),
            )

            overdue shouldBe true
            logger.getLog() shouldBe "Overdue for Dasher located North Pole."
        }

        it("should return false when not overdue") {
            travelTimes.answers[Pair(northPole, secretBase)] = numberOfDayBeforeChristmas - numberOfDaysToRest - 1
            communicator.isOverdue(
                Reindeer("Dasher", northPole),
            ) shouldBe false
        }
    }
})

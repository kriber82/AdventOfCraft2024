package communication

import doubles.TestLogger
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class SantaCommunicatorTest : DescribeSpec({
    val numberOfDaysToRest = 2
    val logger = TestLogger()
    val today = LocalDate.of(2024, 11, 30)
    val christmasDay = LocalDate.of(2024, 12, 24)
    val numberOfDayBeforeChristmas = 24
    lateinit var communicator: SantaCommunicator

    beforeEach {
        communicator = SantaCommunicator(numberOfDaysToRest, logger, today, christmasDay)
    }

    describe("composeMessage") {
        it("should compose correct message") {
            communicator
                .composeMessage(
                    Reindeer("Dasher", "North Pole"),
                    5
                ) shouldBe "Dear Dasher, please return from North Pole in 17 day(s) to be ready and rest before Christmas."
        }
    }

    describe("isOverdue") {
        it("should detect overdue reindeer") {
            val overdue = communicator.isOverdue(
                Reindeer("Dasher", "North Pole"),
                numberOfDayBeforeChristmas,
            )

            overdue shouldBe true
            logger.getLog() shouldBe "Overdue for Dasher located North Pole."
        }

        it("should return false when not overdue") {
            communicator.isOverdue(
                Reindeer("Dasher", "North Pole"),
                numberOfDayBeforeChristmas - numberOfDaysToRest - 1,
            ) shouldBe false
        }
    }
})

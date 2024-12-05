import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import routine.*

class RoutineTestWithMyFakes : StringSpec({

    lateinit var emailService: EmailServiceForTest
    lateinit var scheduleService: ScheduleServiceForTest
    lateinit var reindeerFeeder: ReindeerFeederForTest
    lateinit var routine: Routine

    beforeTest {
        emailService = EmailServiceForTest()
        scheduleService = ScheduleServiceForTest()
        reindeerFeeder = ReindeerFeederForTest()

        routine = Routine(emailService, scheduleService, reindeerFeeder)
    }

    "should feed reindeers during routine" {
        routine.start()

        reindeerFeeder.assertReindeersHaveBeenFed()
    }

    "should read new emails during routine" {
        routine.start()

        emailService.assertEmailsHaveBeenRead()
    }

    "should organize day based on todays schedule during routine" {
        val todaysSchedule = Schedule()
        scheduleService.todaysSchedule = todaysSchedule

        routine.start()

        scheduleService.assertDayWasOrganizedBasedOn(todaysSchedule)
    }

    "should call continueDay after organizeMyDay" {
        routine.start()

        scheduleService.assertContinueDayWasCalledAfterOrganizingDay()
    }

    /*
    "should not have any other interaction after continueDay" {
        routine.start()
    }
    */

})

class ScheduleServiceForTest : ScheduleService {
    var todaysSchedule = Schedule()
    var scheduleUsedForOrganizingMyDay: Schedule? = null

    private val methodCallOrder = mutableListOf<String>()

    override fun todaySchedule(): Schedule {
        return todaysSchedule
    }

    override fun organizeMyDay(schedule: Schedule) {
        scheduleUsedForOrganizingMyDay = schedule
        methodCallOrder.add("organizeMyDay")
    }

    override fun continueDay() {
        methodCallOrder.add("continueDay")
    }

    fun assertDayWasOrganizedBasedOn(schedule: Schedule) {
        methodCallOrder shouldContain "organizeMyDay"
        scheduleUsedForOrganizingMyDay shouldBe schedule
    }

    fun assertContinueDayWasCalledAfterOrganizingDay() {
        val organizeIndex = methodCallOrder.indexOf("organizeMyDay")
        val continueIndex = methodCallOrder.indexOf("continueDay")

        // Assert that both methods were called
        organizeIndex shouldBeGreaterThan -1
        continueIndex shouldBeGreaterThan -1

        // Assert that continueDay was called after organizeMyDay
        continueIndex shouldBeGreaterThan organizeIndex
    }

}

class EmailServiceForTest : EmailService {
    var emailsHaveBeenRead = false

    override fun readNewEmails() {
        emailsHaveBeenRead = true
    }

    fun assertEmailsHaveBeenRead() {
        emailsHaveBeenRead shouldBe true
    }
}


class ReindeerFeederForTest : ReindeerFeeder {
    var reindeersHaveBeenFed = false

    override fun feedReindeers() {
        reindeersHaveBeenFed = true
    }

    fun assertReindeersHaveBeenFed() {
        reindeersHaveBeenFed shouldBe true
    }
}
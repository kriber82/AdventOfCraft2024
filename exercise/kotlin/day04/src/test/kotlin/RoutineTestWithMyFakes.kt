import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
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

    /*
    "should call continueDay after organizeMyDay" {
        routine.start()
    }

    "should not have any other interaction after continueDay" {
        routine.start()
    }
    */

})

class ScheduleServiceForTest : ScheduleService {
    var dayWasOrganized = false
    var continueDayWasCalled = false
    var todaysSchedule: Schedule? = null
    var dayWasBasedOnSchedule: Schedule? = null

    override fun todaySchedule(): Schedule {
        return todaysSchedule ?: Schedule()
    }

    override fun organizeMyDay(schedule: Schedule) {
        dayWasOrganized = true
        dayWasBasedOnSchedule = schedule
    }

    override fun continueDay() {
        continueDayWasCalled = true
    }

    fun assertDayWasOrganizedBasedOn(schedule: Schedule) {
        dayWasOrganized shouldBe true
        dayWasBasedOnSchedule shouldBe schedule
    }

    fun assertContinueDayWasCalled() {
        continueDayWasCalled shouldBe true
    }

    fun assertNoOtherInteraction() {
        //assert all other properties are false
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
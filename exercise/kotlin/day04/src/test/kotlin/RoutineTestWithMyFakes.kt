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

    /*
    "should read new emails during routine" {
        routine.start()
    }

    "should organize day based on todays schedule during routine" {
        routine.start()
    }


    "should call continueDay after organizeMyDay" {
        routine.start()
    }

    "should not have any other interaction after continueDay" {
        routine.start()
    }
    */

})

class ScheduleServiceForTest : ScheduleService {
    override fun todaySchedule(): Schedule {
        return Schedule()
    }

    override fun organizeMyDay(schedule: Schedule) {
        //dummy for now
    }

    override fun continueDay() {
        //dummy for now
    }

}

class EmailServiceForTest : EmailService {
    override fun readNewEmails() {
        //dummy for now
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
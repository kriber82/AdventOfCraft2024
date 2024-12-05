import io.kotest.core.spec.style.StringSpec
import io.mockk.*
import routine.*

class RoutineTestWithMyFakes : StringSpec({

    lateinit var emailService: EmailService
    lateinit var scheduleService: ScheduleService
    lateinit var reindeerFeeder: ReindeerFeeder
    lateinit var routine: Routine

    beforeTest {
        emailService = EmailServiceForTest()
        scheduleService = ScheduleServiceForTest()
        reindeerFeeder = ReindeerFeederForTest()

        routine = Routine(emailService, scheduleService, reindeerFeeder)
    }

    "should feed reindeers during routine" {
        routine.start()

        //TODO add verification
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
    override fun feedReindeers() {
        //dummy for now
    }
}
import io.kotest.core.spec.style.StringSpec
import io.mockk.mockk
import io.mockk.every
import io.mockk.verify
import routine.*

class RoutineTestWithMockK : StringSpec({

    lateinit var emailService: EmailService
    lateinit var scheduleService: ScheduleService
    lateinit var reindeerFeeder: ReindeerFeeder
    lateinit var schedule: Schedule
    lateinit var routine: Routine

    beforeTest {
        emailService = mockk(relaxed = true)
        scheduleService = mockk(relaxed = true)
        reindeerFeeder = mockk(relaxed = true)
        schedule = Schedule()

        every { scheduleService.todaySchedule() } returns schedule

        routine = Routine(emailService, scheduleService, reindeerFeeder)
    }

    "test start" {
        routine.start()

        verify { scheduleService.organizeMyDay(schedule) }
        verify { reindeerFeeder.feedReindeers() }
        verify { emailService.readNewEmails() }
        verify { scheduleService.continueDay() }
    }

    /*
    "should feed reindeers during routine" {
        // Test case implementation goes here
    }
    */
})
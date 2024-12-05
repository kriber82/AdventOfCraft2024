import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
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
        val organizeIndex = getCallOrderIndex("organizeMyDay")
        val continueIndex = getCallOrderIndex("continueDay")

        continueIndex shouldBeGreaterThan organizeIndex
    }

    private fun getCallOrderIndex(method: String): Int {
        val callIndex = methodCallOrder.indexOf(method)
        callIndex shouldBeGreaterThan -1
        return callIndex
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
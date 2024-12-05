import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import routine.*

class RoutineTestWithMyFakes : StringSpec({

    lateinit var emailService: EmailServiceForTest
    lateinit var scheduleService: ScheduleServiceForTest
    lateinit var reindeerFeeder: ReindeerFeederForTest

    lateinit var callsTracker: CallsTracker

    lateinit var routine: Routine

    beforeTest {
        callsTracker = CallsTracker()

        emailService = EmailServiceForTest(callsTracker)
        scheduleService = ScheduleServiceForTest(callsTracker)
        reindeerFeeder = ReindeerFeederForTest(callsTracker)

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

    "should not have any other interaction after continueDay" {
        routine.start()

        callsTracker.assertIsLastInteraction("continueDay")
    }

})

class CallsTracker {
    private val calledMethodIds = mutableListOf<String>()

    fun registerMethodCall(methodId: String) {
        calledMethodIds.add(methodId)
    }

    fun assertIsLastInteraction(methodId: String) {
        calledMethodIds.last() shouldBe methodId
    }

    fun getCallOrderIndex(method: String): Int {
        val callIndex = calledMethodIds.indexOf(method)
        callIndex shouldBeGreaterThan -1 // Ensure the method exists
        return callIndex
    }
}

class ScheduleServiceForTest(val callsTracker: CallsTracker) : ScheduleService {
    var todaysSchedule = Schedule()
    var scheduleUsedForOrganizingMyDay: Schedule? = null

    override fun todaySchedule(): Schedule {
        return todaysSchedule
    }

    override fun organizeMyDay(schedule: Schedule) {
        scheduleUsedForOrganizingMyDay = schedule
        callsTracker.registerMethodCall("organizeMyDay")
    }

    override fun continueDay() {
        callsTracker.registerMethodCall("continueDay")
    }

    fun assertDayWasOrganizedBasedOn(schedule: Schedule) {
        scheduleUsedForOrganizingMyDay shouldBe schedule
    }

    fun assertContinueDayWasCalledAfterOrganizingDay() {
        val organizeIndex = callsTracker.getCallOrderIndex("organizeMyDay")
        val continueIndex = callsTracker.getCallOrderIndex("continueDay")

        continueIndex shouldBeGreaterThan organizeIndex
    }
}

class EmailServiceForTest(val callsTracker: CallsTracker) : EmailService {
    var emailsHaveBeenRead = false

    override fun readNewEmails() {
        emailsHaveBeenRead = true
        callsTracker.registerMethodCall("readNewEmails")
    }

    fun assertEmailsHaveBeenRead() {
        emailsHaveBeenRead shouldBe true
    }
}

class ReindeerFeederForTest(val callsTracker: CallsTracker) : ReindeerFeeder {
    var reindeersHaveBeenFed = false

    override fun feedReindeers() {
        reindeersHaveBeenFed = true
        callsTracker.registerMethodCall("feedReindeers")
    }

    fun assertReindeersHaveBeenFed() {
        reindeersHaveBeenFed shouldBe true
    }
}

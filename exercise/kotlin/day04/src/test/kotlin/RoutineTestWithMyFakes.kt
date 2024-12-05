import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.comparables.shouldBeGreaterThan
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

    fun assertMethodCalled(methodId: String) {
        calledMethodIds.contains(methodId) shouldBe true
    }

    fun getCallOrderIndex(method: String): Int {
        val callIndex = calledMethodIds.indexOf(method)
        callIndex shouldBeGreaterThan -1 // Ensure the method exists
        return callIndex
    }

    fun assertCallOrder(earlierMethodId: String, laterMethodId: String) {
        val earlierIndex = getCallOrderIndex(earlierMethodId)
        val laterIndex = getCallOrderIndex(laterMethodId)

        laterIndex shouldBeGreaterThan earlierIndex
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
        callsTracker.assertMethodCalled("organizeMyDay")
    }

    fun assertContinueDayWasCalledAfterOrganizingDay() {
        callsTracker.assertCallOrder("organizeMyDay", "continueDay")
    }
}

class EmailServiceForTest(val callsTracker: CallsTracker) : EmailService {
    override fun readNewEmails() {
        callsTracker.registerMethodCall("readNewEmails")
    }

    fun assertEmailsHaveBeenRead() {
        callsTracker.assertMethodCalled("readNewEmails")
    }
}

class ReindeerFeederForTest(val callsTracker: CallsTracker) : ReindeerFeeder {
    override fun feedReindeers() {
        callsTracker.registerMethodCall("feedReindeers")
    }

    fun assertReindeersHaveBeenFed() {
        callsTracker.assertMethodCalled("feedReindeers")
    }
}

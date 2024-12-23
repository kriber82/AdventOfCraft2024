import core.control.*
import external.stable.MagicStable
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.floats.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ControlSystemTest : StringSpec({
    val outputStreamCaptor = ByteArrayOutputStream()
    val lineBreak = System.lineSeparator()

    beforeTest {
        System.setOut(PrintStream(outputStreamCaptor))
    }

    afterTest {
        System.setOut(System.out)
    }

    "testStart" {
        // The system has been started
        val controlSystem = ControlSystem()
        controlSystem.action = SleighAction.FLYING
        controlSystem.status = SleighEngineStatus.OFF
        controlSystem.startSystem()
        controlSystem.status shouldBe SleighEngineStatus.ON
        outputStreamCaptor.toString().trim() shouldBe "Starting the sleigh...${lineBreak}System ready."
    }

    "testAscend" {
        val controlSystem = ControlSystem()
        controlSystem.startSystem()
        controlSystem.ascend()
        controlSystem.action shouldBe SleighAction.FLYING
        outputStreamCaptor.toString().trim() shouldBe "Starting the sleigh...${lineBreak}System ready.${lineBreak}Ascending..."
    }

    "testDescend" {
        val controlSystem = ControlSystem()
        controlSystem.startSystem()
        controlSystem.ascend()
        controlSystem.descend()
        controlSystem.action shouldBe SleighAction.HOVERING
        outputStreamCaptor.toString()
            .trim() shouldBe "Starting the sleigh...${lineBreak}System ready.${lineBreak}Ascending...${lineBreak}Descending..."
    }

    "testPark" {
        val controlSystem = ControlSystem()
        controlSystem.startSystem()
        controlSystem.park()
        controlSystem.action shouldBe SleighAction.PARKED
    }
})

class ControlSystemTestAdditions : FunSpec({
    context("Reindeer Power Unit") {
        context("Fresh and Sick Reindeer") {
            val sickAndFreshDancer = MagicStable().allReindeers[0]
            val tested = ReindeerPowerUnit(sickAndFreshDancer)

            test("should return 0 on checkMagicPower") {
                tested.checkMagicPower() shouldBe 0
            }

            test("should return 0 on harnessMagicPower") {
                tested.harnessMagicPower() shouldBe 0
            }
        }

        context("Rested and Sick Reindeer") {
            val sickAndRestedDancer = MagicStable().allReindeers[0]
            val tested = ReindeerPowerUnit(sickAndRestedDancer)

            beforeTest {
                sickAndRestedDancer.timesHarnessing = 0
            }

            test("should return 0 on checkMagicPower") {
                tested.checkMagicPower() shouldBe 0
            }

            test("should return 0 on harnessMagicPower") {
                tested.harnessMagicPower() shouldBe 0
            }
        }

        context("fresh and healthy reindeer") {
            val healthyAndRestedBlitzen = MagicStable().allReindeers[1]
            val tested = ReindeerPowerUnit(healthyAndRestedBlitzen)

            beforeTest {
                healthyAndRestedBlitzen.timesHarnessing = 0
            }

            test("should return > 0 on checkMagicPower") {
                tested.checkMagicPower() shouldBeGreaterThan 0.0f
            }

            test("should return > 0 on harnessMagicPower") {
                tested.harnessMagicPower() shouldBeGreaterThan 0.0f
            }

        }

        context("exhausted and healthy reindeer") {
            val healthyAndExhaustedBlitzen = MagicStable().allReindeers[1]
            val tested = ReindeerPowerUnit(healthyAndExhaustedBlitzen)

            beforeTest {
                while (!healthyAndExhaustedBlitzen.needsRest())
                    healthyAndExhaustedBlitzen.timesHarnessing ++
            }

            test("should return 0 on checkMagicPower") {
                tested.checkMagicPower() shouldBe 0.0f
            }

            test("should return 0 on harnessMagicPower") {
                tested.harnessMagicPower() shouldBe 0.0f
            }

        }

        context("power amplifiers") {
            val healthyAndRestedBlitzen = MagicStable().allReindeers[1]

            test("should amplify power by 2 with blessed amp") {
                val standardReindeer = ReindeerPowerUnit(healthyAndRestedBlitzen)
                val blessedReindeer = ReindeerPowerUnit(healthyAndRestedBlitzen, MagicPowerAmplifier(AmplifierType.BLESSED))

                blessedReindeer.checkMagicPower() shouldBe standardReindeer.checkMagicPower() * 2
                blessedReindeer.harnessMagicPower() shouldBe standardReindeer.harnessMagicPower() * 2
            }

            test("should amplify power by 3 with divine amp") {
                val standardReindeer = ReindeerPowerUnit(healthyAndRestedBlitzen)
                val divineReindeer = ReindeerPowerUnit(healthyAndRestedBlitzen, MagicPowerAmplifier(AmplifierType.DIVINE))

                divineReindeer.checkMagicPower() shouldBe standardReindeer.checkMagicPower() * 3
                divineReindeer.harnessMagicPower() shouldBe standardReindeer.harnessMagicPower() * 3
            }
        }

    }

    context("sleigh") {
        test("can not accumulate power over several ascends") {
            val tested = ControlSystem()
            tested.startSystem()
            shouldThrow<ReindeersNeedRestException> {
                tested.ascend()
            }
            shouldThrow<ReindeersNeedRestException> {
                tested.ascend()
            }
        }
    }
})
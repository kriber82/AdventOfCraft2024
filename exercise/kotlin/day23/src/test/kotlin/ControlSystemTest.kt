import adapters.stable.ReindeersFromMagicStable
import core.control.*
import external.deer.Reindeer
import external.stable.MagicStable
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.floats.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.shuffle
import io.kotest.property.checkAll
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.io.PrintStream

class ControlSystemTest : StringSpec({
    lateinit var outputStreamCaptor: OutputStream
    val lineBreak = System.lineSeparator()

    beforeTest {
        outputStreamCaptor = ByteArrayOutputStream()
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
        test("should not be able to accumulate power over several ascend tries") {
            val tested = ControlSystem(amplifierInventory= AmplifierInventory(0, 0))
            tested.startSystem()
            shouldThrow<ReindeersNeedRestException> {
                tested.ascend()
            }
            shouldThrow<ReindeersNeedRestException> {
                tested.ascend()
            }
        }

        test("should be able to ascend with amplifiers applied") {
            val tested = ControlSystem()
            tested.startSystem()
            tested.ascend()
            tested.action shouldBe SleighAction.FLYING
        }

        test("should use reindeers from given repository") {
            val reindeers = listOf(Reindeer("R1", 1, 1), Reindeer("R2", 2, 2))
            val reindeerRepo: ForGettingReindeer = MagicStableFake(reindeers)
            val tested = ControlSystem(reindeerRepo)

            tested.reindeerPowerUnits.size shouldBe 2
        }

        test ("should use available stronger amplifiers") {
            val ampInventory = AmplifierInventory(1, 2)

            val tested = ControlSystem(amplifierInventory = ampInventory)

            tested.reindeerPowerUnits.filter{ it.amplifier.amplifierType == AmplifierType.DIVINE }.size shouldBe 1
            tested.reindeerPowerUnits.filter{ it.amplifier.amplifierType == AmplifierType.BLESSED}.size shouldBe 2
            tested.reindeerPowerUnits.filter{ it.amplifier.amplifierType == AmplifierType.BASIC}.size shouldBe 6 - 2 //leave sick reindeer home
        }

        test("should automatically distribute amplifiers to healthy reindeer") {
            val tested = ControlSystem(ReindeersFromMagicStable(MagicStable()), AmplifierInventory(1, 2))

            tested.checkAvailablePower() shouldBe 65.0f
        }

        val santasReindeerIndices = ReindeerBuilder.getSantasReindeers().indices.toList()
        val reindeerIndexPermutations = Arb.shuffle(santasReindeerIndices)
        val upToThreeSickReindeerIndices = Arb.bind(Arb.int(0..3), reindeerIndexPermutations) { sickCount, indices ->
            indices.take(sickCount)
        }
        test("should be able to power the sleigh with up to 3 sick reindeer") {
            checkAll(upToThreeSickReindeerIndices) { sickReindeerIndices ->
                val reindeers = ReindeerBuilder.getSantasReindeers().mapIndexed { index, builder ->
                    builder.withSickness(sickReindeerIndices.contains(index)).build()
                }
                val tested = ControlSystem(MagicStableFake(reindeers), AmplifierInventory(1, 2))

                tested.startSystem()
                tested.ascend()
                tested.action shouldBe SleighAction.FLYING
            }
        }

        val eightToNineSickReindeerIndices = Arb.bind(Arb.int(8..9), reindeerIndexPermutations) { sickCount, indices ->
            indices.take(sickCount)
        }
        test("should not be able to power the sleigh with 8 or 9 sick reindeer") {
            checkAll(eightToNineSickReindeerIndices) { sickReindeerIndices ->
                val reindeers = ReindeerBuilder.getSantasReindeers().mapIndexed { index, builder ->
                    builder.withSickness(sickReindeerIndices.contains(index)).build()
                }
                val tested = ControlSystem(MagicStableFake(reindeers), AmplifierInventory(1, 2))

                tested.startSystem()
                shouldThrow<ReindeersNeedRestException> { tested.ascend() }
            }
        }
    }
})
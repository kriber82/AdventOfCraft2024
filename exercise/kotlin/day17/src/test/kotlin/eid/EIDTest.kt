package eid

import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import net.jqwik.api.*
import net.jqwik.api.constraints.IntRange
import net.jqwik.api.constraints.StringLength

data class EidPayloadSubstrings(val gender: String, val year: Int, val serialNumber: String) {
    val yearString = EIDTest.toYearString(year)

    fun plusControlKey(): String {
        return "$gender$yearString$serialNumber" + "00" //TODO add real control key
    }

    override fun toString(): String {
        return "$gender$yearString$serialNumber"
    }
}

class EIDTest {

    val serialNumbers = Arbitraries.strings().ofLength(3).withCharRange('0', '9')
    val controlKeys = Arbitraries.strings().ofLength(2).withCharRange('0', '9')

    private val validGenderChars = setOf('1', '2', '3')

    @Provide
    fun validGenders(): Arbitrary<String> {
        return Arbitraries.strings().withChars(*validGenderChars.toCharArray()).ofLength(1)
    }

    @Provide
    fun invalidGenders(): Arbitrary<String> {
        return Arbitraries.strings().ofLength(1).filter { it[0] !in validGenderChars }
    }

    @Provide
    fun validYears(): Arbitrary<Int> {
        return Arbitraries.integers().between(0, 99)
    }

    @Provide
    fun validEidPayloads(): Arbitrary<EidPayloadSubstrings> {
        return Combinators.combine(validGenders(), validYears(), serialNumbers)
            .`as` { g, y, s -> EidPayloadSubstrings(g, y, s) }
    }

    @Provide
    fun validEids(): Arbitrary<String> {
        return Combinators.combine(validEidPayloads(), controlKeys)
            .`as` { p, s4 -> "$p$s4" }
    }

    @Property
    fun shouldRejectEIDsThatAreTooShort(@ForAll @StringLength(max = 7) tooShortForEid: String) {
        EID.parse(tooShortForEid).leftOrNull() shouldBe ParsingError.InputTooShort()
    }

    @Property
    fun `should reject EIDs that are too long`(@ForAll @StringLength(min = 9) tooLongForEid: String) {
        EID.parse(tooLongForEid).leftOrNull() shouldBe ParsingError.InputTooLong()
    }

    @Property
    fun `should accept valid EIDs`(@ForAll("validEids") validEid: String) {
        val parsed = EID.parse(validEid)

        parsed.should { it.isRight() }
        parsed.getOrNull().toString() shouldBe validEid
    }

    @Property
    fun `should parse genders`(
        @ForAll elfGender: ElfGender,
        @ForAll("validEidPayloads") randomEidFields: EidPayloadSubstrings
    ) {
        val usedEidPayload =
            EidPayloadSubstrings(toEidField(elfGender), randomEidFields.year, randomEidFields.serialNumber)
        val input = usedEidPayload.plusControlKey()

        EID.parse(input).getOrNull()?.gender shouldBe elfGender
    }

    @Property
    fun `should reject invalid genders`(
        @ForAll("invalidGenders") invalidGenderString: String,
        @ForAll("validEidPayloads") randomEidFields: EidPayloadSubstrings
    ) {
        val usedEidPayload =
            EidPayloadSubstrings(invalidGenderString, randomEidFields.year, randomEidFields.serialNumber)
        val input = usedEidPayload.plusControlKey()

        EID.parse(input).leftOrNull().shouldBeInstanceOf<ParsingError.InvalidElfGender>()
    }

    @Property
    fun `should parse year`(
        @ForAll("validYears") year: Int,
        @ForAll("validEidPayloads") randomEidFields: EidPayloadSubstrings
    ) {
        val usedEidPayload = EidPayloadSubstrings(randomEidFields.gender, year, randomEidFields.serialNumber)
        val input = usedEidPayload.plusControlKey()

        EID.parse(input).getOrNull()?.year shouldBe year
    }

    @Property
    fun `should reject negative years`(
        @ForAll @IntRange(min = -9, max = -1) negativeYear: Int,
        @ForAll("validEidPayloads") randomEidFields: EidPayloadSubstrings
    ) {
        val usedEidPayload = EidPayloadSubstrings(randomEidFields.gender, negativeYear, randomEidFields.serialNumber)
        val input = usedEidPayload.plusControlKey()

        EID.parse(input).leftOrNull().shouldBeInstanceOf<ParsingError.InvalidYear>()
    }

    companion object {

        private fun toEidField(elfGender: ElfGender): String {
            return when (elfGender) {
                ElfGender.Sloubi -> "1"
                ElfGender.Gagna -> "2"
                ElfGender.Catact -> "3"
            }
        }

        fun toYearString(year: Int): String {
            return year.toString().padStart(2, '0')
        }
    }
}
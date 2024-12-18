package eid

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeTypeOf
import net.jqwik.api.*
import net.jqwik.api.constraints.StringLength

data class EidPayloadSubstrings(val gender: String, val year: String, val serialNumber: String) {
    fun plusControlKey(): String {
        return "$gender$year$serialNumber" + "00" //TODO add real control key
    }
}

class EIDTest {

    val years = Arbitraries.strings().ofLength(2)
    val serialNumbers = Arbitraries.strings().ofLength(3)
    val controlKeys = Arbitraries.strings().ofLength(2)

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
    fun validEidPayloads(): Arbitrary<EidPayloadSubstrings> {
        return Combinators.combine(validGenders(), years, serialNumbers)
            .`as` {g, y, s -> EidPayloadSubstrings(g, y, s) }
    }

    @Provide
    fun validEids(): Arbitrary<String> {
        return Combinators.combine(validGenders(), years, serialNumbers, controlKeys)
            .`as` { s1, s2, s3, s4 -> "$s1$s2$s3$s4" }
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
        EID.parse(validEid).getOrNull().toString() shouldBe validEid
    }

    @Property
    fun `should parse genders`(@ForAll elfGender: ElfGender, @ForAll("validEidPayloads") randomEidFields: EidPayloadSubstrings) {
        val usedEidPayload = EidPayloadSubstrings(toEidField(elfGender), randomEidFields.year, randomEidFields.serialNumber)
        val input = usedEidPayload.plusControlKey()

        EID.parse(input).getOrNull()?.gender shouldBe elfGender
    }

    @Property
    fun `should reject invalid genders`(@ForAll("invalidGenders") invalidGenderString: String, @ForAll("validEidPayloads") randomEidFields: EidPayloadSubstrings) {
        val usedEidPayload = EidPayloadSubstrings(invalidGenderString, randomEidFields.year, randomEidFields.serialNumber)
        val input = usedEidPayload.plusControlKey()

        EID.parse(input).leftOrNull().shouldBeInstanceOf<ParsingError.InvalidElfGender>()
    }

    private fun toEidField(elfGender: ElfGender): String {
        return when (elfGender) {
            ElfGender.Sloubi -> "1"
            ElfGender.Gagna -> "2"
            ElfGender.Catact -> "3"
        }
    }
}
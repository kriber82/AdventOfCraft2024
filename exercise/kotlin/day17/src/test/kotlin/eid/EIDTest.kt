package eid

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import net.jqwik.api.*
import net.jqwik.api.constraints.IntRange
import net.jqwik.api.constraints.StringLength

data class EidPayloadSubstrings(val gender: String, val year: String, val serialNumber: String) {
    fun plusControlKey(): String {
        return toString() + "00" //TODO add real control key
    }

    override fun toString(): String {
        return "$gender$year$serialNumber"
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

    val digitCharacters: CharRange = '0'..'9'

    @Provide
    fun yearsContainingNonDigits(): Arbitrary<String> {
        return Arbitraries.strings().ofLength(2).filter { !it.all { it in digitCharacters } }
    }

    @Provide
    fun validEids(): Arbitrary<String> {
        return Combinators.combine(validGenders(), validYears(), serialNumbers, controlKeys)
            .`as` { g, y, sn, s4 -> "$g${toYearString(y)}$sn$s4" } //TODO use valid builder instead?
    }

    @Property
    fun shouldRejectEIDsThatAreTooShort(@ForAll @StringLength(max = 7) tooShortForEid: String) {
        EID.parse(tooShortForEid).shouldBeLeft().shouldBeInstanceOf<ParsingError.InputTooShort>()
    }

    @Property
    fun `should reject EIDs that are too long`(@ForAll @StringLength(min = 9) tooLongForEid: String) {
        EID.parse(tooLongForEid).shouldBeLeft().shouldBeInstanceOf<ParsingError.InputTooLong>()
    }

    @Property
    fun `should accept valid EIDs`(@ForAll("validEids") validEid: String) {
        val parsed = EID.parse(validEid)

        parsed.should { it.isRight() }
        parsed.shouldBeRight().toString() shouldBe validEid
    }

    @Property
    fun `should parse genders`(
        @ForAll elfGender: ElfGender,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withGender(elfGender).build()

        EID.parse(input).shouldBeRight().gender shouldBe elfGender
    }

    @Property
    fun `should reject invalid genders`(
        @ForAll("invalidGenders") invalidGenderString: String,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withGenderString(invalidGenderString).build()

        EID.parse(input).shouldBeLeft().shouldBeInstanceOf<ParsingError.InvalidElfGender>()
    }

    @Property
    fun `should parse year`(
        @ForAll("validYears") year: Int,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withYear(year).build()

        EID.parse(input).shouldBeRight().year shouldBe year
    }

    @Property
    fun `should reject negative years`(
        @ForAll @IntRange(min = -9, max = -1) negativeYear: Int,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withYear(negativeYear).build()

        EID.parse(input).shouldBeLeft().shouldBeInstanceOf<ParsingError.InvalidYear>()
    }

    @Property
    fun `should reject non-number years`(
        @ForAll("yearsContainingNonDigits") invalidYear: String,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withYearString(invalidYear).build()

        EID.parse(input).shouldBeLeft().shouldBeInstanceOf<ParsingError.InvalidYear>()
    }

    companion object {

        internal fun toYearString(year: Int): String {
            return year.toString().padStart(2, '0')
        }
    }

    @Provide
    fun validEidBuilders(): Arbitrary<EidStringBuilder> {
        return Combinators.combine(validGenders(), validYears(), serialNumbers, controlKeys)
            .`as` { g, y, sn, c ->
                EidStringBuilder().withGenderString(g).withYear(y).withSerialNumber(sn).withControlKeyOverride(c)
            }
    }

}

class EidStringBuilder {
    private var gender: String = ""
    private var year: String = ""
    private var serialNumber: String = ""
    private var controlKeyOverride: String? = null

    fun withGender(gender: ElfGender): EidStringBuilder {
        this.gender = toEidField(gender)
        return this
    }

    fun withGenderString(genderString: String): EidStringBuilder {
        this.gender = genderString
        return this
    }

    fun withYear(year: Int): EidStringBuilder {
        this.year = EIDTest.toYearString(year)
        return this
    }

    fun withSerialNumber(serialNumber: String): EidStringBuilder {
        this.serialNumber = serialNumber
        return this
    }

    fun withControlKeyOverride(controlKeyOverride: String): EidStringBuilder {
        this.controlKeyOverride = controlKeyOverride
        return this
    }

    fun build(): String {
        var controlKey = "00" //TODO calculate
        if (controlKeyOverride != null) {
            controlKey = controlKeyOverride!!
        }
        return "$gender$year$serialNumber$controlKey"
    }

    fun withYearString(yearString: String): EidStringBuilder {
        this.year = yearString
        return this
    }

    private fun toEidField(elfGender: ElfGender): String {
        return when (elfGender) {
            ElfGender.Sloubi -> "1"
            ElfGender.Gagna -> "2"
            ElfGender.Catact -> "3"
        }
    }


}
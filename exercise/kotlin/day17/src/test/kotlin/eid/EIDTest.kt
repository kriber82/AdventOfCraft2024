package eid

import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import net.jqwik.api.*
import net.jqwik.api.constraints.IntRange
import net.jqwik.api.constraints.StringLength

class EIDTest {

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

    @Property
    fun `should parse valid serial number`(
        @ForAll("validSerialNumbers") serialNumber: Int,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withSerialNumber(serialNumber).build()

        EID.parse(input).shouldBeRight().serialNumber shouldBe serialNumber
    }

    @Property
    fun `should reject negative serial numbers and zero`(
        @ForAll @IntRange(min = -99, max = 0) zeroOrNegativeSerialNumber: Int,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withSerialNumber(zeroOrNegativeSerialNumber).build()

        EID.parse(input).shouldBeLeft().shouldBeInstanceOf<ParsingError.InvalidSerialNumber>()
    }

    @Property
    fun `should reject non-number serial numbers`(
        @ForAll("serialNumbersContainingNonDigits") invalidSerialNumber: String,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withSerialNumberString(invalidSerialNumber).build()

        EID.parse(input).shouldBeLeft().shouldBeInstanceOf<ParsingError.InvalidSerialNumber>()
    }

    @Provide
    fun validEidBuilders(): Arbitrary<EidStringBuilder> {
        return Combinators.combine(validGenders(), validYears(), validSerialNumbers())
            .`as` { g, y, sn ->
                EidStringBuilder().withGenderString(g).withYear(y).withSerialNumber(sn)
            }
    }

    @Provide
    fun validEids(): Arbitrary<String> {
        return validEidBuilders().map { it.build() }
    }

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
    fun yearsContainingNonDigits(): Arbitrary<String> {
        return Arbitraries.strings().ofLength(2).filter { !it.all { it in digitCharacters } }
    }

    @Provide
    fun validSerialNumbers(): Arbitrary<Int> {
        return Arbitraries.integers().between(1, 999)
    }

    @Provide
    fun serialNumbersContainingNonDigits(): Arbitrary<String> {
        return Arbitraries.strings().ofLength(3).filter { !it.all { it in digitCharacters } }
    }

    val digitCharacters: CharRange = '0'..'9'
}

class EidStringBuilder {
    private var gender: String = ""
    private var year: String = ""
    private var serialNumber: String = ""
    private var controlKeyOverride: String? = null

    fun withGender(gender: ElfGender): EidStringBuilder {
        this.gender = toGenderString(gender)
        return this
    }

    fun withGenderString(genderString: String): EidStringBuilder {
        this.gender = genderString
        return this
    }

    fun withYear(year: Int): EidStringBuilder {
        this.year = toYearString(year)
        return this
    }

    fun withYearString(yearString: String): EidStringBuilder {
        this.year = yearString
        return this
    }

    fun withSerialNumber(serialNumber: Int): EidStringBuilder {
        this.serialNumber = toSerialNumberString(serialNumber)
        return this
    }

    fun withSerialNumberString(serialNumberString: String): EidStringBuilder {
        this.serialNumber = serialNumberString
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

    private fun toGenderString(elfGender: ElfGender): String {
        return when (elfGender) {
            ElfGender.Sloubi -> "1"
            ElfGender.Gagna -> "2"
            ElfGender.Catact -> "3"
        }
    }

    private fun toYearString(year: Int): String {
        return year.toString().padStart(2, '0')
    }

    private fun toSerialNumberString(serialNumber: Int): String {
        return serialNumber.toString().padStart(3, '0')
    }
}
package eid

import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import net.jqwik.api.*
import net.jqwik.api.constraints.IntRange
import net.jqwik.api.constraints.StringLength
import org.junit.jupiter.api.Test

//no longer necessary, as the same tests are covered in EIDTest. Keeping EIDTestJqwik for reference as this is a learning / demo project
class EIDTestJqwik {

    @Property
    fun shouldRejectEIDsThatAreTooShort(@ForAll @StringLength(max = 7) tooShortForEid: String) {
        EID.parse(tooShortForEid)
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.InputTooShort>()
    }

    @Property
    fun `should reject EIDs that are too long`(@ForAll @StringLength(min = 9) tooLongForEid: String) {
        EID.parse(tooLongForEid)
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.InputTooLong>()
    }

    @Property
    fun `should accept valid EIDs`(@ForAll("validEids") validEid: String) {
        EID.parse(validEid)
            .shouldBeRight()
            .toString() shouldBe validEid
    }

    @Property
    fun `should parse genders`(
        @ForAll elfGender: ElfGender,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withGender(elfGender).build()

        EID.parse(input)
            .shouldBeRight()
            .gender shouldBe elfGender
    }

    @Property
    fun `should reject invalid genders`(
        @ForAll("invalidGenders") invalidGenderString: String,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withGenderString(invalidGenderString).build()

        EID.parse(input)
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.InvalidElfGender>()
    }

    @Property
    fun `should parse year`(
        @ForAll("validYears") year: Int,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withYear(year).build()

        EID.parse(input)
            .shouldBeRight()
            .year shouldBe year
    }

    //might be merged to `should reject invalid years` with next test (pro: way less boilerplate, con: less descriptive & error pinpointing by test name not as good)
    //see `should reject invalid control keys` for merged example
    @Property
    fun `should reject negative years`(
        @ForAll @IntRange(min = -9, max = -1) negativeYear: Int,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withYear(negativeYear).build()

        EID.parse(input)
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.InvalidYear>()
    }

    @Property
    fun `should reject non-number years`(
        @ForAll("yearsContainingNonDigits") invalidYear: String,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withYearString(invalidYear).build()

        EID.parse(input)
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.InvalidYear>()
    }

    @Test
    fun `should reject year containing + despite being valid number - bug`() {
        val invalidYear = "+0"
        EID.parse("1${invalidYear}00100")
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.InvalidYear>()
    }

    @Property
    fun `should parse valid serial number`(
        @ForAll("validSerialNumbers") serialNumber: Int,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withSerialNumber(serialNumber).build()

        EID.parse(input)
            .shouldBeRight()
            .serialNumber shouldBe serialNumber
    }

    //might be merged (for details see comment above)
    @Property
    fun `should reject negative serial numbers and zero`(
        @ForAll @IntRange(min = -99, max = 0) zeroOrNegativeSerialNumber: Int,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withSerialNumber(zeroOrNegativeSerialNumber).build()

        EID.parse(input)
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.InvalidSerialNumber>()
    }

    @Property
    fun `should reject non-number serial numbers`(
        @ForAll("serialNumbersContainingNonDigits") serialNumberContainingNonDigit: String,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withSerialNumberString(serialNumberContainingNonDigit).build()

        EID.parse(input)
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.InvalidSerialNumber>()
    }

    @Test
    fun `should calculate control key for Jerceval's EID`() {
        EID.calculateControlKey("198007xx") shouldBe 67.right()
    }

    @Property
    fun `should calculate valid control keys for valid EIDs`(@ForAll("validEids") validEid: String) {
        EID.calculateControlKey(validEid)
            .shouldBeRight()
            .shouldBeInRange(1..97)
    }

    @Property
    fun `should reject control key calculation for non-numeric EIDs`(@ForAll("eidsWithNonDigitChars") validEid: String) {
        EID.calculateControlKey(validEid)
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.CouldNotCalculateControlKey>()
    }

    @Test
    fun `should reject generating control keys outside valid range - bug`() {
        EID.calculateControlKey("-00003xx")
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.CouldNotCalculateControlKey>()
    }

    @Test
    fun `should accept Jerceval's EID with matching control key`() {
        EID.parse("19800767").shouldBeRight()
    }

    @Test
    fun `should reject Jerceval's EID with non-matching control key`() {
        EID.parse("19800766").shouldBeLeft().shouldBeInstanceOf<ParsingError.ControlKeyDoesNotMatch>()
    }

    @Property
    fun `should reject EIDs with non-matching control key`(
        @ForAll("eidsWithNonMatchingControlKey") eidWithNonMatchingControlKey: String,
    ) {
        EID.parse(eidWithNonMatchingControlKey).shouldBeLeft().shouldBeInstanceOf<ParsingError.ControlKeyDoesNotMatch>()
    }

    //could isolate different tests for each variant of invalid control key
    //see `should reject negative years` for unmerged example (see pros / cons there)
    @Property
    fun `should reject invalid control keys`(
        @ForAll("invalidControlKeys") invalidControlKey: String,
        @ForAll("validEidBuilders") eidBuilder: EidStringBuilder
    ) {
        val input = eidBuilder.withControlKeyOverride(invalidControlKey).build()

        EID.parse(input)
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.InvalidControlKey>()
    }

    @Property
    fun `should parse control key`(@ForAll("validEids") validEid: String) {
        val parsed = EID.parse(validEid)

        val expectedControlKey = EID.calculateControlKey(validEid).getOrNull()
        parsed.shouldBeRight().controlKey shouldBe expectedControlKey
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

    @Provide
    fun eidsWithNonDigitChars(): Arbitrary<String> {
        return Arbitraries.strings().ofLength(8).filter { containsNonDigitCharacters(it) }
    }

    @Provide
    fun eidsWithNonMatchingControlKey(): Arbitrary<String> {
        return Combinators.combine(validEids(), validControlKeys())
            .filter { validEid, nonMatchingControlKeyCandidate ->
                val matchingControlKey = validEid.substring(6..7).toInt()
                nonMatchingControlKeyCandidate != matchingControlKey
            }
            .`as` { validEid, nonMatchingControlKey ->
                validEid.substring(0..5) + nonMatchingControlKey.toZeroPaddedString(2)
            }
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
        return Arbitraries.strings().ofLength(2).filter { containsNonDigitCharacters(it) }
    }

    @Provide
    fun validSerialNumbers(): Arbitrary<Int> {
        return Arbitraries.integers().between(1, 999)
    }

    @Provide
    fun serialNumbersContainingNonDigits(): Arbitrary<String> {
        return Arbitraries.strings().ofLength(3).filter { containsNonDigitCharacters(it) }
    }

    @Provide
    fun validControlKeys(): Arbitrary<Int> {
        return Arbitraries.integers().between(1, 97)
    }

    @Provide
    fun invalidControlKeys(): Arbitrary<String> {
        val belowRange = Arbitraries.integers().between(-9, 0).map { it.toZeroPaddedString(2) }
        val aboveRange = Arbitraries.integers().between(98, 99).map { it.toZeroPaddedString(2) }
        val containingNonDigits = Arbitraries.strings().ofLength(2).filter { containsNonDigitCharacters(it) }
        return Arbitraries.oneOf(belowRange, aboveRange, containingNonDigits)
    }

    private fun containsNonDigitCharacters(it: String) = !it.all { it in '0'..'9' }

}
package eid

import arrow.core.right
import io.kotest.assertions.arrow.core.shouldBeLeft
import io.kotest.assertions.arrow.core.shouldBeRight
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.map
import io.kotest.core.spec.style.FunSpec

class EIDTest : FunSpec({
    test("should reject EIDs that are too short") {
        checkAll(Arb.string(maxSize = 7)) { tooShortForEid ->
            EID.parse(tooShortForEid)
                .shouldBeLeft()
                .shouldBeInstanceOf<ParsingError.InputTooShort>()
        }
    }

    test("should reject EIDs that are too long") {
        checkAll(Arb.string(minSize = 9)) { tooLongForEid ->
            EID.parse(tooLongForEid)
                .shouldBeLeft()
                .shouldBeInstanceOf<ParsingError.InputTooLong>()
        }
    }

    val validGenders = Arb.enum<ElfGender>()
    val validYears = Arb.int(0, 99)
    val validSerialNumbers = Arb.int(1, 999)

    val validEidBuilders = arbitrary {
        EidStringBuilder()
            .withGender(validGenders.next())
            .withYear(validYears.next())
            .withSerialNumber(validSerialNumbers.next())
    }

    val validEids = validEidBuilders.map { it.build() }

    test("should accept valid EIDs") {
        checkAll(validEids) { validEid ->
            EID.parse(validEid)
                .shouldBeRight()
                .toString() shouldBe validEid
        }
    }

    test("should parse genders") {
        checkAll(validGenders, validEidBuilders) { elfGender, eidBuilder ->
            val input = eidBuilder.withGender(elfGender).build()

            EID.parse(input)
                .shouldBeRight()
                .gender shouldBe elfGender
        }
    }

    val validGenderChars = setOf('1', '2', '3')
    val invalidGenders = Arb.string(1).filter { it[0] !in validGenderChars }
    test("should reject invalid genders") {
        checkAll(invalidGenders, validEidBuilders) { invalidGenderString, eidBuilder ->
            val input = eidBuilder.withGenderString(invalidGenderString).build()

            EID.parse(input)
                .shouldBeLeft()
                .shouldBeInstanceOf<ParsingError.InvalidElfGender>()
        }
    }

    test("should parse year") {
        checkAll(validYears, validEidBuilders) { year, eidBuilder ->
            val input = eidBuilder.withYear(year).build()

            EID.parse(input)
                .shouldBeRight()
                .year shouldBe year
        }
    }

    test("should reject negative years") {
        checkAll(Arb.int(-9, -1), validEidBuilders) { negativeYear, eidBuilder ->
            val input = eidBuilder.withYear(negativeYear).build()

            EID.parse(input)
                .shouldBeLeft()
                .shouldBeInstanceOf<ParsingError.InvalidYear>()
        }
    }

    fun containsNonDigitCharacters(it: String) = !it.all { it in '0'..'9' }
    val yearsContainingNonDigits = Arb.string(2).filter { containsNonDigitCharacters(it) }

    test("should reject non-number years") {
        checkAll(yearsContainingNonDigits, validEidBuilders) { invalidYear, eidBuilder ->
            val input = eidBuilder.withYearString(invalidYear).build()

            EID.parse(input)
                .shouldBeLeft()
                .shouldBeInstanceOf<ParsingError.InvalidYear>()
        }
    }

    test("should reject year containing + despite being valid number - bug") {
        val invalidYear = "+0"
        EID.parse("1${invalidYear}00100")
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.InvalidYear>()
    }

    test("should parse valid serial number") {
        checkAll(validSerialNumbers, validEidBuilders) { serialNumber, eidBuilder ->
            val input = eidBuilder.withSerialNumber(serialNumber).build()

            EID.parse(input)
                .shouldBeRight()
                .serialNumber shouldBe serialNumber
        }
    }

    test("should reject negative serial numbers and zero") {
        checkAll(Arb.int(-99, 0), validEidBuilders) { zeroOrNegativeSerialNumber, eidBuilder ->
            val input = eidBuilder.withSerialNumber(zeroOrNegativeSerialNumber).build()

            EID.parse(input)
                .shouldBeLeft()
                .shouldBeInstanceOf<ParsingError.InvalidSerialNumber>()
        }
    }

    val serialNumbersContainingNonDigits = Arb.string(3).filter { containsNonDigitCharacters(it) }

    test("should reject non-number serial numbers") {
        checkAll(serialNumbersContainingNonDigits, validEidBuilders) { serialNumberContainingNonDigit, eidBuilder ->
            val input = eidBuilder.withSerialNumberString(serialNumberContainingNonDigit).build()

            EID.parse(input)
                .shouldBeLeft()
                .shouldBeInstanceOf<ParsingError.InvalidSerialNumber>()
        }
    }

    test("should calculate control key for Jerceval's EID") {
        EID.calculateControlKey("198007xx") shouldBe 67.right()
    }

    test("should calculate valid control keys for valid EIDs") {
        checkAll(validEids) { validEid ->
            EID.calculateControlKey(validEid)
                .shouldBeRight()
                .shouldBeInRange(1..97)
        }
    }

    val eidsWithNonDigitChars = Arb.string(8).filter { containsNonDigitCharacters(it) }

    test("should reject control key calculation for non-numeric EIDs") {
        checkAll(eidsWithNonDigitChars) { validEid ->
            EID.calculateControlKey(validEid)
                .shouldBeLeft()
                .shouldBeInstanceOf<ParsingError.CouldNotCalculateControlKey>()
        }
    }

    test("should reject generating control keys outside valid range - bug") {
        EID.calculateControlKey("-00003xx")
            .shouldBeLeft()
            .shouldBeInstanceOf<ParsingError.CouldNotCalculateControlKey>()
    }

    test("should accept Jerceval's EID with matching control key") {
        EID.parse("19800767").shouldBeRight()
    }

    test("should reject Jerceval's EID with non-matching control key") {
        EID.parse("19800766").shouldBeLeft().shouldBeInstanceOf<ParsingError.ControlKeyDoesNotMatch>()
    }

    val validControlKeys = Arb.int(1, 97)

    val eidsWithNonMatchingControlKey =
        Arb.zip(validEids, validControlKeys) { validEid, nonMatchingControlKeyCandidate ->
            validEid.substring(0..5) + nonMatchingControlKeyCandidate.toZeroPaddedString(2)
        }.filter {
            val payloadControlKey = EID.calculateControlKey(it).getOrNull()
            val nonMatchingControlKeyCandidate = it.substring(6..7).toInt()
            payloadControlKey != nonMatchingControlKeyCandidate
        }

    test("should reject EIDs with non-matching control key") {
        checkAll(eidsWithNonMatchingControlKey) { eidWithNonMatchingControlKey ->
            EID.parse(eidWithNonMatchingControlKey)
                .shouldBeLeft()
                .shouldBeInstanceOf<ParsingError.ControlKeyDoesNotMatch>()
        }
    }

    val invalidControlKeys = {
        val belowRange = Arb.int(-9, 0).map { it.toZeroPaddedString(2) }
        val aboveRange = Arb.int(98, 99).map { it.toZeroPaddedString(2) }
        val containingNonDigits = Arb.string(2).filter { containsNonDigitCharacters(it) }
        Arb.choice(belowRange, aboveRange, containingNonDigits)
    }

    test("should reject invalid control keys") {
        checkAll(invalidControlKeys(), validEidBuilders) { invalidControlKey, eidBuilder ->
            val input = eidBuilder.withControlKeyOverride(invalidControlKey).build()

            EID.parse(input)
                .shouldBeLeft()
                .shouldBeInstanceOf<ParsingError.InvalidControlKey>()
        }
    }

    test("should parse control key") {
        checkAll(validEids) { validEid ->
            val parsed = EID.parse(validEid)

            val expectedControlKey = EID.calculateControlKey(validEid).getOrNull()
            parsed.shouldBeRight().controlKey shouldBe expectedControlKey
        }
    }
})
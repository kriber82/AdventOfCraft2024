import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll


class EIDTest : DescribeSpec({
    val jercivalsValidSampleEid = EID.fromCompleteIdentifier(19800767)
    val jercivalsEidPayload = EidPayload.fromParts(1, 98, 7)
    val jercivalsEidControlKey = EidControlKey(67)

    val validSex = 1
    val validYear = 0
    val validSerialNumber = 1

    describe("EidValidator") {

        it("should successfully validate sample EID") {
            EidValidator.isValid(jercivalsValidSampleEid) shouldBe true
        }

        describe("control key validation") {
            it("should return false for invalid control digits") {
                val invalidControlKey = 99
                val invalidEid = EID.fromParts(validSex, validYear, validSerialNumber, invalidControlKey)
                EidValidator.isValid(invalidEid) shouldBe false
            }

            it ("should return true for valid control digits") {
                val validEid = EID.fromParts(validSex, validYear, validSerialNumber)
                EidValidator.isValid(validEid) shouldBe true
            }
        }


        describe("sex validation") {
            it("should return false for sexes greater than 3") {
                val invalidSex = 4
                val invalidEidDueToSex = EID.fromParts(invalidSex, validYear, validSerialNumber)
                EidValidator.isValid(invalidEidDueToSex) shouldBe false
            }

            it("should return false for sex 0") {
                val invalidSex = 0
                val invalidEidDueToSex = EID.fromParts(invalidSex, validYear, validSerialNumber)
                EidValidator.isValid(invalidEidDueToSex) shouldBe false
            }

            it("should return true for valid sexes") {
                checkAll(Arb.int(1..3)) { validSex ->
                    EidValidator.isValid(EID.fromParts(validSex, validYear, validSerialNumber)) shouldBe true
                }
            }
        }

        describe("birth year validation") {
            it("should return true for valid birth years") {
                checkAll(Arb.int(0..99)) { validYear ->
                    EidValidator.isValid(EID.fromParts(validSex, validYear, validSerialNumber)) shouldBe true
                }
            }

            /*
            it("should return false for birth years greater than 99") {
                val invalidYear = 100
                val invalidEidDueToYear = EID.fromParts(validSex, invalidYear, validSerialNumber)
                EidValidator.isValid(invalidEidDueToYear) shouldBe false
            }

            it("should return false for birth years less than 0") {
                val invalidYear = -1
                val invalidEidDueToYear = EID.fromParts(validSex, invalidYear, validSerialNumber)
                EidValidator.isValid(invalidEidDueToYear) shouldBe false
            }
             */
        }
    }

    describe("EID") {
        it("should be constructable from individual parts") {
            EID.fromParts(1, 98, 7, 67) shouldBe jercivalsValidSampleEid
            EID.fromParts(1, 23, 456, 78) shouldBe EID.fromCompleteIdentifier(12345678)
            EID.fromParts(1, 0, 0,1) shouldBe EID.fromCompleteIdentifier(10000001)
        }

        it("should be constructable from individual parts without explicitly providing control key") {
            EID.fromParts(1, 98, 7) shouldBe jercivalsValidSampleEid
        }

        it("should be constructable from valid full EID number") {
            jercivalsValidSampleEid.payload shouldBe jercivalsEidPayload
            jercivalsValidSampleEid.controlKey shouldBe jercivalsEidControlKey
        }

        it("should be constructable from invalid full EID number") {
            val constructed = EID.fromCompleteIdentifier(12345678)
            constructed.payload shouldBe EidPayload.fromParts(1,23,456)
            constructed.controlKey shouldBe EidControlKey(78)
        }

    }

    describe("EidPayload") {
        it("should compute valid control digits for payload digits for sample EID") {
            jercivalsEidPayload.computeValidControlKey() shouldBe jercivalsEidControlKey
        }

        it("should compute valid control digits for payload digits for arbitrary EID") {
            /*
                eid payload = first six digits = 123456
                payload modulo 97 = 123456 % 97 = 72
                complement of payload modulo 97 = 97 - payload modulo 97 = 97 - 72 = 25
             */
            EidPayload.fromParts(1,23,456).computeValidControlKey() shouldBe EidControlKey(25)
        }

    }
})
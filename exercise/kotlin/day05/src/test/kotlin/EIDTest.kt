import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe


class EIDTest : DescribeSpec({
    val jercivalsValidSampleEid = EID.fromCompleteIdentifier(19800767)
    val jercivalsEidPayload = EidPayload(198007)
    val jercivalsEidControlKey = EidControlKey(67)

    val validSex = 1
    val validYear = 0
    val validSerialNumber = 1

    describe("EidValidator") {

        describe ("sample EID") {

            it("should successfully validate") {
                EidValidator.isValid(jercivalsValidSampleEid) shouldBe true
            }

            it("should compute valid control digits for payload digits") {
                jercivalsEidPayload.computeValidControlKey() shouldBe jercivalsEidControlKey
            }
        }

        describe ("EID 12345678 - invalid due to control digits") {

            it("should compute valid control digits for payload digits") {
                /*
                    eid payload = first six digits = 123456
                    payload modulo 97 = 123456 % 97 = 72
                    complement of payload modulo 97 = 97 - payload modulo 97 = 97 - 72 = 25
                 */
                EidPayload(123456).computeValidControlKey() shouldBe EidControlKey(25)
            }

            it("should not validate due to control digits") {
                EidValidator.isValid(EID.fromCompleteIdentifier(12345678)) shouldBe false

                //test with valid control digits
                EidValidator.isValid(EID.fromCompleteIdentifier(12345625)) shouldBe true
            }
        }

        describe("sex validation") {
            it("should return false for sexes greater than 3") {
                val invalidSex = 4
                val invalidEidDueToSex = EID.fromParts(invalidSex, validYear, validSerialNumber)
                EidValidator.isValid(invalidEidDueToSex) shouldBe false
            }

            /*
            it("should return true for valid sexes") {
                val validYear = 0
                val validSerialNumber = 1
                val invalidEid = EID.fromParts(1, validYear, validSerialNumber, 0)
                val validEid = EID.fromParts(1, validYear, validSerialNumber, EidValidator.computeValidControlKey(invalidEid))
                EidValidator.isValid(validEid) shouldBe true
            }
             */
        }
    }

    describe("EID") {
        describe("sample EID") {
            it("should extract payload digits") {
                jercivalsValidSampleEid.payload shouldBe jercivalsEidPayload
            }

            it("should extract control digits") {
                jercivalsValidSampleEid.controlKey shouldBe jercivalsEidControlKey
            }

        }

        describe("EID 12345678") {
            it("should extract payload digits") {
                EID.fromCompleteIdentifier(12345678).payload shouldBe EidPayload(123456)
            }

            it("should extract control digits") {
                EID.fromCompleteIdentifier(12345678).controlKey shouldBe EidControlKey(78)
            }
        }

        describe("should be constructable from individual parts") {
            EID.fromParts(1, 98, 7, 67) shouldBe jercivalsValidSampleEid
            EID.fromParts(1, 23, 456, 78) shouldBe EID.fromCompleteIdentifier(12345678)
            EID.fromParts(1, 0, 0,1) shouldBe EID.fromCompleteIdentifier(10000001)
        }

    }

    describe("EidPayload") {
        it("should return the elf sex") {
            jercivalsEidPayload.getGenderPart() shouldBe 1
        }
    }
})
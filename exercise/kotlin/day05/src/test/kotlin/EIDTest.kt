import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe


class EIDTest : DescribeSpec({
    val jercivalsValidSampleEid = EID(19800767)
    val jercivalsEidPayload = 198007
    val jercivalsEidControlKey = 67

    describe("EidValidator") {

        describe ("sample EID") {

            it("should successfully validate") {
                EidValidator.isValid(jercivalsValidSampleEid) shouldBe true
            }

            it("should compute valid control digits for payload digits") {
                EidValidator.computeValidControlKey(jercivalsValidSampleEid) shouldBe jercivalsEidControlKey
            }
        }

        describe ("EID 12345678 - invalid due to control digits") {

            it("should compute valid control digits for payload digits") {
                /*
                    eid = 12345678
                    eid payload = first six digits = 123456
                    payload modulo 97 = 123456 % 97 = 72
                    complement of payload modulo 97 = 97 - payload modulo 97 = 97 - 72 = 25
                 */
                EidValidator.computeValidControlKey(EID(12345678)) shouldBe 25
            }

            it("should not validate due to control digits") {
                EidValidator.isValid(EID(12345678)) shouldBe false

                //test with valid control digits
                EidValidator.isValid(EID(12345625)) shouldBe true
            }
        }

        describe("sex validation") {
            it("should return false for sexes greater than 3") {
                val invalidSex = 4
                val validYear = 0
                val validSerialNumber = 1
                val invalidEidWithInvalidControlKey = EID.fromParts(invalidSex, validYear, validSerialNumber, 0)
                val invalidEidWithValidControlKey = EID.fromParts(invalidSex, validYear, validSerialNumber, EidValidator.computeValidControlKey(invalidEidWithInvalidControlKey))
                EidValidator.isValid(invalidEidWithValidControlKey) shouldBe false
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
                jercivalsValidSampleEid.getPayloadDigits() shouldBe jercivalsEidPayload
            }

            it("should extract control digits") {
                jercivalsValidSampleEid.getControlDigits() shouldBe jercivalsEidControlKey
            }

            it("should return the elf identifier") {
                jercivalsValidSampleEid.getGenderPart() shouldBe 1
            }
        }

        describe("EID 12345678") {
            it("should extract payload digits") {
                EID(12345678).getPayloadDigits() shouldBe 123456
            }

            it("should extract control digits") {
                EID(12345678).getControlDigits() shouldBe 78
            }
        }

        describe("should be constructable from individual parts") {
            EID.fromParts(1, 98, 7, 67) shouldBe jercivalsValidSampleEid
            EID.fromParts(1, 23, 456, 78) shouldBe EID(12345678)
            EID.fromParts(1, 0, 0,1) shouldBe EID(10000001)
        }

    }
})
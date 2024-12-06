import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class EIDTest : DescribeSpec({
    val jercivalsValidSampleEid = EID(19800767)
    val jercivalsEidPayload = 198007
    val jercivalsEidControlKey = 67

    val eidOneThroughEight = EID(12345678)

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
    }

    describe("EID") {
        describe("sample EID") {
            it("should extract payload digits") {
                jercivalsValidSampleEid.getPayloadDigits() shouldBe jercivalsEidPayload
            }

            it("should extract control digits") {
                jercivalsValidSampleEid.getControlDigits() shouldBe jercivalsEidControlKey
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
    }
})
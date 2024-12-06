import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe


class EIDTest : StringSpec({
    val jercivalsValidSampleEid = EID(19800767)
    val jercivalsEidPayload = 198007
    val jercivalsEidControlKey = 67

    "should validate sample EID" {
        EidValidator.isValid(jercivalsValidSampleEid) shouldBe true
    }

    "should extract payload digits from sample EID" {
        jercivalsValidSampleEid.getPayloadDigits() shouldBe jercivalsEidPayload
    }

    "should extract payload digits from other EIDs" {
        EID(12345678).getPayloadDigits() shouldBe 123456
    }

    "should compute valid control digits for payload digits" {
        EidValidator.computeValidControlKey(jercivalsValidSampleEid) shouldBe jercivalsEidControlKey

        /*
            eid = 12345678
            eid payload = first six digits = 123456
            payload modulo 97 = 123456 % 97 = 72
            complement of payload modulo 97 = 97 - payload modulo 97 = 97 - 72 = 25
         */
        EidValidator.computeValidControlKey(EID(12345678)) shouldBe 25
    }
})
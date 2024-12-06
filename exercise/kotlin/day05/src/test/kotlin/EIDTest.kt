import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EIDTest : StringSpec({
    val jercivalsValidSampleEid = EID(19800767)

    "should validate sample EID" {
        EidValidator.isValid(jercivalsValidSampleEid) shouldBe true
    }

    "should extract payload digits from sample EID" {
        jercivalsValidSampleEid.getPayloadDigits() shouldBe 198007 //primitive obsession?
    }
})
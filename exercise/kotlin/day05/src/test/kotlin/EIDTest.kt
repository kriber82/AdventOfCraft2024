import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class EIDTest : StringSpec({
    "Sample EID is valid" {
        EidValidator.isValid(EID(19800767)) shouldBe true
    }
})
package eid

import io.kotest.matchers.shouldBe
import net.jqwik.api.ForAll
import net.jqwik.api.Property
import net.jqwik.api.constraints.StringLength

class EIDTest {
    @Property
    fun shouldRejectEIDsThatAreTooShort(@ForAll @StringLength(min = 0, max = 7) tooShortForEid: String) {
        EID.parse(tooShortForEid).leftOrNull() shouldBe ParsingError.InputTooShort()
    }

    @Property
    fun `should reject EIDs that are too long`(@ForAll @StringLength(min = 9) tooLongForEid: String) {
        EID.parse(tooLongForEid).leftOrNull() shouldBe ParsingError.InputTooLong()
    }
}
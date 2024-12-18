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
}
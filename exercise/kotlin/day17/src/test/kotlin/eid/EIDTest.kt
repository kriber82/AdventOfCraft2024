package eid

import io.kotest.matchers.shouldBe
import net.jqwik.api.*
import net.jqwik.api.constraints.StringLength

class EIDTest {

    val genders = Arbitraries.strings().ofLength(1)
    val years = Arbitraries.strings().ofLength(2)
    val serialNumbers = Arbitraries.strings().ofLength(3)
    val controlKeys = Arbitraries.strings().ofLength(2)

    @Provide
    fun validEids(): Arbitrary<String> {
        return Combinators.combine(genders, years, serialNumbers, controlKeys)
            .`as` { s1, s2, s3, s4 -> "$s1$s2$s3$s4" }
    }

    @Property
    fun shouldRejectEIDsThatAreTooShort(@ForAll @StringLength(max = 7) tooShortForEid: String) {
        EID.parse(tooShortForEid).leftOrNull() shouldBe ParsingError.InputTooShort()
    }

    @Property
    fun `should reject EIDs that are too long`(@ForAll @StringLength(min = 9) tooLongForEid: String) {
        EID.parse(tooLongForEid).leftOrNull() shouldBe ParsingError.InputTooLong()
    }

    @Property
    fun `should accept valid EIDs`(@ForAll("validEids") validEid: String) {
        EID.parse(validEid).getOrNull().toString() shouldBe validEid
    }
}
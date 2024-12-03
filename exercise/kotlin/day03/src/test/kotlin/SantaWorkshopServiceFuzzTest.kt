import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.FuzzTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import preparation.SantaWorkshopService

class SantaWorkshopServiceFuzzTest {

    lateinit var service: SantaWorkshopService

    @BeforeEach
    fun beforeEach() {
        service = SantaWorkshopService()
    }
    @Test
    fun test1() {
        assertEquals("foo", "foo")
    }

    @FuzzTest
    fun `should prepare a gift with valid parameters`(data: FuzzedDataProvider) {
            val giftName = data.consumeString(129)
            val weight = data.consumeDouble()
            val color = "Purple"
            val material = "Plastic1"

            assertNotNull(service.prepareGift(giftName, weight, color, material))
    }
}
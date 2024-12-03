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
    @FuzzTest(maxExecutions = 1000000)
    fun `should prepare a gift with valid parameters`(data: FuzzedDataProvider) {
            val giftName = data.consumeString(1025)
            val weight = data.consumeRegularDouble(Double.MIN_VALUE, 5.0)
            val color = data.consumeString(1025)
            val material = data.consumeString(1025)

            assertNotNull(service.prepareGift(giftName, weight, color, material))
    }
}
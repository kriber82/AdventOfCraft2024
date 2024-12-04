import com.code_intelligence.jazzer.api.FuzzedDataProvider
import com.code_intelligence.jazzer.junit.FuzzTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import preparation.Gift
import preparation.SantaWorkshopService

class SantaWorkshopServiceFuzzTest {

    private lateinit var service: SantaWorkshopService

    @BeforeEach
    fun beforeEach() {
        service = SantaWorkshopService()
    }

    @FuzzTest(maxDuration = "1s")
    fun runAllFuzzTests(data: FuzzedDataProvider) {
        shouldPrepareAGiftWithValidParameters(data)
        shouldRetrieveSensibleValuesForRecommendedAge(data)
        shouldBeAbleToSetAnyAttribute(data)
        shouldRetrieveAnAttributeToAGift(data)
        shouldRetrieveAllIntValuesForRecommendedAge(data)
    }

    private fun shouldPrepareAGiftWithValidParameters(data: FuzzedDataProvider) {
            val giftName = data.consumeString(1025)
            val weight = data.consumeRegularDouble(Double.MIN_VALUE, 5.0)
            val color = data.consumeString(1025)
            val material = data.consumeString(1025)

            assertNotNull(service.prepareGift(giftName, weight, color, material))
    }

    private fun shouldRetrieveAnAttributeToAGift(data: FuzzedDataProvider) {
        val giftName = "Furby"
        val weight = 1.0
        val color = "Multi"
        val material = "Cotton"

        val gift = Gift(giftName, weight, color, material)
        gift.addAttribute("recommendedAge", data.consumeString(1025))

        //ensure getRecommended Age does not throw
        gift.getRecommendedAge()
    }

    private fun shouldRetrieveSensibleValuesForRecommendedAge(data: FuzzedDataProvider) {
        val giftName = "Furby"
        val weight = 1.0
        val color = "Multi"
        val material = "Cotton"

        val gift = Gift(giftName, weight, color, material)
        val recommendedAge = data.consumeInt(0, 200)
        gift.addAttribute("recommendedAge", recommendedAge.toString())

        //ensure getRecommended Age does not throw
        gift.getRecommendedAge() shouldBe recommendedAge
    }

    private fun shouldRetrieveAllIntValuesForRecommendedAge(data: FuzzedDataProvider) {
        val giftName = "Furby"
        val weight = 1.0
        val color = "Multi"
        val material = "Cotton"

        val gift = Gift(giftName, weight, color, material)
        val recommendedAge = data.consumeInt()
        gift.addAttribute("recommendedAge", recommendedAge.toString())

        //ensure getRecommended Age does not throw
        gift.getRecommendedAge() shouldBe recommendedAge
    }

    private fun shouldBeAbleToSetAnyAttribute(data: FuzzedDataProvider) {
        val giftName = "Furby"
        val weight = 1.0
        val color = "Multi"
        val material = "Cotton"

        val gift = Gift(giftName, weight, color, material)
        gift.addAttribute(data.consumeString(1025), data.consumeString(1025))

        gift.getRecommendedAge() shouldBe 0
    }
}
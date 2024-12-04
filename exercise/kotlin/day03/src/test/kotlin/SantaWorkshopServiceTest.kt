import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import preparation.Gift
import preparation.SantaWorkshopService

class SantaWorkshopServiceTest : DescribeSpec({
    val service = SantaWorkshopService()

    describe("prepareGift") {
        it("should prepare a gift with required parameters") {
            val giftName = "Bitzee"
            val weight = 3.0
            val color = "Purple"
            val material = "Plastic"

            service.prepareGift(giftName, weight, color, material) shouldNotBe null
        }

        it("should store optional recommendedAge on gift") {
            val giftName = "Furby"
            val weight = 1.0
            val color = "Multi"
            val material = "Cotton"

            val gift = Gift(giftName, weight, color, material, 3)

            gift.recommendedAge shouldBe 3
        }

        it("should fail if gift is too heavy") {
            val giftName = "Dog-E"
            val weight = 6.0
            val color = "White"
            val material = "Metal"

            shouldThrow<IllegalArgumentException> {
                service.prepareGift(giftName, weight, color, material)
            }.message shouldBe "Gift is too heavy for Santa's sleigh"
        }
    }
})
package santamarket.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import santamarket.model.offer.ItemBundleForDiscountedPrice
import santamarket.model.offer.SpecialOfferType

class SantamarketTestDescribe : DescribeSpec({
    describe("ShoppingSleigh") {
        it("should be empty initially") {
            val scenario = TestScenarioBuilder().build()

            scenario.sleigh.getItems() shouldBe emptyList()
            scenario.sleigh.productQuantities() shouldBe emptyMap()
        }

        it("should add single item") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddybear", 1, ProductUnit.EACH, 1.0)
                .build()
            val teddybear = scenario.catalog.product("teddybear")!!

            scenario.sleigh.getItems() shouldContainExactly listOf(ProductQuantity(teddybear, 1.0))
        }

        it("should add multiple items") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddybear", 3, ProductUnit.EACH, 1.0)
                .build()
            val teddybear = scenario.catalog.product("teddybear")!!

            scenario.sleigh.getItems() shouldContainExactly List(3) { (ProductQuantity(teddybear, 1.0)) }
        }

        it("should add single item with quantity") {
            val scenario = TestScenarioBuilder()
                .withProduct("teddybear", 2.0, ProductUnit.EACH, 1.0)
                .build()
            val teddybear = scenario.catalog.product("teddybear")!!

            scenario.sleigh.getItems() shouldContainExactly listOf(ProductQuantity(teddybear, 2.0))
        }

        it("should add multiple items with quantity") {
            val scenario = TestScenarioBuilder()
                .withProduct("teddybear", 2.0, ProductUnit.EACH, 1.0)
                .withProduct("teddybear", 1.0, ProductUnit.EACH, 1.0)
                .build()
            val teddybear = scenario.catalog.product("teddybear")!!

            scenario.sleigh.getItems() shouldContainExactly listOf(
                ProductQuantity(teddybear, 2.0),
                ProductQuantity(teddybear, 1.0))
        }

        it("should return 1.0 as quantity of single item") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddybear", 1, ProductUnit.EACH, 1.0)
                .build()
            val teddybear = scenario.catalog.product("teddybear")!!

            scenario.sleigh.productQuantities() shouldContainExactly mapOf(Pair(teddybear, 1.0))
        }

        it("should return amount of items as quantity of multiple items ") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddybear", 3, ProductUnit.EACH, 1.0)
                .build()
            val teddybear = scenario.catalog.product("teddybear")!!

            scenario.sleigh.productQuantities() shouldContainExactly mapOf(Pair(teddybear, 3.0))
        }

        it("should return item given quantity for item with quantity") {
            val scenario = TestScenarioBuilder()
                .withProduct("teddybear", 2.0, ProductUnit.EACH, 1.0)
                .build()
            val teddybear = scenario.catalog.product("teddybear")!!

            scenario.sleigh.productQuantities() shouldContainExactly mapOf(Pair(teddybear, 2.0))
        }

        it("should return sum of given quantities for multiple items with quantity") {
            val scenario = TestScenarioBuilder()
                .withProduct("teddybear", 2.0, ProductUnit.EACH, 1.0)
                .withProduct("teddybear", 1.0, ProductUnit.EACH, 1.0)
                .build()
            val teddybear = scenario.catalog.product("teddybear")!!

            scenario.sleigh.productQuantities() shouldContainExactly mapOf(Pair(teddybear, 3.0))
        }

        it("should be able to contain different items") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddybear", 1, ProductUnit.EACH, 1.0)
                .withProduct("teddybear", 2.0, ProductUnit.EACH, 1.0)
                .withRepeatedSingleProduct("turkey", 1, ProductUnit.EACH, 1.0)
                .withProduct("turkey", 3.0, ProductUnit.EACH, 1.0)
                .build()
            val teddybear = scenario.catalog.product("teddybear")!!
            val turkey = scenario.catalog.product("turkey")!!

            scenario.sleigh.getItems() shouldContainExactly listOf(
                ProductQuantity(teddybear, 1.0),
                ProductQuantity(teddybear, 2.0),
                ProductQuantity(turkey, 1.0),
                ProductQuantity(turkey, 3.0))
        }

        it("should return the sum of quantities for each product") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddybear", 1, ProductUnit.EACH, 1.0)
                .withProduct("teddybear", 2.0, ProductUnit.EACH, 1.0)
                .withRepeatedSingleProduct("turkey", 1, ProductUnit.EACH, 1.0)
                .withProduct("turkey", 3.0, ProductUnit.EACH, 1.0)
                .build()
            val teddybear = scenario.catalog.product("teddybear")!!
            val turkey = scenario.catalog.product("turkey")!!

            scenario.sleigh.productQuantities() shouldContainExactly mapOf(
                Pair(teddybear, 3.0),
                Pair(turkey, 4.0),)
        }
    }

    describe("ChristmasElf fills recipe") {
        describe("total") {
            it("should be 0 without items in sleigh") {
                val scenario = TestScenarioBuilder().build()

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (0.0 plusOrMinus 0.001)
            }

            it("should contain price of single item in sleigh") {
                val scenario = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddybear", 1, ProductUnit.EACH, 5.0)
                    .build()

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (5.0 plusOrMinus 0.001)
            }

            it("should multiply unit price for multiple items of same type in sleigh") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddybear", 2.0, ProductUnit.EACH, 5.0)
                    .build()

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (2.0 * 5.0 plusOrMinus 0.001)
            }

            it("should contain sum of multiplied unit prices for several items in sleigh") {
                val scenario = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddybear", 2, ProductUnit.EACH, 5.0)
                    .withProduct("turkey", 3.0, ProductUnit.KILO, 2.5)
                    .withProduct("turkey", 1.5, ProductUnit.KILO, 2.5)
                    .build()

                val receipt = scenario.checkout()

                val expectedTotalPrice = 2.0 * 5.0 + 3.0 * 2.5 + 1.5 * 2.5
                receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
            }

            it("should subtract discount from undiscounted total") {
                val scenario = TestScenarioBuilder()
                    .withProduct("turkey", 2.0, ProductUnit.KILO, 2.5)
                    .withTenPercentDiscount("turkey")
                    .build()

                val receipt = scenario.checkout()

                val expectedNonDiscountedPrice = 2.0 * 2.5
                val expectedTotalPrice = expectedNonDiscountedPrice * 0.9
                receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
            }
        }

        describe("list of items") {
            it("should contain one line with unit price for single item") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddyBear", 1.0, ProductUnit.EACH, 1.0)
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!

                val receipt = scenario.checkout()

                receipt.getItems() shouldContainExactly listOf(ReceiptItem(teddyBear, 1.0, 1.0, 1.0))
            }

            it("should contain one line with total price for single item with quality above 1") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddyBear", 3.0, ProductUnit.EACH, 1.0)
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!

                val receipt = scenario.checkout()

                receipt.getItems() shouldContainExactly listOf(ReceiptItem(teddyBear, 3.0, 1.0, 3.0))
            }

            it("should contain individual lines for products with several items") {
                val scenario = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddyBear", 2, ProductUnit.EACH, 1.5)
                    .withRepeatedSingleProduct("turkey", 1, ProductUnit.EACH, 2.0)
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!
                val turkey = scenario.catalog.product("turkey")!!

                val receipt = scenario.checkout()

                receipt.getItems() shouldContainExactly listOf(
                    ReceiptItem(teddyBear, 1.0, 1.5, 1.5),
                    ReceiptItem(teddyBear, 1.0, 1.5, 1.5),
                    ReceiptItem(turkey, 1.0, 2.0, 2.0))
            }

            it("should ignore discounts in calculated line price") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddyBear", 1.0, ProductUnit.EACH, 1.0)
                    .withTenPercentDiscount("teddyBear")
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!

                val receipt = scenario.checkout()

                receipt.getItems() shouldContainExactly listOf(ReceiptItem(teddyBear, 1.0, 1.0, 1.0))
                receipt.getTotalPrice() shouldBeLessThan 1.0
                receipt.getDiscounts().size shouldBeGreaterThan 0
            }
        }

        describe("list of discounts") {
            it("should be empty without offers") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddyBear", 1.0, ProductUnit.EACH, 1.0)
                    .build()

                val receipt = scenario.checkout()

                receipt.getDiscounts() shouldBe emptyList()
            }

            it("should contain discount for single item with discount") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddyBear", 1.0, ProductUnit.EACH, 1.0)
                    .withTenPercentDiscount("teddyBear")
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!

                val receipt = scenario.checkout()

                receipt.getDiscounts() shouldContainExactly listOf(Discount(teddyBear, "10.0% off", -0.1))
            }

            it("should countain single discount for several items of the same type") {
                val scenario = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddyBear", 3, ProductUnit.EACH, 1.0)
                    .withTenPercentDiscount("teddyBear")
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!

                val receipt = scenario.checkout()

                receipt.getDiscounts() shouldContainExactly listOf(Discount(teddyBear, "10.0% off", -0.3))
            }

            it("should not contain discount for items with inapplicable offer") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddyBear", 1.0, ProductUnit.EACH, 1.0)
                    .withTwoForOneDiscount("teddyBear")
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!

                val receipt = scenario.checkout()

                receipt.getDiscounts() shouldBe emptyList()
            }
        }
    }

    describe("discounts") {}
})

class SantamarketTest : StringSpec({

    "noDiscount" {
        val teddyBearPrice = 1.0
        val numberOfTeddyBears = 3

        val scenario = TestScenarioBuilder()
            .withProduct("teddyBear", numberOfTeddyBears.toDouble(), ProductUnit.EACH, teddyBearPrice)
            .build()
        val teddyBear = scenario.catalog.product("teddyBear")!!

        val receipt = scenario.checkout()

        val expectedTotalPrice = numberOfTeddyBears * teddyBearPrice
        val expectedReceiptItem =
            ReceiptItem(teddyBear, numberOfTeddyBears.toDouble(), teddyBearPrice, expectedTotalPrice)

        receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
        receipt.getItems() shouldContainExactly listOf(expectedReceiptItem)
    }

    "tenPercentDiscount" {
        val turkeyQuantity = 2.0
        val turkeyPrice = 2.0

        val scenario = TestScenarioBuilder()
            .withProduct("turkey", turkeyQuantity, ProductUnit.KILO, turkeyPrice)
            .withTenPercentDiscount("turkey")
            .build()
        val turkey = scenario.catalog.product("turkey")!!

        val receipt = scenario.checkout()

        val expectedNonDiscountedPrice = turkeyQuantity * turkeyPrice
        val expectedTotalPrice = expectedNonDiscountedPrice * 0.9
        receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)

        val expectedReceiptItem = ReceiptItem(turkey, turkeyQuantity, turkeyPrice, expectedNonDiscountedPrice)
        receipt.getItems() shouldContainExactly listOf(expectedReceiptItem)

        val expectedDiscount = Discount(turkey, "10.0% off", expectedTotalPrice - expectedNonDiscountedPrice)
        receipt.getDiscounts() shouldContainExactly listOf(expectedDiscount)
    }

    "threeForTwoDiscount" {
        val teddyBearPrice = 1.0
        val numberOfTeddyBears = 3

        val scenario = TestScenarioBuilder()
            .withProduct("teddyBear", numberOfTeddyBears.toDouble(), ProductUnit.EACH, teddyBearPrice)
            .withSpecialOffer(SpecialOfferType.THREE_FOR_TWO, "teddyBear", 0.0)
            .build()
        val teddyBear = scenario.catalog.product("teddyBear")!!

        val receipt = scenario.checkout()

        val expectedNonDiscountedPrice = numberOfTeddyBears * teddyBearPrice
        val expectedTotalPrice = 2 * teddyBearPrice
        val expectedReceiptItem =
            ReceiptItem(teddyBear, numberOfTeddyBears.toDouble(), teddyBearPrice, expectedNonDiscountedPrice)
        val expectedDiscount = Discount(teddyBear, "3 for 2", expectedTotalPrice - expectedNonDiscountedPrice)

        receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
        receipt.getItems() shouldContainExactly listOf(expectedReceiptItem)
        receipt.getDiscounts() shouldContainExactly listOf(expectedDiscount)
    }

    "threeForTwoDiscount price other than 1.0" {
        val teddyBearPrice = 2.0
        val numberOfTeddyBears = 3

        val scenario = TestScenarioBuilder()
            .withProduct("teddyBear", numberOfTeddyBears.toDouble(), ProductUnit.EACH, teddyBearPrice)
            .withSpecialOffer(SpecialOfferType.THREE_FOR_TWO, "teddyBear", 0.0)
            .build()
        val teddyBear = scenario.catalog.product("teddyBear")!!

        val receipt = scenario.checkout()

        val expectedNonDiscountedPrice = numberOfTeddyBears * teddyBearPrice
        val expectedTotalPrice = 2 * teddyBearPrice
        val expectedReceiptItem =
            ReceiptItem(teddyBear, numberOfTeddyBears.toDouble(), teddyBearPrice, expectedNonDiscountedPrice)
        val expectedDiscount = Discount(teddyBear, "3 for 2", expectedTotalPrice - expectedNonDiscountedPrice)

        receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
        receipt.getItems() shouldContainExactly listOf(expectedReceiptItem)
        receipt.getDiscounts() shouldContainExactly listOf(expectedDiscount)
    }

    "twoForOneDiscount" {
        val teddyBearPrice = 1.0
        val numberOfTeddyBears = 2

        val scenario = TestScenarioBuilder()
            .withProduct("teddyBear", numberOfTeddyBears.toDouble(), ProductUnit.EACH, teddyBearPrice)
            .withSpecialOffer(SpecialOfferType.TWO_FOR_ONE, "teddyBear", 0.0)
            .build()
        val teddyBear = scenario.catalog.product("teddyBear")!!

        val receipt = scenario.checkout()

        val expectedNonDiscountedPrice = numberOfTeddyBears * teddyBearPrice
        val expectedTotalPrice = 1 * teddyBearPrice
        val expectedReceiptItem =
            ReceiptItem(teddyBear, numberOfTeddyBears.toDouble(), teddyBearPrice, expectedNonDiscountedPrice)
        val expectedDiscount = Discount(teddyBear, "2 for 1", expectedTotalPrice - expectedNonDiscountedPrice)

        receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
        receipt.getItems() shouldContainExactly listOf(expectedReceiptItem)
        receipt.getDiscounts() shouldContainExactly listOf(expectedDiscount)
    }

    "twoForAmountDiscount" {
        val teddyBearPrice = 1.0
        val numberOfTeddyBears = 2
        val discountedPriceForTwoTeddyBears = 1.6

        val scenario = TestScenarioBuilder()
            .withProduct("teddyBear", numberOfTeddyBears.toDouble(), ProductUnit.EACH, teddyBearPrice)
            .withSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, "teddyBear", discountedPriceForTwoTeddyBears)
            .build()
        val teddyBear = scenario.catalog.product("teddyBear")!!

        val receipt = scenario.checkout()

        val expectedNonDiscountedPrice = numberOfTeddyBears * teddyBearPrice
        val expectedTotalPrice = discountedPriceForTwoTeddyBears
        val expectedReceiptItem =
            ReceiptItem(teddyBear, numberOfTeddyBears.toDouble(), teddyBearPrice, expectedNonDiscountedPrice)
        val expectedDiscount = Discount(
            teddyBear,
            "2 for $discountedPriceForTwoTeddyBears",
            expectedTotalPrice - expectedNonDiscountedPrice
        )

        receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
        receipt.getItems() shouldContainExactly listOf(expectedReceiptItem)
        receipt.getDiscounts() shouldContainExactly listOf(expectedDiscount)
    }

    "twoForAmountDiscount - price other than 1.0" {
        val teddyBearPrice = 2.0
        val numberOfTeddyBears = 2
        val discountedPriceForTwoTeddyBears = 3.6

        val scenario = TestScenarioBuilder()
            .withProduct("teddyBear", numberOfTeddyBears.toDouble(), ProductUnit.EACH, teddyBearPrice)
            .withSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, "teddyBear", discountedPriceForTwoTeddyBears)
            .build()
        val teddyBear = scenario.catalog.product("teddyBear")!!

        val receipt = scenario.checkout()

        val expectedNonDiscountedPrice = numberOfTeddyBears * teddyBearPrice
        val expectedTotalPrice = discountedPriceForTwoTeddyBears
        val expectedReceiptItem =
            ReceiptItem(teddyBear, numberOfTeddyBears.toDouble(), teddyBearPrice, expectedNonDiscountedPrice)
        val expectedDiscount = Discount(
            teddyBear,
            "2 for $discountedPriceForTwoTeddyBears",
            expectedTotalPrice - expectedNonDiscountedPrice
        )

        receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
        receipt.getItems() shouldContainExactly listOf(expectedReceiptItem)
        receipt.getDiscounts() shouldContainExactly listOf(expectedDiscount)
    }

    "fiveForAmountDiscount" {
        val teddyBearPrice = 1.0
        val numberOfTeddyBears = 6
        val discountedPriceForFiveTeddyBears = 4.0

        val scenario = TestScenarioBuilder()
            .withRepeatedSingleProduct("teddyBear", numberOfTeddyBears, ProductUnit.EACH, teddyBearPrice)
            .withSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, "teddyBear", discountedPriceForFiveTeddyBears)
            .build()
        val teddyBear = scenario.catalog.product("teddyBear")!!

        val receipt = scenario.checkout()

        val expectedNonDiscountedPrice = numberOfTeddyBears * teddyBearPrice
        val expectedTotalPrice = discountedPriceForFiveTeddyBears + teddyBearPrice
        val expectedReceiptItem = ReceiptItem(teddyBear, 1.0, teddyBearPrice, teddyBearPrice)
        val expectedDiscount = Discount(
            teddyBear,
            "5 for $discountedPriceForFiveTeddyBears",
            expectedTotalPrice - expectedNonDiscountedPrice
        )

        receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
        receipt.getItems() shouldContainExactly List(numberOfTeddyBears) { expectedReceiptItem }
        receipt.getDiscounts() shouldContainExactly listOf(expectedDiscount)
    }

    "twoDifferentItemsInSleighWithDiscountOnOneOfThem" {
        val teddyBearPrice = 1.0
        val numberOfTeddyBears = 3
        val turkeyPrice = 2.0
        val turkeyQuantity = 1.5

        val scenario = TestScenarioBuilder()
            .withProduct("teddyBear", numberOfTeddyBears.toDouble(), ProductUnit.EACH, teddyBearPrice)
            .withProduct("turkey", turkeyQuantity, ProductUnit.KILO, turkeyPrice)
            .withTenPercentDiscount("teddyBear")
            .build()
        val teddyBear = scenario.catalog.product("teddyBear")!!
        val turkey = scenario.catalog.product("turkey")!!

        val receipt = scenario.checkout()

        val expectedNonDiscountedTeddyBearPrice = numberOfTeddyBears * teddyBearPrice
        val teddyBearReceiptItem =
            ReceiptItem(teddyBear, numberOfTeddyBears.toDouble(), teddyBearPrice, expectedNonDiscountedTeddyBearPrice)
        val totalTurkeyPrice = turkeyPrice * turkeyQuantity
        val turkeyReceiptItem = ReceiptItem(turkey, turkeyQuantity, turkeyPrice, totalTurkeyPrice)
        val expectedTotalTeddyBearPrice = expectedNonDiscountedTeddyBearPrice * 0.9
        val expectedTotalPrice = expectedTotalTeddyBearPrice + totalTurkeyPrice
        val expectedDiscount =
            Discount(teddyBear, "10.0% off", expectedTotalTeddyBearPrice - expectedNonDiscountedTeddyBearPrice)

        receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
        receipt.getItems() shouldContainExactly listOf(teddyBearReceiptItem, turkeyReceiptItem)
        receipt.getDiscounts() shouldContainExactly listOf(expectedDiscount)
    }

    "newAddSpecialOfferMethod" {
        val teddyBearPrice = 1.0
        val numberOfTeddyBears = 6
        val discountedPriceForFiveTeddyBears = 4.0

        val scenario = TestScenarioBuilder()
            .withProduct("teddyBear", numberOfTeddyBears.toDouble(), ProductUnit.EACH, teddyBearPrice)
            .build()
        val teddyBear = scenario.catalog.product("teddyBear")!!
        scenario.elf.addSpecialOffer(ItemBundleForDiscountedPrice(teddyBear, 5, discountedPriceForFiveTeddyBears))

        val receipt = scenario.checkout()

        val expectedNonDiscountedPrice = numberOfTeddyBears * teddyBearPrice
        val expectedTotalPrice = discountedPriceForFiveTeddyBears + teddyBearPrice
        val expectedReceiptItem = ReceiptItem(teddyBear, 6.0, teddyBearPrice, expectedNonDiscountedPrice)
        val expectedDiscount = Discount(
            teddyBear,
            "5 for $discountedPriceForFiveTeddyBears",
            expectedTotalPrice - expectedNonDiscountedPrice
        )

        receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
        receipt.getItems() shouldContainExactly listOf(expectedReceiptItem)
        receipt.getDiscounts() shouldContainExactly listOf(expectedDiscount)
    }

})

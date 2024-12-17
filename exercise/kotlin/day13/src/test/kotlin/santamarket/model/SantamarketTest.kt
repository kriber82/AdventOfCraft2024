package santamarket.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.doubles.shouldBeLessThan
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.property.Exhaustive
import io.kotest.property.exhaustive.collection
import io.kotest.property.forAll
import santamarket.model.offer.ItemBundleForDiscountedPrice
import santamarket.model.offer.SpecialOfferType
import santamarket.model.offer.TenPercentOff

class SantamarketTestDescribe : DescribeSpec({
    describe("ShoppingSleigh") {
        it("should be empty initially") {
            val scenario = TestScenarioBuilder().build()

            scenario.sleigh.getItems() shouldBe emptyList()
            scenario.sleigh.productQuantities() shouldBe emptyMap()
        }

        it("should add single item") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddyBear", 1, ProductUnit.EACH, 1.0)
                .build()
            val teddyBear = scenario.catalog.product("teddyBear")!!

            scenario.sleigh.getItems() shouldContainExactly listOf(ProductQuantity(teddyBear, 1.0))
        }

        it("should add multiple items") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddyBear", 3, ProductUnit.EACH, 1.0)
                .build()
            val teddyBear = scenario.catalog.product("teddyBear")!!

            scenario.sleigh.getItems() shouldContainExactly List(3) { (ProductQuantity(teddyBear, 1.0)) }
        }

        it("should add single item with quantity") {
            val scenario = TestScenarioBuilder()
                .withProduct("teddyBear", 2.0, ProductUnit.EACH, 1.0)
                .build()
            val teddyBear = scenario.catalog.product("teddyBear")!!

            scenario.sleigh.getItems() shouldContainExactly listOf(ProductQuantity(teddyBear, 2.0))
        }

        it("should add multiple items with quantity") {
            val scenario = TestScenarioBuilder()
                .withProduct("teddyBear", 2.0, ProductUnit.EACH, 1.0)
                .withProduct("teddyBear", 1.0, ProductUnit.EACH, 1.0)
                .build()
            val teddyBear = scenario.catalog.product("teddyBear")!!

            scenario.sleigh.getItems() shouldContainExactly listOf(
                ProductQuantity(teddyBear, 2.0),
                ProductQuantity(teddyBear, 1.0)
            )
        }

        it("should return 1.0 as quantity of single item") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddyBear", 1, ProductUnit.EACH, 1.0)
                .build()
            val teddyBear = scenario.catalog.product("teddyBear")!!

            scenario.sleigh.productQuantities() shouldContainExactly mapOf(Pair(teddyBear, 1.0))
        }

        it("should return amount of items as quantity of multiple items ") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddyBear", 3, ProductUnit.EACH, 1.0)
                .build()
            val teddyBear = scenario.catalog.product("teddyBear")!!

            scenario.sleigh.productQuantities() shouldContainExactly mapOf(Pair(teddyBear, 3.0))
        }

        it("should return item given quantity for item with quantity") {
            val scenario = TestScenarioBuilder()
                .withProduct("teddyBear", 2.0, ProductUnit.EACH, 1.0)
                .build()
            val teddyBear = scenario.catalog.product("teddyBear")!!

            scenario.sleigh.productQuantities() shouldContainExactly mapOf(Pair(teddyBear, 2.0))
        }

        it("should return sum of given quantities for multiple items with quantity") {
            val scenario = TestScenarioBuilder()
                .withProduct("teddyBear", 2.0, ProductUnit.EACH, 1.0)
                .withProduct("teddyBear", 1.0, ProductUnit.EACH, 1.0)
                .build()
            val teddyBear = scenario.catalog.product("teddyBear")!!

            scenario.sleigh.productQuantities() shouldContainExactly mapOf(Pair(teddyBear, 3.0))
        }

        it("should be able to contain different items") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddyBear", 1, ProductUnit.EACH, 1.0)
                .withProduct("teddyBear", 2.0, ProductUnit.EACH, 1.0)
                .withRepeatedSingleProduct("turkey", 1, ProductUnit.EACH, 1.0)
                .withProduct("turkey", 3.0, ProductUnit.EACH, 1.0)
                .build()
            val teddyBear = scenario.catalog.product("teddyBear")!!
            val turkey = scenario.catalog.product("turkey")!!

            scenario.sleigh.getItems() shouldContainExactly listOf(
                ProductQuantity(teddyBear, 1.0),
                ProductQuantity(teddyBear, 2.0),
                ProductQuantity(turkey, 1.0),
                ProductQuantity(turkey, 3.0)
            )
        }

        it("should return the sum of quantities for each product") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddyBear", 1, ProductUnit.EACH, 1.0)
                .withProduct("teddyBear", 2.0, ProductUnit.EACH, 1.0)
                .withRepeatedSingleProduct("turkey", 1, ProductUnit.EACH, 1.0)
                .withProduct("turkey", 3.0, ProductUnit.EACH, 1.0)
                .build()
            val teddyBear = scenario.catalog.product("teddyBear")!!
            val turkey = scenario.catalog.product("turkey")!!

            scenario.sleigh.productQuantities() shouldContainExactly mapOf(
                Pair(teddyBear, 3.0),
                Pair(turkey, 4.0),
            )
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
                    .withRepeatedSingleProduct("teddyBear", 1, ProductUnit.EACH, 5.0)
                    .build()

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (5.0 plusOrMinus 0.001)
            }

            it("should multiply unit price for multiple items of same type in sleigh") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddyBear", 2.0, ProductUnit.EACH, 5.0)
                    .build()

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (2.0 * 5.0 plusOrMinus 0.001)
            }

            it("should contain sum of multiplied unit prices for several items in sleigh") {
                val scenario = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddyBear", 2, ProductUnit.EACH, 5.0)
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

            it("should contain one line with total price for single item with quantity above 1") {
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
                    ReceiptItem(turkey, 1.0, 2.0, 2.0)
                )
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

            it("should contain discount for single item with offer") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddyBear", 1.0, ProductUnit.EACH, 1.0)
                    .withTenPercentDiscount("teddyBear")
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!

                val receipt = scenario.checkout()

                receipt.getDiscounts() shouldContainExactly listOf(Discount(teddyBear, "10.0% off", -0.1))
            }

            it("should countain single entry for several items of the same type") {
                val scenario = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddyBear", 3, ProductUnit.EACH, 1.0)
                    .withTenPercentDiscount("teddyBear")
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!

                val receipt = scenario.checkout()

                receipt.getDiscounts() shouldContainExactly listOf(Discount(teddyBear, "10.0% off", -0.3))
            }

        }
    }

    describe("discount types") {
        describe("christmas elfs discount logic") {
            it("should apply discount for each applicable item") {
                val scenario = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddyBear", 2, ProductUnit.EACH, 1.0)
                    .withTenPercentDiscount("teddyBear")
                    .build()

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (2 * 0.9 plusOrMinus 0.001)
            }

            it("should not apply discount for correct item type with insufficient quantity") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddyBear", 1.0, ProductUnit.EACH, 1.0)
                    .withTwoForOneDiscount("teddyBear")
                    .build()

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (1.0 plusOrMinus 0.001)
            }

            it("should not apply discount for different item type") {
                val scenario = TestScenarioBuilder()
                    .withProduct("teddyBear", 0.0, ProductUnit.EACH, 1.0)
                    .withProduct("turkey", 1.0, ProductUnit.EACH, 1.0)
                    .withTwoForOneDiscount("teddyBear")
                    .build()

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (1.0 plusOrMinus 0.001)
            }

            it("should check the total quantity of an item when applying discount") {
                val scenario = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddyBear", 2, ProductUnit.EACH, 1.0)
                    .withTwoForOneDiscount("teddyBear")
                    .build()

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (1.0 plusOrMinus 0.001)
            }

            it("should override existing offer for given item type") {
                val scenario = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddyBear", 2, ProductUnit.EACH, 1.0)
                    .withTenPercentDiscount("teddyBear")
                    .withTwoForOneDiscount("teddyBear")
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (1.0 plusOrMinus 0.001)
            }

            it("should apply offers added with new addOffer") {
                val scenario = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddyBear", 1, ProductUnit.EACH, 1.0)
                    .build()
                val teddyBear = scenario.catalog.product("teddyBear")!!
                scenario.elf.addSpecialOffer(TenPercentOff(teddyBear, 10.0))

                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (0.9 plusOrMinus 0.001)
            }

        }

        describe("ten percent discount") {
            val scenario = TestScenarioBuilder()
                .withRepeatedSingleProduct("teddyBear", 2, ProductUnit.EACH, 1.0)
                .withTenPercentDiscount("teddyBear")
                .build()

            it("should reduce the price by ten percent") {
                val receipt = scenario.checkout()

                receipt.getTotalPrice() shouldBe (2 * 0.9 plusOrMinus 0.001)
            }

            it("should output the correct discount description") {
                val receipt = scenario.checkout()

                receipt.getDiscounts().first().description shouldBe ("10.0% off")
            }
        }

        forAll(
            Exhaustive.collection(
                listOf(
                    Triple(TestScenarioBuilder::withTwoForOneDiscount, 2, 1),
                    Triple(TestScenarioBuilder::withThreeForTwoDiscount, 3, 2),
                )
            )
        ) { (addOfferToScenario, receivedItems, paidItems) ->

            describe("$receivedItems for $paidItems offer") {

                val builder = TestScenarioBuilder()
                    .withRepeatedSingleProduct("teddyBear", receivedItems, ProductUnit.EACH, 1.0)
                addOfferToScenario(builder, "teddyBear")
                val scenario = builder.build()

                it("should reduce the price to single unit price") {
                    val receipt = scenario.checkout()

                    val expectedTotalPrice = paidItems * 1.0
                    receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
                }

                it("should output the correct discount description") {
                    val receipt = scenario.checkout()

                    receipt.getDiscounts().first().description shouldBe ("$receivedItems for $paidItems")
                }

                it("should not apply discount for item count below bundle size") {
                    val itemsInSleigh = receivedItems - 1
                    val builder = TestScenarioBuilder()
                        .withRepeatedSingleProduct("teddyBear", itemsInSleigh, ProductUnit.EACH, 1.0)
                    addOfferToScenario(builder, "teddyBear")
                    val scenario = builder.build()

                    val receipt = scenario.checkout()

                    val expectedPrice = itemsInSleigh * 1.0
                    receipt.getTotalPrice() shouldBe (expectedPrice plusOrMinus 0.001)
                    receipt.getDiscounts() shouldBe emptyList()
                }

                it("should apply discount to full bundles and sell leftover items for normal price") {
                    val amountOfFullBundles = 2
                    val amountOfLeftoverItems = 1
                    val itemsInSleigh = amountOfFullBundles * receivedItems + amountOfLeftoverItems

                    val builder = TestScenarioBuilder()
                        .withRepeatedSingleProduct("teddyBear", itemsInSleigh, ProductUnit.EACH, 1.0)
                    addOfferToScenario(builder, "teddyBear")
                    val scenario = builder.build()

                    val receipt = scenario.checkout()

                    val bundlePrice = paidItems * 1.0
                    val expectedTotalPrice = amountOfFullBundles * bundlePrice + amountOfLeftoverItems * 1.0
                    receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
                }

            }
            true

        }

                forAll(
                    Exhaustive.collection(
                        listOf(
                            Triple(TestScenarioBuilder::withTwoForDiscountedPriceOffer, 2, 1.6),
                            Triple(TestScenarioBuilder::withFiveForDiscountedPriceOffer, 5, 3.9)
                        )
                    )
                ) { (addOfferToScenario, itemQuantityInBundle, discountedPrice) ->

                    describe("$itemQuantityInBundle for discounted price ($discountedPrice) offer") {

                        val builder = TestScenarioBuilder()
                            .withRepeatedSingleProduct("teddyBear", itemQuantityInBundle, ProductUnit.EACH, 1.0)
                        addOfferToScenario(builder, "teddyBear", discountedPrice)
                        val scenario = builder.build()

                        it("should reduce the price to discounted price") {
                            val receipt = scenario.checkout()

                            receipt.getTotalPrice() shouldBe (discountedPrice plusOrMinus 0.001)
                        }

                        it("should output the correct discount description") {
                            val receipt = scenario.checkout()

                            receipt.getDiscounts().first().description shouldBe ("$itemQuantityInBundle for $discountedPrice")
                        }

                        it("should not apply discount for item count below bundle size") {
                            val itemsInSleigh = itemQuantityInBundle - 1
                            val builder = TestScenarioBuilder()
                                .withRepeatedSingleProduct("teddyBear", itemsInSleigh, ProductUnit.EACH, 1.0)
                            addOfferToScenario(builder, "teddyBear", discountedPrice)
                            val scenario = builder.build()

                            val receipt = scenario.checkout()

                            val expectedPrice = itemsInSleigh * 1.0
                            receipt.getTotalPrice() shouldBe (expectedPrice plusOrMinus 0.001)
                            receipt.getDiscounts() shouldBe emptyList()
                        }

                        it("should apply discount to full bundles and sell leftover items for normal price") {
                            val amountOfFullBundles = 2
                            val amountOfLeftoverItems = 1
                            val itemsInSleigh = amountOfFullBundles * itemQuantityInBundle + amountOfLeftoverItems

                            val builder = TestScenarioBuilder()
                                .withRepeatedSingleProduct("teddyBear", itemsInSleigh, ProductUnit.EACH, 1.0)
                            addOfferToScenario(builder, "teddyBear", discountedPrice)
                            val scenario = builder.build()

                            val receipt = scenario.checkout()

                            val expectedTotalPrice = amountOfFullBundles * discountedPrice + amountOfLeftoverItems * 1.0
                            receipt.getTotalPrice() shouldBe (expectedTotalPrice plusOrMinus 0.001)
                        }

                    }
                    true
                }
            }
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

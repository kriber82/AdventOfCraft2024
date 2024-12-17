package santamarket.model

import santamarket.model.offer.SpecialOfferType

class TestScenarioBuilder {
    private val catalog = FakeCatalog()
    private val sleigh = ShoppingSleigh()
    private val elf = ChristmasElf(catalog)

    fun withProduct(name: String, quantity: Double, unit: ProductUnit, price: Double): TestScenarioBuilder {
        val product = Product(name, unit)
        if (catalog.product(name) == null)
            catalog.addProduct(product, price)
        sleigh.addItemQuantity(product, quantity)
        return this
    }

    fun withRepeatedSingleProduct(name: String, repetitions: Int, unit: ProductUnit, price: Double): TestScenarioBuilder {
        val product = Product(name, unit)
        if (catalog.product(name) == null)
            catalog.addProduct(product, price)
        repeat(repetitions) {
            sleigh.addItem(product)
        }
        return this
    }

    fun withTenPercentDiscount(productName: String): TestScenarioBuilder {
        elf.addSpecialOffer(SpecialOfferType.TEN_PERCENT_DISCOUNT, catalog.product(productName)!!, 10.0)
        return this
    }

    fun withTwoForOneDiscount(productName: String): TestScenarioBuilder {
        elf.addSpecialOffer(SpecialOfferType.TWO_FOR_ONE, catalog.product(productName)!!, 0.0)
        return this
    }

    fun withTwoForDiscountedPriceOffer(productName: String, discountedPrice: Double): TestScenarioBuilder {
        elf.addSpecialOffer(SpecialOfferType.TWO_FOR_AMOUNT, catalog.product(productName)!!, discountedPrice)
        return this
    }

    fun withFiveForDiscountedPriceOffer(productName: String, discountedPrice: Double): TestScenarioBuilder {
        elf.addSpecialOffer(SpecialOfferType.FIVE_FOR_AMOUNT, catalog.product(productName)!!, discountedPrice)
        return this
    }

    fun withThreeForTwoDiscount(productName: String): TestScenarioBuilder {
        elf.addSpecialOffer(SpecialOfferType.THREE_FOR_TWO, catalog.product(productName)!!, 0.0)
        return this
    }

    fun withSpecialOffer(specialOfferType: SpecialOfferType, productName: String, argument: Double): TestScenarioBuilder {
        elf.addSpecialOffer(specialOfferType, catalog.product(productName)!!, argument)
        return this
    }

    fun build(): TestScenario {
        return TestScenario(catalog, sleigh, elf)
    }

}
package santamarket.model

class Offer(val offerType: SpecialOfferType, val product: Product, val argument: Double) {
    fun getDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ) = when (this.offerType) {
        SpecialOfferType.TEN_PERCENT_DISCOUNT -> getTenPercentDiscount(unitPrice, itemsInCart)
        SpecialOfferType.THREE_FOR_TWO -> getThreeForTwoDiscount(unitPrice, itemsInCart)
        SpecialOfferType.TWO_FOR_AMOUNT -> getTwoForAmountDiscount(itemsInCart, unitPrice)
        SpecialOfferType.FIVE_FOR_AMOUNT -> getFiveForAmountDiscount(itemsInCart, unitPrice)
    }

    private fun getTenPercentDiscount(
        unitPrice: Double,
        itemsInCart: Double,
    ): Discount {
        val product = this.product
        return Discount(product, "${this.argument}% off", -getUndiscountedPrice(unitPrice, itemsInCart) * this.argument / 100.0)
    }

    private fun getThreeForTwoDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ): Discount? {
        val discountItemsGiven = 3
        val discountItemsPaid = 2
        val product = this.product
        val priceForGivenItems = unitPrice * discountItemsPaid
        return getDiscountWithReducedPriceForMultipleItems(
            itemsInCart,
            product,
            unitPrice,
            discountItemsGiven,
            priceForGivenItems,
            "$discountItemsGiven for $discountItemsPaid"
        )
    }

    private fun getTwoForAmountDiscount(
        itemsInCart: Double,
        unitPrice: Double
    ): Discount? {
        val discountItemsGiven = 2
        val priceForGivenItems = this.argument
        val product = this.product
        return getDiscountWithReducedPriceForMultipleItems(
            itemsInCart,
            product,
            unitPrice,
            discountItemsGiven,
            priceForGivenItems,
            "$discountItemsGiven for $priceForGivenItems"
        )
    }

    private fun getFiveForAmountDiscount(
        itemsInCart: Double,
        unitPrice: Double
    ): Discount? {
        val discountItemsGiven = 5
        val priceForGivenItems = this.argument
        val product = this.product
        return getDiscountWithReducedPriceForMultipleItems(
            itemsInCart,
            product,
            unitPrice,
            discountItemsGiven,
            priceForGivenItems,
            "$discountItemsGiven for $priceForGivenItems"
        )
    }

    private fun getUndiscountedPrice(unitPrice: Double, itemsInCart: Double) = unitPrice * itemsInCart

    private fun getDiscountWithReducedPriceForMultipleItems(
        itemsInCart: Double,
        product: Product,
        unitPrice: Double,
        discountBundleItemAmount: Int,
        discountBundlePrice: Double,
        discountDescription: String
    ): Discount? {
        val itemsInCartAsInt = itemsInCart.toInt()
        return if (itemsInCartAsInt >= discountBundleItemAmount) {
            val discountAmount =
                getUndiscountedPrice(unitPrice, itemsInCart) - (discountBundlePrice * (itemsInCartAsInt / discountBundleItemAmount) + itemsInCartAsInt % discountBundleItemAmount * unitPrice)
            Discount(product, discountDescription, -discountAmount)
        } else null
    }

}
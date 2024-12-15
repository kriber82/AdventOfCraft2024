package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product
import santamarket.model.SpecialOfferType

open class Offer(val offerType: SpecialOfferType, val product: Product, val argument: Double) {
    open fun getDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ) = when (this.offerType) {
        SpecialOfferType.THREE_FOR_TWO -> getThreeForTwoDiscount(unitPrice, itemsInCart)
        else -> null
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

    protected fun getUndiscountedPrice(unitPrice: Double, itemsInCart: Double) = unitPrice * itemsInCart

    protected fun getDiscountWithReducedPriceForMultipleItems(
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
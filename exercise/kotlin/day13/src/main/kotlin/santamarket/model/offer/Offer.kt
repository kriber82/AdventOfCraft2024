package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product
import santamarket.model.SpecialOfferType

abstract class Offer(val offerType: SpecialOfferType, val product: Product, val argument: Double) {
    abstract fun getDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ) : Discount?

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
package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

abstract class Offer(val product: Product) {
    abstract fun getDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ) : Discount?

    protected fun getDiscountWithReducedPriceForMultipleItems(
        itemsInCart: Double,
        unitPrice: Double,
        discountBundleItemAmount: Int,
        discountBundlePrice: Double,
        discountDescription: String
    ): Discount? {
        val itemsInCartAsInt = itemsInCart.toInt()
        return if (itemsInCartAsInt >= discountBundleItemAmount) {
            val undiscountedPrice = unitPrice * discountBundlePrice
            val discountAmount =
                undiscountedPrice - (discountBundlePrice * (itemsInCartAsInt / discountBundleItemAmount) + itemsInCartAsInt % discountBundleItemAmount * unitPrice)
            Discount(this.product, discountDescription, -discountAmount)
        } else null
    }

}
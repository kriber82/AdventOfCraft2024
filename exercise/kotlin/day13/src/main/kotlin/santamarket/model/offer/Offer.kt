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
        pricePerDiscountedBundle: Double,
        discountDescription: String
    ): Discount? {
        val itemsInCartAsInt = itemsInCart.toInt()
        return if (itemsInCartAsInt >= discountBundleItemAmount) {
            val undiscountedTotal = unitPrice * itemsInCart

            val amountOfDiscountedBundles = itemsInCartAsInt / discountBundleItemAmount
            val amountOfItemsOutsideOfDiscountedBundles = itemsInCartAsInt % discountBundleItemAmount
            val discountedTotal =
                amountOfDiscountedBundles * pricePerDiscountedBundle + amountOfItemsOutsideOfDiscountedBundles * unitPrice

            val discountAmount = undiscountedTotal - discountedTotal
            Discount(this.product, discountDescription, -discountAmount)
        } else null
    }

}
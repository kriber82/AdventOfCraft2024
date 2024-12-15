package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class ItemBundleForDiscountedPriceOffer(product: Product, private val bundlePrice: Double, private val bundleItemsAmount: Int) :
    Offer(product) {

    override fun getDiscount(unitPrice: Double, itemsInCart: Double): Discount? {
        return getDiscountWithReducedPriceForMultipleItems(
            product,
            itemsInCart,
            unitPrice,
            bundleItemsAmount,
            bundlePrice,
            "$bundleItemsAmount for $bundlePrice"
        )
    }

    companion object {
        internal fun getDiscountWithReducedPriceForMultipleItems(
            product: Product,
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
                Discount(product, discountDescription, -discountAmount)
            } else null
        }
    }

}
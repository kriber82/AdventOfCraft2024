package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class ItemBundleForDiscountedPriceOffer(product: Product, private val bundlePrice: Double, private val bundleItemsAmount: Int) :
    Offer(product) {

    override fun getDiscount(unitPrice: Double, itemsInCart: Double): Discount? {
        return getDiscountWithReducedPriceForMultipleItems(
            itemsInCart,
            unitPrice,
            bundleItemsAmount,
            bundlePrice,
            "$bundleItemsAmount for $bundlePrice"
        )
    }


}
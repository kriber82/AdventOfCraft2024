package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class ItemBundleForDiscountedPriceOffer(product: Product, bundlePrice: Double, private val bundleItemsAmount: Int) :
    Offer(product, bundlePrice) {

    override fun getDiscount(unitPrice: Double, itemsInCart: Double): Discount? =
        getFiveForAmountDiscount(itemsInCart, unitPrice)

    private fun getFiveForAmountDiscount(
        itemsInCart: Double,
        unitPrice: Double
    ): Discount? {
        val discountItemsGiven = bundleItemsAmount
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


}
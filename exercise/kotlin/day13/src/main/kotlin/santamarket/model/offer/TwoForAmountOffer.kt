package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class TwoForAmountOffer(product: Product, argument: Double) :
    Offer(product, argument) {

    override fun getDiscount(unitPrice: Double, itemsInCart: Double): Discount? =
        getTwoForAmountDiscount(itemsInCart, unitPrice)

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


}
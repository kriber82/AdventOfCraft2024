package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class FiveForAmountOffer(product: Product, argument: Double) :
    Offer(product, argument) {

    override fun getDiscount(unitPrice: Double, itemsInCart: Double): Discount? =
        getFiveForAmountDiscount(itemsInCart, unitPrice)

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


}
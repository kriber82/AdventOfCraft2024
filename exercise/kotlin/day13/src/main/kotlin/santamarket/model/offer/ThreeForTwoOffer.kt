package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class ThreeForTwoOffer(product: Product, argument: Double) :
    Offer(product, argument) {

    override fun getDiscount(unitPrice: Double, itemsInCart: Double): Discount? {
        return getThreeForTwoDiscount(unitPrice, itemsInCart)
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


}
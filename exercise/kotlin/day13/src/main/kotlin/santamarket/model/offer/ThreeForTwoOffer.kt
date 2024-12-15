package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class ThreeForTwoOffer(product: Product) :
    Offer(product) {

    override fun getDiscount(unitPrice: Double, itemsInCart: Double): Discount? {
        val discountItemsGiven = 3
        val discountItemsPaid = 2
        val priceForGivenItems = unitPrice * discountItemsPaid
        return ItemBundleForDiscountedPriceOffer.getDiscountWithReducedPriceForMultipleItems(
            product,
            itemsInCart,
            unitPrice,
            discountItemsGiven,
            priceForGivenItems,
            "$discountItemsGiven for $discountItemsPaid"
        )
    }


}
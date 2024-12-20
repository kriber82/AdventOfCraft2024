package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class ItemBundleForPriceOfLessItems(override val product: Product, private val discountBundleItemsAmount: Int, private val discountBundItemsToPay: Int) :
    Offer {

    override fun getDiscount(unitPrice: Double, sumOfItemQuantitiesInSleigh: Double): Discount? {
        val discountItemsGiven = discountBundleItemsAmount
        val discountItemsPaid = discountBundItemsToPay
        val priceForGivenItems = unitPrice * discountItemsPaid
        return ItemBundleForDiscountedPrice.getDiscountWithReducedPriceForMultipleItems(
            product,
            sumOfItemQuantitiesInSleigh,
            unitPrice,
            discountItemsGiven,
            priceForGivenItems,
            "$discountItemsGiven for $discountItemsPaid"
        )
    }


}
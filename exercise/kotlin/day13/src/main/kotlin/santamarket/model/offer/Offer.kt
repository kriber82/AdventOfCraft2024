package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

interface Offer {
    val product: Product

    fun getDiscount(
        unitPrice: Double,
        sumOfItemQuantitiesInSleigh: Double
    ): Discount?
}
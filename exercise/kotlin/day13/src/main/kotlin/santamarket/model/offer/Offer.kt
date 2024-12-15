package santamarket.model.offer

import santamarket.model.Discount

interface Offer {
    fun getDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ): Discount?
}
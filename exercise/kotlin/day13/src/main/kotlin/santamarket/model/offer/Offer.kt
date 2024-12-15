package santamarket.model.offer

import santamarket.model.Discount

abstract class Offer() {
    abstract fun getDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ) : Discount?
}
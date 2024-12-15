package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

abstract class Offer(val product: Product) {
    abstract fun getDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ) : Discount?
}
package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class TenPercentOffer(product: Product, private val percentOff: Double) :
    Offer(product) {

    override fun getDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ) = Discount(
        product,
        "$percentOff% off",
        -getUndiscountedPrice(unitPrice, itemsInCart) * percentOff / 100.0
    )

}
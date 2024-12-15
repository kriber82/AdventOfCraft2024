package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class TenPercentOffer(private val product: Product, private val percentOff: Double) :
    Offer() {

    override fun getDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ): Discount {
        val undiscountedTotal = unitPrice * itemsInCart
        return Discount(
            product,
            "$percentOff% off",
            -undiscountedTotal * percentOff / 100.0
        )
    }

}
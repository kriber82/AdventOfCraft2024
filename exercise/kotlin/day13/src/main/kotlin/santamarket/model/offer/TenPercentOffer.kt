package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class TenPercentOffer(product: Product, argument: Double) :
    Offer(product, argument) {

    override fun getDiscount(
        unitPrice: Double,
        itemsInCart: Double
    ) = getTenPercentDiscount(unitPrice, itemsInCart)

    private fun getTenPercentDiscount(
        unitPrice: Double,
        itemsInCart: Double,
    ): Discount {
        val product = this.product
        return Discount(product, "${this.argument}% off", -getUndiscountedPrice(unitPrice, itemsInCart) * this.argument / 100.0)
    }

}
package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product
import santamarket.model.SpecialOfferType

class TenPercentOffer(offerType: SpecialOfferType, product: Product, argument: Double) :
    Offer(offerType, product, argument) {

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
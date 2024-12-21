package santamarket.model.offer

import santamarket.model.Discount
import santamarket.model.Product

class TenPercentOff(override val product: Product, private val percentOff: Double) :
    Offer {

    override fun getDiscount(
        unitPrice: Double,
        sumOfItemQuantitiesInSleigh: Double
    ): Discount {
        val undiscountedTotal = unitPrice * sumOfItemQuantitiesInSleigh
        return Discount(
            product,
            "$percentOff% off",
            -undiscountedTotal * percentOff / 100.0
        )
    }

}
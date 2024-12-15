package santamarket.model

import santamarket.model.offer.FiveForAmountOffer
import santamarket.model.offer.Offer
import santamarket.model.offer.TenPercentOffer
import santamarket.model.offer.TwoForAmountOffer

class ChristmasElf(private val catalog: SantamarketCatalog) {
    private val offers = mutableMapOf<Product, Offer>()

    fun addSpecialOffer(offerType: SpecialOfferType, product: Product, argument: Double) {
        val offer =
            when (offerType) {
                SpecialOfferType.TEN_PERCENT_DISCOUNT -> TenPercentOffer(offerType, product, argument)
                SpecialOfferType.TWO_FOR_AMOUNT -> TwoForAmountOffer(offerType, product, argument)
                SpecialOfferType.FIVE_FOR_AMOUNT -> FiveForAmountOffer(offerType, product, argument)
                else -> Offer(offerType, product, argument)
            }
        offers[product] = offer
    }

    fun checksOutArticlesFrom(sleigh: ShoppingSleigh): Receipt {
        val receipt = Receipt()
        val productQuantities = sleigh.getItems()

        productQuantities.forEach { pq ->
            val product = pq.product
            val quantity = pq.quantity
            val unitPrice = catalog.getUnitPrice(product)
            val price = quantity * unitPrice
            receipt.addProduct(product, quantity, unitPrice, price)
        }

        sleigh.handleOffers(receipt, offers, catalog)
        return receipt
    }
}
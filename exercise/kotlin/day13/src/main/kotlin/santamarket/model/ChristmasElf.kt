package santamarket.model

import santamarket.model.offer.*

class ChristmasElf(private val catalog: SantamarketCatalog) {
    private val offers = mutableMapOf<Product, Offer>()

    fun addSpecialOffer(offerType: SpecialOfferType, product: Product, argument: Double) {
        val offer = when (offerType) {
                SpecialOfferType.TEN_PERCENT_DISCOUNT -> TenPercentOffer(product, argument)
                SpecialOfferType.TWO_FOR_AMOUNT -> ItemBundleForDiscountedPriceOffer(product, argument, 2)
                SpecialOfferType.FIVE_FOR_AMOUNT -> ItemBundleForDiscountedPriceOffer(product, argument, 5)
                SpecialOfferType.THREE_FOR_TWO -> ThreeForTwoOffer(product)
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
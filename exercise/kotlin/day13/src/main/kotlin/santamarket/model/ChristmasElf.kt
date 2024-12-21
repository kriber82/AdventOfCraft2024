package santamarket.model

import santamarket.model.offer.*

class ChristmasElf(private val catalog: SantamarketCatalog) {
    private val offers = mutableMapOf<Product, Offer>()

    fun addSpecialOffer(offer: Offer) {
        offers[offer.product] = offer
    }

    @Deprecated("Use addSpecialOffer(offer: Offer) with Offer implementations instead. SpecialOfferType have been misunderstood too often.")
    fun addSpecialOffer(offerType: SpecialOfferType, product: Product, argument: Double) {
        val offer = when (offerType) {
                SpecialOfferType.TEN_PERCENT_DISCOUNT -> TenPercentOff(product, argument)
                SpecialOfferType.TWO_FOR_AMOUNT -> ItemBundleForDiscountedPrice(product, 2, argument)
                SpecialOfferType.FIVE_FOR_AMOUNT -> ItemBundleForDiscountedPrice(product, 5, argument)
                SpecialOfferType.THREE_FOR_TWO -> ItemBundleForPriceOfLessItems(product, 3, 2)
                SpecialOfferType.TWO_FOR_ONE -> ItemBundleForPriceOfLessItems(product, 2, 1)
            }
        offers[product] = offer
    }

    fun checksOutArticlesFrom(sleigh: ShoppingSleigh): Receipt {
        val receipt = Receipt()

        sleigh.getItems().forEach { (product, individualItemQuantity) ->
            addItemToReceipt(product, individualItemQuantity, receipt)
        }

        sleigh.productQuantities().forEach { (product, sumOfItemQuantitiesInSleigh) ->
            addDiscountToReceiptIfApplicable(product, sumOfItemQuantitiesInSleigh, receipt)
        }

        return receipt
    }

    private fun addItemToReceipt(product: Product, quantity: Double, receipt: Receipt) {
        val unitPrice = catalog.getUnitPrice(product)
        val price = quantity * unitPrice
        receipt.addProduct(product, quantity, unitPrice, price)
    }

    private fun addDiscountToReceiptIfApplicable(
        product: Product,
        sumOfItemQuantitiesInSleigh: Double,
        receipt: Receipt
    ) {
        offers[product]?.let { offer ->
            val unitPrice = catalog.getUnitPrice(product)
            val discount = offer.getDiscount(unitPrice, sumOfItemQuantitiesInSleigh)
            if (discount != null) {
                receipt.addDiscount(discount)
            }
        }
    }

}
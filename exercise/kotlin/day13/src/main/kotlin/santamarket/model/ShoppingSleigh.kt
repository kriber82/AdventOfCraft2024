package santamarket.model

class ShoppingSleigh {
    private val items = mutableListOf<ProductQuantity>()
    private val productQuantities = mutableMapOf<Product, Double>()

    fun getItems(): List<ProductQuantity> = items.toList()

    fun addItem(product: Product) {
        addItemQuantity(product, 1.0)
    }

    fun productQuantities(): Map<Product, Double> = productQuantities.toMap()

    fun addItemQuantity(product: Product, quantity: Double) {
        items.add(ProductQuantity(product, quantity))
        productQuantities[product] = productQuantities.getOrDefault(product, 0.0) + quantity
    }

    fun handleOffers(receipt: Receipt, offers: Map<Product, Offer>, catalog: SantamarketCatalog) {
        productQuantities.forEach { (product, itemsInCart) ->
            offers[product]?.let { offer ->
                val unitPrice = catalog.getUnitPrice(product)
                val discount = getDiscount(offer, unitPrice, itemsInCart)
                discount?.let { receipt.addDiscount(it) }
            }
        }
    }

    private fun getDiscount(
        offer: Offer,
        unitPrice: Double,
        itemsInCart: Double
    ) = when (offer.offerType) {
        SpecialOfferType.TEN_PERCENT_DISCOUNT -> getTenPercentDiscount(offer, unitPrice, itemsInCart)
        SpecialOfferType.THREE_FOR_TWO -> getThreeForTwoDiscount(unitPrice, itemsInCart, offer)
        SpecialOfferType.TWO_FOR_AMOUNT -> getTwoForAmountDiscount(offer, itemsInCart, unitPrice)
        SpecialOfferType.FIVE_FOR_AMOUNT -> getFiveForAmountDiscount(offer, itemsInCart, unitPrice)
    }

    private fun getTenPercentDiscount(
        offer: Offer,
        unitPrice: Double,
        itemsInCart: Double,
    ): Discount {
        val product = offer.product
        return Discount(product, "${offer.argument}% off", -getUndiscountedPrice(unitPrice, itemsInCart) * offer.argument / 100.0)
    }

    private fun getThreeForTwoDiscount(
        unitPrice: Double,
        itemsInCart: Double,
        offer: Offer
    ): Discount? {
        val discountItemsGiven = 3
        val discountItemsPaid = 2
        val product = offer.product
        val priceForGivenItems = unitPrice * discountItemsPaid
        return getDiscountWithReducedPriceForMultipleItems(
            itemsInCart,
            product,
            unitPrice,
            discountItemsGiven,
            priceForGivenItems,
            "$discountItemsGiven for $discountItemsPaid"
        )
    }

    private fun getTwoForAmountDiscount(
        offer: Offer,
        itemsInCart: Double,
        unitPrice: Double
    ): Discount? {
        val discountItemsGiven = 2
        val priceForGivenItems = offer.argument
        val product = offer.product
        return getDiscountWithReducedPriceForMultipleItems(
            itemsInCart,
            product,
            unitPrice,
            discountItemsGiven,
            priceForGivenItems,
            "$discountItemsGiven for $priceForGivenItems"
        )
    }

    private fun getFiveForAmountDiscount(
        offer: Offer,
        itemsInCart: Double,
        unitPrice: Double
    ): Discount? {
        val discountItemsGiven = 5
        val priceForGivenItems = offer.argument
        val product = offer.product
        return getDiscountWithReducedPriceForMultipleItems(
            itemsInCart,
            product,
            unitPrice,
            discountItemsGiven,
            priceForGivenItems,
            "$discountItemsGiven for $priceForGivenItems"
        )
    }

    private fun getUndiscountedPrice(unitPrice: Double, itemsInCart: Double) = unitPrice * itemsInCart

    private fun getDiscountWithReducedPriceForMultipleItems(
        itemsInCart: Double,
        product: Product,
        unitPrice: Double,
        discountBundleItemAmount: Int,
        discountBundlePrice: Double,
        discountDescription: String
    ): Discount? {
        val itemsInCartAsInt = itemsInCart.toInt()
        return if (itemsInCartAsInt >= discountBundleItemAmount) {
            val discountAmount =
                getUndiscountedPrice(unitPrice, itemsInCart) - (discountBundlePrice * (itemsInCartAsInt / discountBundleItemAmount) + itemsInCartAsInt % discountBundleItemAmount * unitPrice)
            Discount(product, discountDescription, -discountAmount)
        } else null
    }
}

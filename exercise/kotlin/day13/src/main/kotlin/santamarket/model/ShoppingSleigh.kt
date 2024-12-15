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
                var discount: Discount? = null

                when (offer.offerType) {
                    SpecialOfferType.TEN_PERCENT_DISCOUNT -> {
                        discount = getTenPercentDiscount(product, offer, unitPrice, itemsInCart)
                    }

                    SpecialOfferType.THREE_FOR_TWO -> {
                        discount = getThreeForTwoDiscount(unitPrice, itemsInCart, product)
                    }

                    SpecialOfferType.TWO_FOR_AMOUNT -> {
                        discount = getTwoForAmountDiscount(offer, itemsInCart, product, unitPrice)
                    }

                    SpecialOfferType.FIVE_FOR_AMOUNT -> {
                        discount = getFiveForAmountDiscount(offer, itemsInCart, product, unitPrice)
                    }
                }
                discount?.let { receipt.addDiscount(it) }
            }
        }
    }

    private fun getTenPercentDiscount(
        product: Product,
        offer: Offer,
        unitPrice: Double,
        itemsInCart: Double,
    ): Discount {
        return Discount(product, "${offer.argument}% off", -getUndiscountedPrice(unitPrice, itemsInCart) * offer.argument / 100.0)
    }

    private fun getThreeForTwoDiscount(
        unitPrice: Double,
        itemsInCart: Double,
        product: Product
    ): Discount? {
        val discountItemsGiven = 3
        val discountItemsPaid = 2
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
        product: Product,
        unitPrice: Double
    ): Discount? {
        val discountItemsGiven = 2
        val priceForGivenItems = offer.argument
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
        product: Product,
        unitPrice: Double
    ): Discount? {
        val discountItemsGiven = 5
        val priceForGivenItems = offer.argument
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

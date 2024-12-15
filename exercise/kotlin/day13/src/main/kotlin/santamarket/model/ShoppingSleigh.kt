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

                val undiscountedPrice = getUndiscountedPrice(unitPrice, itemsInCart)
                when (offer.offerType) {
                    SpecialOfferType.TEN_PERCENT_DISCOUNT -> {
                        discount =
                            Discount(product, "${offer.argument}% off", -undiscountedPrice * offer.argument / 100.0)
                    }

                    SpecialOfferType.THREE_FOR_TWO -> {
                        val discountItemsGiven = 3
                        val discountItemsPaid = 2
                        val priceForGivenItems = unitPrice * discountItemsPaid
                        discount = getDiscountWithReducedPriceForMultipleItems(
                            itemsInCart,
                            product,
                            unitPrice,
                            discountItemsGiven,
                            priceForGivenItems,
                            "$discountItemsGiven for $discountItemsPaid"
                        )
                    }

                    SpecialOfferType.TWO_FOR_AMOUNT -> {
                        val discountItemsGiven = 2
                        val priceForGivenItems = offer.argument
                        discount = getDiscountWithReducedPriceForMultipleItems(
                            itemsInCart,
                            product,
                            unitPrice,
                            discountItemsGiven,
                            priceForGivenItems,
                            "$discountItemsGiven for $priceForGivenItems"
                        )
                    }

                    SpecialOfferType.FIVE_FOR_AMOUNT -> {
                        val discountItemsGiven = 5
                        val priceForGivenItems = offer.argument
                        discount = getDiscountWithReducedPriceForMultipleItems(
                            itemsInCart,
                            product,
                            unitPrice,
                            discountItemsGiven,
                            priceForGivenItems,
                            "$discountItemsGiven for $priceForGivenItems"
                        )
                    }
                }
                discount?.let { receipt.addDiscount(it) }
            }
        }
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

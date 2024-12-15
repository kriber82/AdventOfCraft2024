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
        productQuantities.forEach { (product, quantity) ->
            offers[product]?.let { offer ->
                val unitPrice = catalog.getUnitPrice(product)
                val quantityAsInt = quantity.toInt()
                var discount: Discount? = null

                when (offer.offerType) {
                    SpecialOfferType.TEN_PERCENT_DISCOUNT -> {
                        discount =
                            Discount(product, "${offer.argument}% off", -quantity * unitPrice * offer.argument / 100.0)
                    }

                    SpecialOfferType.THREE_FOR_TWO -> {
                        val discountItemsGiven = 3
                        val discountItemsPaid = 2
                        if (quantityAsInt > 2) {
                            val discountAmount =
                                quantity * unitPrice - ((quantityAsInt / 3) * 2 * unitPrice + quantityAsInt % 3 * unitPrice)
                            discount = Discount(product, "3 for 2", -discountAmount)
                        }
                    }

                    SpecialOfferType.TWO_FOR_AMOUNT -> {
                        val discountItemsGiven = 2
                        val discountItemsPaid = offer.argument
                        if (quantityAsInt >= 2) {
                            val total = offer.argument * (quantityAsInt / 2) + quantityAsInt % 2 * unitPrice
                            val discountAmount = unitPrice * quantity - total
                            discount = Discount(product, "2 for ${offer.argument}", -discountAmount)
                        }
                    }

                    SpecialOfferType.FIVE_FOR_AMOUNT -> {
                        val discountItemsGiven = 5
                        val discountItemsPaid = offer.argument
                        val numberOfXs = quantityAsInt / 5
                        if (quantityAsInt >= 5) {
                            val discountTotal =
                                unitPrice * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice)
                            discount = Discount(product, "5 for ${offer.argument}", -discountTotal)
                        }
                    }
                }
                discount?.let { receipt.addDiscount(it) }
            }
        }
    }
}

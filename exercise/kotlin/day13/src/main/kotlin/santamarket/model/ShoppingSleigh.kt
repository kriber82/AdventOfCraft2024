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
                        val priceForGivenItems = unitPrice * discountItemsPaid
                        if (quantityAsInt >= discountItemsGiven) {
                            val discountAmount =
                                unitPrice * quantity - (priceForGivenItems * (quantityAsInt / discountItemsGiven) + quantityAsInt % discountItemsGiven * unitPrice)
                            discount = Discount(product, "$discountItemsGiven for $discountItemsPaid", -discountAmount)
                        }
                    }

                    SpecialOfferType.TWO_FOR_AMOUNT -> {
                        val discountItemsGiven = 2
                        val priceForGivenItems = offer.argument
                        if (quantityAsInt >= discountItemsGiven) {
                            val discountAmount =
                                unitPrice * quantity - (priceForGivenItems * (quantityAsInt / discountItemsGiven) + quantityAsInt % discountItemsGiven * unitPrice)
                            discount = Discount(product, "$discountItemsGiven for $priceForGivenItems", -discountAmount)
                        }
                    }

                    SpecialOfferType.FIVE_FOR_AMOUNT -> {
                        val discountItemsGiven = 5
                        val priceForGivenItems = offer.argument
                        if (quantityAsInt >= discountItemsGiven) {
                            val discountAmount =
                                unitPrice * quantity - (priceForGivenItems * (quantityAsInt / discountItemsGiven) + quantityAsInt % discountItemsGiven * unitPrice)
                            discount = Discount(product, "$discountItemsGiven for $priceForGivenItems", -discountAmount)
                        }
                    }
                }
                discount?.let { receipt.addDiscount(it) }
            }
        }
    }
}

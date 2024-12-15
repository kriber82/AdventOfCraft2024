package santamarket.model

import santamarket.model.offer.Offer

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
                val discount = offer.getDiscount(unitPrice, itemsInCart)
                discount?.let { receipt.addDiscount(it) }
            }
        }
    }
}

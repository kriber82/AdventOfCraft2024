package santamarket.model

class ShoppingSleigh {
    private val individualItems = mutableListOf<ProductQuantity>()
    private val sumOfItemQuantities = mutableMapOf<Product, Double>()

    fun getItems(): List<ProductQuantity> = individualItems.toList()

    fun addItem(product: Product) {
        addItemQuantity(product, 1.0)
    }

    fun productQuantities(): Map<Product, Double> = sumOfItemQuantities.toMap()

    fun addItemQuantity(product: Product, quantity: Double) {
        individualItems.add(ProductQuantity(product, quantity))
        sumOfItemQuantities[product] = sumOfItemQuantities.getOrDefault(product, 0.0) + quantity
    }

}

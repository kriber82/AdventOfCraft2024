package santamarket.model

data class Discount(
    val product: Product,
    val description: String,
    val discountAmount: Double
)
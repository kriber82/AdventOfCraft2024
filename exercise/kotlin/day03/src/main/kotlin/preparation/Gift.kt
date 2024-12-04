package preparation

class Gift(
    private val name: String,
    private val weight: Double,
    private val color: String,
    private val material: String,
    val recommendedAge: Int = 0
) {
    private val attributes: MutableMap<String, String> = mutableMapOf();

    fun addAttributeTemp(key: String, value: String) {
        attributes[key] = value
    }

    override fun toString(): String = "A $color-colored $name weighing $weight kg made in $material"
}
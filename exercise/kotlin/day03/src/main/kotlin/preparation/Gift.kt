package preparation

class Gift(
    private val name: String,
    private val weight: Double,
    private val color: String,
    private val material: String,
    var recommendedAge: Int = 0
) {
    private val attributes: MutableMap<String, String> = mutableMapOf();

    fun addAttribute(key: String, value: String) {
        if (key == "recommendedAge") {
            recommendedAge = value.toIntOrNull() ?: 0
        } else {
            addAttributeTemp(key, value)
        }
    }

    fun addAttributeTemp(key: String, value: String) {
        attributes[key] = value
    }

    override fun toString(): String = "A $color-colored $name weighing $weight kg made in $material"
}
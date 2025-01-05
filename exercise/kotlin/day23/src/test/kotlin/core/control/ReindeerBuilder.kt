package core.control

import external.deer.Reindeer

class ReindeerBuilder(var name: String, var age: Int, var spirit: Int, var sick: Boolean = false) {

    fun withSickness(sick: Boolean): ReindeerBuilder {
        this.sick = sick
        return this
    }

    fun build(): Reindeer {
        return Reindeer(name, age, spirit, sick)
    }

    companion object {
        fun dasher() = ReindeerBuilder("Dasher", 4, 10, true)
        fun dancer() = ReindeerBuilder("Dancer", 2, 8) // give blessed amp
        fun prancer() = ReindeerBuilder("Prancer", 3, 9) // give divine amp
        fun vixen() = ReindeerBuilder("Vixen", 3, 6)
        fun comet() = ReindeerBuilder("Comet", 4, 9, true)
        fun cupid() = ReindeerBuilder("Cupid", 4, 6)
        fun donner() = ReindeerBuilder("Donner", 7, 6)
        fun blitzen() = ReindeerBuilder("Blitzen", 8, 7) // give blessed amp
        fun rudolph() = ReindeerBuilder("Rudolph", 6, 3)

        fun getSantasReindeers() = listOf(dasher(), dancer(), prancer(), vixen(), comet(), cupid(), donner(), blitzen(), rudolph())
    }
}
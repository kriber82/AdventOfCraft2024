package santamarket.model

class TestScenario(val catalog: FakeCatalog, val sleigh: ShoppingSleigh, val elf: ChristmasElf) {
    fun checkout(): Receipt {
        return elf.checksOutArticlesFrom(sleigh)
    }
}
package eid

class EidStringBuilder {
    private var gender: String = "1"
    private var year: String = "98"
    private var serialNumber: String = "007"
    private var controlKeyOverride: String? = "67"

    fun withGender(gender: ElfGender): EidStringBuilder {
        this.gender = toGenderString(gender)
        return this
    }

    fun withGenderString(genderString: String): EidStringBuilder {
        this.gender = genderString
        return this
    }

    fun withYear(year: Int): EidStringBuilder {
        this.year = toYearString(year)
        return this
    }

    fun withYearString(yearString: String): EidStringBuilder {
        this.year = yearString
        return this
    }

    fun withSerialNumber(serialNumber: Int): EidStringBuilder {
        this.serialNumber = toSerialNumberString(serialNumber)
        return this
    }

    fun withSerialNumberString(serialNumberString: String): EidStringBuilder {
        this.serialNumber = serialNumberString
        return this
    }

    fun withControlKeyOverride(controlKeyOverride: String): EidStringBuilder {
        this.controlKeyOverride = controlKeyOverride
        return this
    }

    fun build(): String {
        var controlKey = "00" //TODO calculate
        if (controlKeyOverride != null) {
            controlKey = controlKeyOverride!!
        }
        return "$gender$year$serialNumber$controlKey"
    }

    private fun toGenderString(elfGender: ElfGender): String {
        return when (elfGender) {
            ElfGender.Sloubi -> "1"
            ElfGender.Gagna -> "2"
            ElfGender.Catact -> "3"
        }
    }

    private fun toYearString(year: Int): String {
        return year.toString().padStart(2, '0')
    }

    private fun toSerialNumberString(serialNumber: Int): String {
        return serialNumber.toString().padStart(3, '0')
    }
}
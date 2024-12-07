import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.uInt
import io.kotest.property.checkAll


class EIDTest : DescribeSpec({
    val jercivalsValidSampleEid = EID.fromCompleteIdentifier(19800767u)
    val jercivalsEidPayload = EidPayload.fromParts(1u, 98u, 7u)
    val jercivalsEidControlKey = EidControlKey(67u)

    val validSex = 1u
    val validYear = 0u
    val validSerialNumber = 1u

    describe("EidValidator") {

        it("should successfully validate sample EID") {
            EidValidator.isValid(jercivalsValidSampleEid) shouldBe true
        }

        describe("control key validation") {
            it("should return false for invalid control digits") {
                val invalidControlKey = 99u
                val invalidEid = EID.fromParts(validSex, validYear, validSerialNumber, invalidControlKey)
                EidValidator.isValid(invalidEid) shouldBe false
            }

            it ("should return true for valid control digits") {
                val validEid = EID.fromParts(validSex, validYear, validSerialNumber)
                EidValidator.isValid(validEid) shouldBe true
            }
        }


        describe("sex validation") {
            it("should return false for sexes greater than 3") {
                val invalidSex = 4u
                val invalidEidDueToSex = EID.fromParts(invalidSex, validYear, validSerialNumber)
                EidValidator.isValid(invalidEidDueToSex) shouldBe false
            }

            it("should return false for sex 0") {
                val invalidSex = 0u
                val invalidEidDueToSex = EID.fromParts(invalidSex, validYear, validSerialNumber)
                EidValidator.isValid(invalidEidDueToSex) shouldBe false
            }

            it("should return true for valid sexes") {
                checkAll(Arb.uInt(1u..3u)) { validSex ->
                    EidValidator.isValid(EID.fromParts(validSex, validYear, validSerialNumber)) shouldBe true
                }
            }
        }

        describe("birth year validation") {
            it("should return true for valid birth years") {
                checkAll(Arb.uInt(0u..99u)) { validYear ->
                    EidValidator.isValid(EID.fromParts(validSex, validYear, validSerialNumber)) shouldBe true
                }
            }

            it("should return false for birth years greater than 99") {
                checkAll(Arb.uInt(100u..UInt.MAX_VALUE)) { invalidYear ->
                    EidValidator.isValid(EID.fromParts(validSex, invalidYear, validSerialNumber)) shouldBe false
                }
            }
        }

        describe("serial number validation") {

            it("should return false for serial numbers greater than 999") {
                checkAll(Arb.uInt(1000u..UInt.MAX_VALUE)) { invalidSerialNumber ->
                    EidValidator.isValid(EID.fromParts(validSex, validYear, invalidSerialNumber)) shouldBe false
                }
            }

            it("should return false for serial numbers equal to 0") {
                val invalidSerialNumber = 0u
                EidValidator.isValid(EID.fromParts(validSex, validYear, invalidSerialNumber)) shouldBe false
            }

            it("should return true for valid serial numbers") {
                checkAll(Arb.uInt(1u..999u)) { validSerialNumber ->
                    EidValidator.isValid(EID.fromParts(validSex, validYear, validSerialNumber)) shouldBe true
                }
            }
        }
    }

    describe("EID") {
        it("should be constructable from individual parts") {
            EID.fromParts(1u, 98u, 7u, 67u) shouldBe jercivalsValidSampleEid
            EID.fromParts(1u, 23u, 456u, 78u) shouldBe EID.fromCompleteIdentifier(12345678u)
            EID.fromParts(1u, 0u, 0u,1u) shouldBe EID.fromCompleteIdentifier(10000001u)
        }

        it("should be constructable from individual parts without explicitly providing control key") {
            EID.fromParts(1u, 98u, 7u) shouldBe jercivalsValidSampleEid
        }

        it("should be constructable from valid full EID number") {
            jercivalsValidSampleEid.payload shouldBe jercivalsEidPayload
            jercivalsValidSampleEid.controlKey shouldBe jercivalsEidControlKey
        }

        it("should be constructable from invalid full EID number") {
            val constructed = EID.fromCompleteIdentifier(12345678u)
            constructed.payload shouldBe EidPayload.fromParts(1u,23u,456u)
            constructed.controlKey shouldBe EidControlKey(78u)
        }

    }

    describe("EidPayload") {
        it("should compute valid control digits for payload digits for sample EID") {
            jercivalsEidPayload.computeValidControlKey() shouldBe jercivalsEidControlKey
        }

        it("should compute valid control digits for payload digits for arbitrary EID") {
            /*
                eid payload = first six digits = 123456
                payload modulo 97 = 123456 % 97 = 72
                complement of payload modulo 97 = 97 - payload modulo 97 = 97 - 72 = 25
             */
            EidPayload.fromParts(1u,23u,456u).computeValidControlKey() shouldBe EidControlKey(25u)
        }

    }
})
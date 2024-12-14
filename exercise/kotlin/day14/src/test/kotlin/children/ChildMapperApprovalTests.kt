package children

import children.ChildMapper
import children.db2.X5T78
import children.dtos.Address
import children.dtos.Gender
import io.kotest.matchers.shouldBe
import org.approvaltests.Approvals
import org.junit.jupiter.api.Test
import java.util.*

class ChildMapperApprovalTests {
    val mapper = ChildMapper.INSTANCE

    @Test
    fun shouldMapX5T78ToChildForAGirl() {
        val db2Child = X5T78(
            id = UUID.fromString("c253ae9c-a44c-405c-9e00-7eac99b81e60").toString(),
            n1 = "Alice",
            n2 = "Marie",
            n3 = "Smith",
            cityOfBirthPc = "Paradise",
            personBd = "19/03/2017",
            salutation = "Girl",
            stNum = "123",
            stName = "Sunny Street",
            stC = "Paradise",
            stCid = "99"
        )

        val child = mapper.toDto(db2Child)

        Approvals.verify(child)
    }

    @Test
    fun `should map X5T78 to Child for a boy`() {
        val db2Child = X5T78(
            id = UUID.fromString("62e0c30c-74da-40f4-b951-0c56837d3704").toString(),
            n1 = "Bob",
            n3 = "Brown",
            cityOfBirthPc = "Paradise",
            personBd = "01/09/2021",
            salutation = "Boy",
            stNum = "9",
            stName = "Oak Street",
            stC = "Paradise",
            stCid = "98988"
        )

        val child = mapper.toDto(db2Child)

        Approvals.verify(child)
    }

}
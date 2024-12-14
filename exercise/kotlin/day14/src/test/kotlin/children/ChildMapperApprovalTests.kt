package children

import children.ChildMapper
import children.db2.X5T78
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

}
package co.zip.candidate.userapi.mapper

import co.zip.candidate.userapi.dto.UserDTO
import co.zip.candidate.userapi.entity.UserEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class UserMapperTest {

    @Test
    fun `should map UserDTO to UserEntity`() {
        val result = UserDTO(name = "John Doe", email = "john@doe.com", monthlySalary = 2, monthlyExpenses = 1)
            .toEntity()

        with(result) {
            assertNotNull(id)
            assertEquals("John Doe", name)
            assertEquals("john@doe.com", email)
            assertEquals(2, monthlySalary)
            assertEquals(1, monthlyExpenses)
        }
    }

    @Test
    fun `should map UserEntity to UserDTO`() {
        val userEntity = UserEntity(name = "John Doe", email = "john@doe.com", monthlySalary = 2, monthlyExpenses = 1)

        val result = userEntity.toDTO()

        with(result) {
            assertEquals(userEntity.id, id)
            assertEquals("John Doe", name)
            assertEquals("john@doe.com", email)
            assertEquals(2, monthlySalary)
            assertEquals(1, monthlyExpenses)
        }
    }

}
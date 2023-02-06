package co.zip.candidate.userapi.repository

import co.zip.candidate.userapi.exception.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import java.util.UUID

class UserRepositoryTest : BaseIntegrationTest() {

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `should get get first page of users`() {
        val firstPage = userRepository.findAll(PageRequest.of(0, 1))

        assertThat(firstPage.content)
            .hasSize(1)
        with(firstPage.first()) {
            assertEquals(UUID.fromString("ebed4c17-844e-4331-b507-044618d9d146"), id)
            assertEquals("John", name)
            assertEquals("john@doe.com", email)
            assertEquals(200000L, monthlySalary)
            assertEquals(150000L, monthlyExpenses)
        }
    }

    @Test
    fun `should get second page of users`() {
        val secondPage = userRepository.findAll(PageRequest.of(1, 1))

        assertThat(secondPage.content)
            .hasSize(1)
        with(secondPage.first()) {
            assertEquals(UUID.fromString("0e370603-54f0-4f7e-8636-4d121e1932df"), id)
            assertEquals("Jane", name)
            assertEquals("jane@doe.com", email)
            assertEquals(300000L, monthlySalary)
            assertEquals(200000L, monthlyExpenses)
        }
    }

    @Test
    fun `should find user by id`() {
        val user = userRepository.getUserById(UUID.fromString("ebed4c17-844e-4331-b507-044618d9d146"))

        with(user) {
            assertEquals(UUID.fromString("ebed4c17-844e-4331-b507-044618d9d146"), id)
            assertEquals("John", name)
            assertEquals("john@doe.com", email)
            assertEquals(200000L, monthlySalary)
            assertEquals(150000L, monthlyExpenses)
        }
    }

    @Test
    fun `should fail to find user by id`() {
        assertThrows<EntityNotFoundException> {
            userRepository.getUserById(UUID.fromString("9efa2525-e605-4ca3-bfac-85b91b8323ae"))
        }.let {
            assertEquals("User 9efa2525-e605-4ca3-bfac-85b91b8323ae not found", it.message)
        }
    }

    @Test
    fun `should find user by email`() {
        val result = userRepository.existsByEmail("john@doe.com")

        assertTrue(result)
    }

    @Test
    fun `should fail to find user by email`() {
        val result = userRepository.existsByEmail("john123@doe.com")

        assertFalse(result)
    }

}
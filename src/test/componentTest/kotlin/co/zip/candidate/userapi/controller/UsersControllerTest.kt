package co.zip.candidate.userapi.controller

import co.zip.candidate.userapi.dto.AccountDTO
import co.zip.candidate.userapi.dto.UserDTO
import co.zip.candidate.userapi.dto.UserPage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.UUID

class UsersControllerTest : BaseComponentTest() {

    @Test
    fun `should create a user`() {
        val postResult: UserDTO = performSuccessfulPost("/v1/users", "/create_user.json".asResourceContent())

        assertNotNull(postResult.id)
        assertThat(postResult)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo("/create_user.json".asResourceContent().asDTO() as UserDTO)

        val getResult: UserDTO = performSuccessfulGet("/v1/users/${postResult.id}")

        assertEquals(getResult, postResult)
    }

    @Test
    fun `should get user by id`() {
        val result: UserDTO = performSuccessfulGet("/v1/users/0e370603-54f0-4f7e-8636-4d121e1932df")

        with(result) {
            assertEquals(UUID.fromString("0e370603-54f0-4f7e-8636-4d121e1932df"), id)
            assertEquals("Jane", name)
            assertEquals("jane@doe.com", email)
            assertEquals(300000, monthlySalary)
            assertEquals(200000, monthlyExpenses)
            assertThat(accounts).hasSize(1)
                .containsExactly(AccountDTO(UUID.fromString("dfc25dfa-f3cf-4d0e-9600-0e15d84b1ac9")))
        }
    }

    @Test
    fun `should list users`() {
        val result: UserPage = performSuccessfulGet("/v1/users")

        with(result) {
            assertEquals(2, numberOfElements)
            assertEquals(2, totalElements)
            assertEquals(1, totalPages)
            assertEquals(0, number)
            assertThat(content)
                .containsExactly(
                    UserDTO(
                        UUID.fromString("ebed4c17-844e-4331-b507-044618d9d146"),
                        "John",
                        "john@doe.com",
                        200000,
                        150000,
                        listOf(
                            AccountDTO(UUID.fromString("9735acb2-821a-48eb-aada-3ad4a5b952f2")),
                            AccountDTO(UUID.fromString("ed18efeb-9c76-4663-88aa-3d92258f051d"))
                        )
                    ),
                    UserDTO(
                        UUID.fromString("0e370603-54f0-4f7e-8636-4d121e1932df"),
                        "Jane",
                        "jane@doe.com",
                        300000,
                        200000,
                        listOf(
                            AccountDTO(UUID.fromString("dfc25dfa-f3cf-4d0e-9600-0e15d84b1ac9"))
                        )
                    )
                )
        }
    }

    @Test
    fun `should get first page of users`() {
        val result: UserPage = performSuccessfulGet("/v1/users?size=1")

        with(result) {
            assertEquals(1, numberOfElements)
            assertEquals(2, totalElements)
            assertEquals(2, totalPages)
            assertEquals(0, number)
            assertThat(content)
                .containsExactly(
                    UserDTO(
                        UUID.fromString("ebed4c17-844e-4331-b507-044618d9d146"),
                        "John",
                        "john@doe.com",
                        200000,
                        150000,
                        listOf(
                            AccountDTO(UUID.fromString("9735acb2-821a-48eb-aada-3ad4a5b952f2")),
                            AccountDTO(UUID.fromString("ed18efeb-9c76-4663-88aa-3d92258f051d"))
                        )
                    )
                )
        }
    }

    @Test
    fun `should get second page of users`() {
        val result: UserPage = performSuccessfulGet("/v1/users?page=1&size=1")

        with(result) {
            assertEquals(1, numberOfElements)
            assertEquals(2, totalElements)
            assertEquals(2, totalPages)
            assertEquals(1, number)
            assertThat(content)
                .containsExactly(
                    UserDTO(
                        UUID.fromString("0e370603-54f0-4f7e-8636-4d121e1932df"),
                        "Jane",
                        "jane@doe.com",
                        300000L,
                        200000L,
                        listOf(
                            AccountDTO(UUID.fromString("dfc25dfa-f3cf-4d0e-9600-0e15d84b1ac9"))
                        )
                    )
                )
        }
    }

}
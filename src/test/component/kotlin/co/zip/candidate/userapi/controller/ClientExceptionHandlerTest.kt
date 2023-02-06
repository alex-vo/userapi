package co.zip.candidate.userapi.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ClientExceptionHandlerTest : BaseComponentTest() {

    @Test
    fun `should handle HttpMessageNotReadableException correctly`() {
        val result: Map<String, String> = performPost("/v1/users", "{}")
            .andExpect { status { isBadRequest() } }
            .andReturn()
            .getResponseDTO()

        assertEquals("payload is not readable", result["message"])
    }

    @Test
    fun `should handle MethodArgumentNotValidException correctly`() {
        val result: Map<String, String> = performPost("/v1/users", "/invalid_user.json".asResourceContent())
            .andExpect { status { isBadRequest() } }
            .andReturn()
            .getResponseDTO()

        assertEquals("must be a positive number", result["monthlyExpenses"])
        assertEquals("must be a positive number", result["monthlySalary"])
        assertEquals("must not be blank", result["email"])
        assertEquals("must not be blank", result["name"])
    }

    @Test
    fun `should handle SalaryExpensesRatioException correctly`() {
        val result: Map<String, String> = performPost("/v1/users/ebed4c17-844e-4331-b507-044618d9d146/accounts")
            .andExpect { status { isBadRequest() } }
            .andReturn()
            .getResponseDTO()

        assertEquals("salary/expenses ratio too low", result["message"])
    }

}
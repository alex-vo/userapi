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
        val result: Map<String, String> = performPost("/v1/users/$LOW_RATIO_USER_ID/accounts")
            .andExpect { status { isBadRequest() } }
            .andReturn()
            .getResponseDTO()

        assertEquals("Salary/expenses ratio too low for user $LOW_RATIO_USER_ID", result["message"])
    }

}
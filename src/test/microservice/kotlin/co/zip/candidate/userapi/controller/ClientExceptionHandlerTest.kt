package co.zip.candidate.userapi.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class ClientExceptionHandlerTest : BaseComponentTest() {

    @Test
    fun `should handle HttpMessageNotReadableException correctly`() {
        val result: Map<String, String> = mockMvc.post("/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = "{}"
        }
            .andExpect { status { isBadRequest() } }
            .andReturn()
            .getResponseDTO()

        assertEquals("payload is not readable", result["message"])
    }

    @Test
    fun `should handle MethodArgumentNotValidException correctly`() {
        val result: Map<String, String> = mockMvc.post("/v1/users") {
            contentType = MediaType.APPLICATION_JSON
            content = "/invalid_user.json".asResourceContent()
        }
            .andExpect { status { isBadRequest() } }
            .andReturn()
            .getResponseDTO()

        assertEquals("must be a positive number", result["monthlyExpenses"])
        assertEquals("must be a positive number", result["monthlySalary"])
        assertEquals("must not be blank", result["email"])
        assertEquals("must not be blank", result["name"])
    }

//    @Test
//    fun `should handle ZipValidationException correctly`() {
//        val result: Map<String, String> = mockMvc.post("/v1/users/${precreatedUserLowSalaryExpenseRatio.id}/accounts")
//            .andExpect { status { isBadRequest() } }
//            .andReturn()
//            .getResponseDTO()
//
//        assertEquals("salary/expenses ratio too low", result["message"])
//    }

}
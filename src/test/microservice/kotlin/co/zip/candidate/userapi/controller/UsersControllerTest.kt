package co.zip.candidate.userapi.controller

import co.zip.candidate.userapi.dto.UserDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

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

//    @Test
//    fun `should get a user`() {
//        val result: UserDTO = mockMvc.get("/v1/users/${precreatedValidUser.id}")
//            .andExpect {
//                status { isOk() }
//            }
//            .andReturn()
//            .getResponseDTO()
//
//        assertThat(precreatedValidUser.toDTO())
//            .usingRecursiveComparison()
//            .ignoringFields("accounts")
//            .isEqualTo(result)
//        assertEquals(listOf(precreatedValidUserAccount.toDTO()), result.accounts)
//    }
//
//    @Test
//    fun `should list users`() {
//        mockMvc.get("/v1/users")
//            .andExpect {
//                status { isOk() }
//                jsonPath("$.numberOfElements", `is`(2))
//                jsonPath("$.totalElements", `is`(2))
//                jsonPath("$.totalPages", `is`(1))
//                jsonPath("$.number", `is`(0))
//                jsonPath("$.content.length()", `is`(2))
//                jsonPath("$.content[0].id", `is`(precreatedValidUser.id.toString()))
//                jsonPath("$.content[1].id", `is`(precreatedUserLowSalaryExpenseRatio.id.toString()))
//            }
//
//        mockMvc.get("/v1/users?size=1")
//            .andExpect {
//                status { isOk() }
//                jsonPath("$.numberOfElements", `is`(1))
//                jsonPath("$.totalElements", `is`(2))
//                jsonPath("$.totalPages", `is`(2))
//                jsonPath("$.number", `is`(0))
//                jsonPath("$.content.length()", `is`(1))
//                jsonPath("$.content[0].id", `is`(precreatedValidUser.id.toString()))
//            }
//
//        mockMvc.get("/v1/users?page=1&size=1")
//            .andExpect {
//                status { isOk() }
//                jsonPath("$.numberOfElements", `is`(1))
//                jsonPath("$.totalElements", `is`(2))
//                jsonPath("$.totalPages", `is`(2))
//                jsonPath("$.number", `is`(1))
//                jsonPath("$.content.length()", `is`(1))
//                jsonPath("$.content[0].id", `is`(precreatedUserLowSalaryExpenseRatio.id.toString()))
//            }
//    }

}
package co.zip.candidate.userapi.controller

import co.zip.candidate.userapi.dto.AccountDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

class AccountsControllerTest : BaseComponentTest() {

//    @Test
//    fun `should add account to a user`() {
//        val result: AccountDTO = mockMvc.post("/v1/users/${precreatedValidUser.id}/accounts")
//            .andExpect { status { isOk() } }
//            .andReturn()
//            .getResponseDTO()
//
//        assertNotNull(result.id)
//    }
//
//    @Test
//    fun `should list user's accounts`() {
//        val result: List<AccountDTO> = mockMvc.get("/v1/users/${precreatedValidUser.id}/accounts")
//            .andExpect { status { isOk() } }
//            .andReturn()
//            .getResponseDTO()
//
//        assertThat(result).hasSize(1)
//        assertEquals(precreatedValidUserAccount.id, result[0].id)
//    }

}
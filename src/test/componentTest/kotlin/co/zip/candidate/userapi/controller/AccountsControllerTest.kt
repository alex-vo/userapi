package co.zip.candidate.userapi.controller

import co.zip.candidate.userapi.dto.AccountDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class AccountsControllerTest : BaseComponentTest() {

    @Test
    fun `should add account to a user`() {
        val result: AccountDTO = performSuccessfulPost("/v1/users/$VALID_USER_ID/accounts")

        assertNotNull(result.id)
    }

    @Test
    fun `should list user's accounts`() {
        val result: List<AccountDTO> = performSuccessfulGet("/v1/users/$VALID_USER_ID/accounts")

        println(result)

        assertThat(result).containsExactly(AccountDTO(VALID_USER_ACCOUNT_ID))
    }

}
package co.zip.candidate.userapi.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class AccountRepositoryTest : BaseIntegrationTest() {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Test
    fun `should find accounts by user id`() {
        val result = accountRepository.findByUserId(UUID.fromString("ebed4c17-844e-4331-b507-044618d9d146"))

        assertThat(result).hasSize(2)
        with(result[0]) {
            assertEquals(UUID.fromString("9735acb2-821a-48eb-aada-3ad4a5b952f2"), id)
            assertEquals(UUID.fromString("ebed4c17-844e-4331-b507-044618d9d146"), user.id)
        }
        with(result[1]) {
            assertEquals(UUID.fromString("ed18efeb-9c76-4663-88aa-3d92258f051d"), id)
            assertEquals(UUID.fromString("ebed4c17-844e-4331-b507-044618d9d146"), user.id)
        }
    }

    @Test
    fun `should fail to find accounts by user id`() {
        val result = accountRepository.findByUserId(UUID.fromString("e33bc033-0066-45da-9908-f8c2013b48cb"))

        assertThat(result).isEmpty()
    }

}
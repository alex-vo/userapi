package co.zip.candidate.userapi.mapper

import co.zip.candidate.userapi.entity.AccountEntity
import co.zip.candidate.userapi.entity.UserEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

class AccountMapperTest {

    @Test
    fun `should map AccountEntity to AccountDTO`() {
        val entity = AccountEntity(mock(UserEntity::class.java))

        val result = entity.toDTO()

        assertEquals(entity.id, result.id)
    }

}
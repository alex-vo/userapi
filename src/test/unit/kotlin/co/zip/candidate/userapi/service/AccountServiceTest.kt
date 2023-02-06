package co.zip.candidate.userapi.service

import co.zip.candidate.userapi.entity.AccountEntity
import co.zip.candidate.userapi.entity.UserEntity
import co.zip.candidate.userapi.exception.UserValidationException
import co.zip.candidate.userapi.repository.AccountRepository
import co.zip.candidate.userapi.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class AccountServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var accountRepository: AccountRepository

    @InjectMocks
    lateinit var accountService: AccountService

    @Test
    fun `should fail to add account to a non-existing user`() {
        val userId = UUID.randomUUID()
        whenever(userRepository.findUserById(userId)).thenReturn(Optional.empty())

        assertThrows<ResponseStatusException> { accountService.create(userId) }
            .let {
                assertEquals(HttpStatus.BAD_REQUEST, it.status)
                assertEquals("user not found", it.reason)
            }
    }


    @Test
    fun `should fail to add account due to low salary-expenses ratio`() {
        val userId = UUID.randomUUID()
        whenever(userRepository.findUserById(userId)).thenReturn(Optional.of(UserEntity("", "", 1000_00, 1)))

        assertThrows<UserValidationException> { accountService.create(userId) }
            .let {
                assertEquals("salary/expenses ratio too low", it.message)
            }
    }

    @Test
    fun `should add account successfully`() {
        val userId = UUID.randomUUID()
        val userEntity = UserEntity("", "", 1000_00, 0)
        whenever(userRepository.findUserById(userId)).thenReturn(Optional.of(userEntity))
        whenever(accountRepository.save(any())).thenAnswer {
            it.getArgument<AccountEntity>(0)
        }

        accountService.create(userId)

        val accountCaptor = argumentCaptor<AccountEntity>()
        verify(accountRepository).save(accountCaptor.capture())
        assertEquals(accountCaptor.firstValue.user, userEntity)
    }

    @Test
    fun `should fail to list accounts of a non existing user`() {
        val userId = UUID.randomUUID()
        whenever(userRepository.existsById(userId)).thenReturn(false)

        assertThrows<ResponseStatusException> { accountService.get(userId) }
            .let {
                assertEquals(HttpStatus.BAD_REQUEST, it.status)
                assertEquals("user not found", it.reason)
            }
    }

    @Test
    fun `should list accounts of a user`() {
        val userId = UUID.randomUUID()
        whenever(userRepository.existsById(userId)).thenReturn(true)
        val user = UserEntity("", "", 0, 0)
        val account1 = AccountEntity(user)
        val account2 = AccountEntity(user)
        whenever(accountRepository.findByUserId(userId)).thenReturn(listOf(account1, account2))

        val result = accountService.get(userId)

        assertThat(result).hasSize(2)
        assertEquals(account1.id, result[0].id)
        assertEquals(account2.id, result[1].id)
    }

}


/*
talk to 3rd party services
get news information
ApiController
NewsService
...
CNNService
BBCService
...
CNNScheduler
BBCScheduler
...
CNNMapper
BBCMapper
...


 */
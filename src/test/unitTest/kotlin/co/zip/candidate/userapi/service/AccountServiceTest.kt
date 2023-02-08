package co.zip.candidate.userapi.service

import co.zip.candidate.userapi.config.AccountBalanceProperties
import co.zip.candidate.userapi.dto.AccountDTO
import co.zip.candidate.userapi.entity.AccountEntity
import co.zip.candidate.userapi.entity.UserEntity
import co.zip.candidate.userapi.exception.EntityNotFoundException
import co.zip.candidate.userapi.exception.SalaryExpensesRatioException
import co.zip.candidate.userapi.repository.AccountRepository
import co.zip.candidate.userapi.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class AccountServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var accountRepository: AccountRepository

    val accountBalanceProperties: AccountBalanceProperties = AccountBalanceProperties(60000)

    lateinit var accountService: AccountService

    @BeforeEach
    fun setUp() {
        accountService = AccountService(userRepository, accountRepository, accountBalanceProperties)
    }

    @Test
    fun `should fail to add account to a non-existing user`() {
        val userId = UUID.randomUUID()
        whenever(userRepository.findUserById(userId)).thenReturn(Optional.empty())

        assertThrows<EntityNotFoundException> { accountService.create(userId) }
            .let {
                assertEquals("User $userId not found", it.message)
            }
    }

    @Test
    fun `should fail to add account due to low salary-expenses ratio`() {
        val userId = UUID.randomUUID()
        whenever(userRepository.findUserById(userId)).thenReturn(
            Optional.of(
                UserEntity(
                    name = "",
                    email = "",
                    monthlySalary = 600_00,
                    monthlyExpenses = 1
                )
            )
        )

        assertThrows<SalaryExpensesRatioException> {
            accountService.create(userId)
        }.let {
            assertEquals("Salary/expenses ratio too low for user $userId", it.message)
        }
    }

    @Test
    fun `should add account successfully`() {
        val userId = UUID.randomUUID()
        val userEntity = UserEntity(name = "", email = "", monthlySalary = 1000_00, monthlyExpenses = 0)
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

        assertThrows<EntityNotFoundException> {
            accountService.get(userId)
        }.let {
            assertEquals("User $userId not found", it.message)
        }
    }

    @Test
    fun `should list accounts of a user`() {
        val userId = UUID.randomUUID()
        val user = UserEntity(name = "", email = "", monthlySalary = 0, monthlyExpenses = 0)
        val account1 = AccountEntity(user)
        val account2 = AccountEntity(user)
        user.accounts = listOf(account1, account2)
        whenever(userRepository.findById(userId)).thenReturn(Optional.of(user))

        val result = accountService.get(userId)

        assertThat(result)
            .containsExactly(
                AccountDTO(account1.id),
                AccountDTO(account2.id),
            )
    }

}

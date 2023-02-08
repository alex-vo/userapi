package co.zip.candidate.userapi.service

import co.zip.candidate.userapi.config.AccountBalanceProperties
import co.zip.candidate.userapi.dto.AccountDTO
import co.zip.candidate.userapi.entity.AccountEntity
import co.zip.candidate.userapi.exception.EntityNotFoundException
import co.zip.candidate.userapi.exception.SalaryExpensesRatioException
import co.zip.candidate.userapi.mapper.toDTO
import co.zip.candidate.userapi.repository.AccountRepository
import co.zip.candidate.userapi.repository.UserRepository
import co.zip.candidate.userapi.repository.getUserById
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class AccountService(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository,
    private var accountBalanceProperties: AccountBalanceProperties
) {

    @Transactional
    fun create(userId: UUID): AccountDTO {
        val user = userRepository.getUserById(userId)

        if (user.monthlySalary - user.monthlyExpenses < accountBalanceProperties.minValue) {
            throw SalaryExpensesRatioException("Salary/expenses ratio too low for user $userId")
        }

        return accountRepository.save(AccountEntity(user))
            .toDTO()
    }

    fun get(userId: UUID): List<AccountDTO> =
        userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User $userId not found") }
            .accounts
            .map { it.toDTO() }

}
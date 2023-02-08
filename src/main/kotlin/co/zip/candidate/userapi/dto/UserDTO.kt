package co.zip.candidate.userapi.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class UserDTO(
    val id: UUID? = null,
    @field:NotBlank
    val name: String,
    @field:NotBlank
    @field:Email
    val email: String,
    @field:Min(value = 1, message = "must be a positive number")
    val monthlySalary: Long,
    @field:Min(value = 1, message = "must be a positive number")
    val monthlyExpenses: Long,
    val accounts: List<AccountDTO> = emptyList()
)
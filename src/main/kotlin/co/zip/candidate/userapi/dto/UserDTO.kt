package co.zip.candidate.userapi.dto

import java.util.UUID
import javax.validation.constraints.Email
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

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
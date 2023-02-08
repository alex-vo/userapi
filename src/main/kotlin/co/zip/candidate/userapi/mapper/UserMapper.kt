package co.zip.candidate.userapi.mapper

import co.zip.candidate.userapi.dto.UserDTO
import co.zip.candidate.userapi.entity.AccountEntity
import co.zip.candidate.userapi.entity.UserEntity

fun UserDTO.toEntity() =
    UserEntity(name = name, email = email, monthlySalary = monthlySalary, monthlyExpenses = monthlyExpenses)

fun UserEntity.toDTO() = UserDTO(id, name, email, monthlySalary, monthlyExpenses, accounts.map(AccountEntity::toDTO))
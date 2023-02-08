package co.zip.candidate.userapi.mapper

import co.zip.candidate.userapi.dto.AccountDTO
import co.zip.candidate.userapi.entity.AccountEntity

fun AccountEntity.toDTO() = AccountDTO(id)
package co.zip.candidate.userapi.repository

import co.zip.candidate.userapi.entity.AccountEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountRepository : CrudRepository<AccountEntity, UUID> {
    fun findByUserId(userId: UUID): List<AccountEntity>
}
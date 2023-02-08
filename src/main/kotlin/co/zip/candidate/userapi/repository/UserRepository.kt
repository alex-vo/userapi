package co.zip.candidate.userapi.repository

import co.zip.candidate.userapi.entity.UserEntity
import co.zip.candidate.userapi.exception.EntityNotFoundException
import jakarta.persistence.LockModeType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface UserRepository : CrudRepository<UserEntity, UUID> {

    fun findAll(pageable: Pageable): Page<UserEntity>

    @Lock(LockModeType.PESSIMISTIC_READ)
    fun findUserById(id: UUID): Optional<UserEntity>

}

fun UserRepository.getUserById(id: UUID): UserEntity {
    return findUserById(id)
        .orElseThrow { EntityNotFoundException("User $id not found") }
}
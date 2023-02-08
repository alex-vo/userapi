package co.zip.candidate.userapi.service

import co.zip.candidate.userapi.dto.UserDTO
import co.zip.candidate.userapi.exception.EmailAlreadyTakenException
import co.zip.candidate.userapi.exception.EntityNotFoundException
import co.zip.candidate.userapi.mapper.toDTO
import co.zip.candidate.userapi.mapper.toEntity
import co.zip.candidate.userapi.repository.UserRepository
import org.hibernate.exception.ConstraintViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

const val EMAIL_UNIQUE_CONSTRAINT_NAME = "user_email_key"

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getUsers(pageable: Pageable): Page<UserDTO> =
        userRepository.findAll(pageable)
            .map { it.toDTO() }

    fun get(id: UUID): UserDTO =
        userRepository.findById(id)
            .orElseThrow { EntityNotFoundException("User $id not found") }
            .toDTO()

    fun create(userDTO: UserDTO): UserDTO =
        runCatching { userRepository.save(userDTO.toEntity()) }
            .onFailure { handleUserCreationFailure(it, userDTO.email) }
            .map { it.toDTO() }
            .getOrThrow()

    private fun handleUserCreationFailure(it: Throwable, email: String) {
        if (it.cause == null ||
            it.cause !is ConstraintViolationException ||
            EMAIL_UNIQUE_CONSTRAINT_NAME != (it.cause as ConstraintViolationException).constraintName
        ) throw it

        throw EmailAlreadyTakenException("Email $email already taken")
    }

}
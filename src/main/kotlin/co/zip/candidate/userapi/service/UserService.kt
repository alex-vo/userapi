package co.zip.candidate.userapi.service

import co.zip.candidate.userapi.dto.UserDTO
import co.zip.candidate.userapi.mapper.toDTO
import co.zip.candidate.userapi.mapper.toEntity
import co.zip.candidate.userapi.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun getUsers(pageable: Pageable): Page<UserDTO> =
        userRepository.findAll(pageable)
            .map { it.toDTO() }

    fun get(id: UUID): UserDTO =
        userRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(HttpStatus.BAD_REQUEST, "user not found")
            }
            .toDTO()

    fun create(userDTO: UserDTO): UserDTO {
        if (userRepository.existsByEmail(userDTO.email)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "email already taken")
        }

        return userRepository.save(userDTO.toEntity())
            .toDTO()
    }

}
package co.zip.candidate.userapi.service

import co.zip.candidate.userapi.dto.UserDTO
import co.zip.candidate.userapi.entity.UserEntity
import co.zip.candidate.userapi.mapper.toEntity
import co.zip.candidate.userapi.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var userService: UserService

    @Test
    fun `should throw ResponseStatusException if user not found`() {
        whenever(userRepository.findById(any())).thenReturn(Optional.empty())

        assertThrows<ResponseStatusException> { userService.get(UUID.randomUUID()) }
            .let {
                assertEquals(HttpStatus.BAD_REQUEST, it.status)
                assertEquals("user not found", it.reason)
            }
    }

    @Test
    fun `should get a user`() {
        val userEntity = UserEntity(name = "John Doe", email = "john@doe.com", monthlySalary = 2, monthlyExpenses = 1)
        whenever(userRepository.findById(userEntity.id)).thenReturn(Optional.of(userEntity))

        val result = userService.get(userEntity.id)

        with(result) {
            assertEquals(userEntity.id, id)
            assertEquals("John Doe", name)
            assertEquals("john@doe.com", email)
            assertEquals(2, monthlySalary)
            assertEquals(1, monthlyExpenses)
        }
    }

    @Test
    fun `should fail to create a user if email is already used`() {
        whenever(userRepository.existsByEmail("john@doe.com")).thenReturn(true)

        assertThrows<ResponseStatusException> {
            userService.create(
                UserDTO(
                    name = "John Doe",
                    email = "john@doe.com",
                    monthlySalary = 2,
                    monthlyExpenses = 1
                )
            )
        }
            .let {
                assertEquals(HttpStatus.BAD_REQUEST, it.status)
                assertEquals("email already taken", it.reason)
            }
    }

    @Test
    fun `should create a user`() {
        val userDTO = UserDTO(name = "John Doe", email = "john@doe.com", monthlySalary = 2, monthlyExpenses = 1)
        whenever(userRepository.save(any())).thenAnswer {
            it.getArgument<UserEntity>(0)
        }
        whenever(userRepository.existsByEmail("john@doe.com")).thenReturn(false)

        val result = userService.create(userDTO)

        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(userDTO)
        val userCaptor = argumentCaptor<UserEntity>()
        verify(userRepository).save(userCaptor.capture())
        assertThat(userCaptor.firstValue)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(userDTO.toEntity())
    }

}
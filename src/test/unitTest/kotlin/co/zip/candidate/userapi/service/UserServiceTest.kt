package co.zip.candidate.userapi.service

import co.zip.candidate.userapi.dto.UserDTO
import co.zip.candidate.userapi.entity.UserEntity
import co.zip.candidate.userapi.exception.EmailAlreadyTakenException
import co.zip.candidate.userapi.exception.EntityNotFoundException
import co.zip.candidate.userapi.mapper.toEntity
import co.zip.candidate.userapi.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.hibernate.exception.ConstraintViolationException
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

        val id = UUID.randomUUID()
        assertThrows<EntityNotFoundException> { userService.get(id) }
            .let {
                assertEquals("User $id not found", it.message)
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
    fun `should rethrow generic exception on user creation failure`() {
        val runtimeException = RuntimeException("123")
        whenever(userRepository.save(any())).thenThrow(runtimeException)

        assertThrows<RuntimeException> {
            userService.create(
                UserDTO(name = "John Doe", email = "john@doe.com", monthlySalary = 2, monthlyExpenses = 1)
            )
        }.let {
            assertEquals(runtimeException, it)
        }
    }

    @Test
    fun `should throw EmailAlreadyTakenException if email is already used`() {
        val cve = ConstraintViolationException("", null, EMAIL_UNIQUE_CONSTRAINT_NAME)
        whenever(userRepository.save(any())).thenThrow(RuntimeException(cve))

        assertThrows<EmailAlreadyTakenException> {
            userService.create(
                UserDTO(name = "John Doe", email = "john@doe.com", monthlySalary = 2, monthlyExpenses = 1)
            )
        }.let {
            assertEquals("Email john@doe.com already taken", it.message)
        }
    }

    @Test
    fun `should create a user`() {
        val userDTO = UserDTO(name = "John Doe", email = "john@doe.com", monthlySalary = 2, monthlyExpenses = 1)
        whenever(userRepository.save(any())).thenAnswer {
            it.getArgument<UserEntity>(0)
        }

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
package co.zip.candidate.userapi.controller

import co.zip.candidate.userapi.dto.UserDTO
import co.zip.candidate.userapi.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping("/v1/users")
class UsersController(
    private val userService: UserService
) {

    @GetMapping
    fun getAll(pageable: Pageable) = userService.getUsers(pageable)

    @GetMapping("{id}")
    fun get(@PathVariable id: UUID) = userService.get(id)

    @PostMapping
    fun create(@Valid @RequestBody userDTO: UserDTO) = userService.create(userDTO)

}
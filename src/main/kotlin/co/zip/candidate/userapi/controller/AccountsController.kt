package co.zip.candidate.userapi.controller

import co.zip.candidate.userapi.dto.AccountDTO
import co.zip.candidate.userapi.service.AccountService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/users/{userId}/accounts")
class AccountsController(
    private val accountService: AccountService
) {

    @PostMapping
    fun create(@PathVariable userId: UUID): AccountDTO = accountService.create(userId)

    @GetMapping
    fun get(@PathVariable userId: UUID): List<AccountDTO> = accountService.get(userId)

}
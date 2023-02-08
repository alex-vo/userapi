package co.zip.candidate.userapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "account.balance")
data class AccountBalanceProperties
@ConstructorBinding constructor(
    val minValue: Long
)
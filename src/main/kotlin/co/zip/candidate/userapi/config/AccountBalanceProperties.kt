package co.zip.candidate.userapi.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "account.balance")
@ConstructorBinding
data class AccountBalanceProperties(
    val minValue: Long
)
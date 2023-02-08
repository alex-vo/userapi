package co.zip.candidate.userapi.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(AccountBalanceProperties::class)
class BusinessRuleConfig
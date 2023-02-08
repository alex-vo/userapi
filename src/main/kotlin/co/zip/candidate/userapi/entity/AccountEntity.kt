package co.zip.candidate.userapi.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "account")
open class AccountEntity(
    @ManyToOne
    val user: UserEntity,
) {
    @Id
    val id: UUID = UUID.randomUUID()
}
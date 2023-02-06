package co.zip.candidate.userapi.entity

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "account")
data class AccountEntity(
    @ManyToOne
    val user: UserEntity,
) {
    @Id
    val id: UUID = UUID.randomUUID()
}
package co.zip.candidate.userapi.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "`user`")
open class UserEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val monthlySalary: Long,
    val monthlyExpenses: Long,
) {
    @OneToMany(targetEntity = AccountEntity::class, cascade = [CascadeType.ALL], mappedBy = "user")
    var accounts: List<AccountEntity> = emptyList()
}
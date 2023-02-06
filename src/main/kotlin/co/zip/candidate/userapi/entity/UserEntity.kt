package co.zip.candidate.userapi.entity

import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "`user`")
data class UserEntity(
    val name: String,
    val email: String,
    val monthlySalary: Long,
    val monthlyExpenses: Long,
) {
    @Id
    val id: UUID = UUID.randomUUID()

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
    val accounts: List<AccountEntity> = emptyList()
}
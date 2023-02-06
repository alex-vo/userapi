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
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val email: String,
    val monthlySalary: Long,
    val monthlyExpenses: Long,
) {
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
    var accounts: List<AccountEntity> = emptyList()
}
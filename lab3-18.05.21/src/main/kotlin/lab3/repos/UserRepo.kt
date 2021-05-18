package lab3.repos

import lab3.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository interface UserRepo: JpaRepository<User, Long> {

    infix fun getByEmail(email: String): User

    infix fun getByUserId(id: Long): User
}
package lab2.repos

import lab2.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository interface UserRepo: JpaRepository<User, Long> {

    infix fun getByEmail(email: String): User

    infix fun getByUserId(id: Long): User
}
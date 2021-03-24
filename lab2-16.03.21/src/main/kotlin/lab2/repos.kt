package lab2

import lab2.models.Advert
import lab2.models.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository interface UserRepo: CrudRepository<User, Long> {
    fun getByEmail(email: String): User

    fun getByUserId(id: Long): User

    fun existsByEmail(email: String): Boolean
}

@Repository interface AdvertRepo: CrudRepository<Advert, Long> {
    fun getByAdvertId(id: Long): Advert
}
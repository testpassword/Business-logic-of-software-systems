package lab2

import lab2.models.Advert
import lab2.models.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository interface UserRepo: CrudRepository<User, Long> {

    infix fun getByEmail(email: String): User

    infix fun getByUserId(id: Long): User
}

@Repository interface AdvertRepo: CrudRepository<Advert, Long> {

    infix fun getByAdvertId(id: Long): Advert
}
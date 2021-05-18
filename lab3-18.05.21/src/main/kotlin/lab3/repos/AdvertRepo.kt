package lab3.repos

import lab3.models.Advert
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository interface AdvertRepo: JpaRepository<Advert, Long> {

    infix fun getByAdvertId(id: Long): Advert
}
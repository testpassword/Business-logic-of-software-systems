package lab2.repos

import lab2.models.Advert
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository interface AdvertRepo: JpaRepository<Advert, Long> {

    infix fun getByAdvertId(id: Long): Advert
}
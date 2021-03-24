package lab2.services

import lab2.AdvertRepo
import lab2.models.Advert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service class AdvertService {

    @Autowired private lateinit var repo: AdvertRepo
    @Autowired private lateinit var moderator: ModeratorService

    infix fun add(advert: Advert): Boolean =
        if (moderator.moderate(advert)) {
            repo.save(advert)
            true
        } else false

    fun getAll(): List<Advert> = repo.findAll().toList()

    infix fun get(id: Long): Advert = repo.getByAdvertId(id)

    infix fun delete(advert: Advert) = repo.delete(advert)
}
package lab2.services

import lab2.AdvertRepo
import lab2.models.Advert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service class AdvertService {

    @Autowired private lateinit var repo: AdvertRepo
    @Autowired private lateinit var moderator: ModeratorService

    infix fun add(advert: Advert) =
        if (moderator.moderate(advert)) {
            repo.save(advert)
            true
        } else false

    infix fun modify(modified: Map<String, String>) =
        repo.save(get(modified["advertId"]!!.toLong()).apply {
            modified["cost"]?.let { cost = it.toInt() }
            modified["typeOfAdvert"]?.let { typeOfAdvert = Advert.TYPE_OF_ADVERT.valueOf(it) }
            modified["typeOfEstate"]?.let { typeOfEstate = Advert.TYPE_OF_ESTATE.valueOf(it) }
            modified["location"]?.let { location = it }
            modified["quantityOfRooms"]?.let { quantityOfRooms = it.toInt() }
            modified["area"]?.let { area = it.toInt() }
            modified["floor"]?.let { floor = it.toInt() }
            modified["description"]?.let { description = it }
            modified["name"]?.let { name = it }
            modified["mobileNumber"]?.let { mobileNumber = it }
            modified["isRealtor"]?.let { isRealtor = it.toBoolean() }
            modified["image"]?.let { image = it }
        })

    fun getAll() = repo.findAll().toList()

    infix operator fun get(id: Long) = repo.getByAdvertId(id)

    infix fun delete(advert: Advert) = repo.delete(advert)
}
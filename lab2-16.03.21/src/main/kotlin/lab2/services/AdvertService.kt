package lab2.services

import lab2.AdvertRepo
import lab2.models.Advert
import lab2.utils.AutoModerator
import lab2.utils.Postman
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service class AdvertService {

    @Autowired private lateinit var repo: AdvertRepo

    infix fun add(advert: Advert) =
        with(AutoModerator(advert)) {
            val (status, prohibited) = this
            if (status) {
                repo.save(advert)
                true
            } else {
                Postman(advert.user.email,
                    "Automatic moderation failed",
                    """
                    Failed automatic moderation cause you use prohibited words:
                    $prohibited
                    Replace them with something else
                    """.trimIndent()
                )
                false
            }
        }

    fun changeStatus(advertId: Long, status: Advert.STATUS) {

    }

    fun modify(advertId: Long, modified: Map<String, String>) =
        repo.save(get(advertId).apply {
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
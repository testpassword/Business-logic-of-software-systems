package lab2.services

import lab2.models.Advert
import lab2.repos.AdvertRepo
import lab2.utils.AutoModerator
import lab2.utils.Postman
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service class AdvertService {

    @Autowired private lateinit var repo: AdvertRepo
    @Autowired private lateinit var postman: Postman

    @Transactional infix fun add(advert: Advert) =
        with(AutoModerator(advert)) {
            println(this)
            println("AUF ${this.isEmpty()}")
            if (isEmpty()) {
                repo.save(advert)
                println("AUF2")
                true
            } else {
                postman(advert.user.email,
                    "Automatic moderation failed",
                    """
                    Failed automatic moderation cause you use prohibited words:
                    $this
                    Replace them with something else
                    """.trimIndent()
                )
                false
            }
        }

    @Transactional fun changeStatus(advertId: Long, status: Advert.STATUS, comment: String = "") =
        with(get(advertId)) {
            val template: (String) -> Unit = { postman(this.user.email, "Moderation result", it) }
            this.status = status
                when (status) {
                    Advert.STATUS.DECLINED -> {
                        template("The moderator decided to remove your ad for the following reason:\n$comment")
                        repo.delete(this)
                    }
                    Advert.STATUS.ON_MODERATION -> {
                        template("The moderator decided that you need to modify the ad due to:\n$comment")
                        repo.save(this)
                    }
                    Advert.STATUS.APPROVED -> {
                        template("Congratulations! Ad approved.")
                        repo.save(this)
                    }
                }
        }

    @Transactional fun modify(advertId: Long, modified: Map<String, String>) =
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
            status = Advert.STATUS.ON_MODERATION
        })

    fun getAll(status: Advert.STATUS = Advert.STATUS.APPROVED) = repo.findAll().filter { it.status == status }.toList()

    infix operator fun get(id: Long) = repo.getByAdvertId(id)

    infix fun delete(advert: Advert) = repo.delete(advert)
}
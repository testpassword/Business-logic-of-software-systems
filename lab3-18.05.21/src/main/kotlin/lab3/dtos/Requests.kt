package lab3.dtos

import lab3.models.Advert
import lab3.models.User
import java.io.Serializable

open class Req: Serializable

data class UserReq(val email: String = "",
                   val password: String = "",
                   val name: String = "",
                   val payload: String = ""): Req()


data class AdvertReq(val userId: Long = 0,
                     val cost: Int,
                     val typeOfAdvert: Advert.TYPE_OF_ADVERT = Advert.TYPE_OF_ADVERT.SALE,
                     val typeOfEstate: Advert.TYPE_OF_ESTATE = Advert.TYPE_OF_ESTATE.FLAT,
                     val location: String = "",
                     val quantityOfRooms: Int = 1,
                     val area: Int = 1,
                     val floor: Int = 1,
                     val description: String = "",
                     val name: String = "",
                     val mobileNumber: String = "",
                     val isRealtor: Boolean = false,
                     val image: String = "",
                     val status: Advert.STATUS = Advert.STATUS.ON_MODERATION): Req() {

    fun toAdvert() = Advert().apply {
        this.cost = this@AdvertReq.cost
        this.typeOfAdvert = this@AdvertReq.typeOfAdvert
        this.typeOfEstate = this@AdvertReq.typeOfEstate
        this.location = this@AdvertReq.location
        this.quantityOfRooms = this@AdvertReq.quantityOfRooms
        this.area = this@AdvertReq.area
        this.floor = this@AdvertReq.floor
        this.description = this@AdvertReq.description
        this.name = this@AdvertReq.name
        this.mobileNumber = this@AdvertReq.mobileNumber
        this.isRealtor = this@AdvertReq.isRealtor
        this.image = this@AdvertReq.image
    }
}

data class ModerateReq(val comment: String = "",
                       val status: Advert.STATUS = Advert.STATUS.ON_MODERATION): Req()


data class AdminReq(val userRole: User.ROLE? = null,
                    val userStatus: User.STATUS? = null): Req()
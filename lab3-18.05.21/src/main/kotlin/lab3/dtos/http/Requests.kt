package lab3.dtos.http

import lab3.models.Advert
import lab3.models.User
import java.io.Serializable

open class Req: Serializable

data class UserReq(val email: String = "",
                   val password: String = "",
                   val name: String = "",
                   val payload: String = ""): Req()

data class AdvertReq(val userId: Long?,
                     val cost: Int?,
                     val typeOfAdvert: Advert.TYPE_OF_ADVERT?,
                     val typeOfEstate: Advert.TYPE_OF_ESTATE?,
                     val location: String?,
                     val quantityOfRooms: Int?,
                     val area: Int?,
                     val floor: Int?,
                     val description: String?,
                     val name: String?,
                     val mobileNumber: String?,
                     val isRealtor: Boolean?,
                     val image: String?,
                     val status: Advert.STATUS?): Req() {

    fun fillAdvert(proto: Advert): Advert {
        this.cost?.let { proto.cost = it }
        this.typeOfAdvert?.let { proto.typeOfAdvert = it }
        this.typeOfEstate?.let { proto.typeOfEstate = it }
        this.location?.let { proto.location = it }
        this.quantityOfRooms?.let { proto.quantityOfRooms = it }
        this.area?.let { proto.area = it }
        this.floor?.let { proto.floor = it }
        this.description?.let { proto.description = it }
        this.name?.let { proto.name = it }
        this.mobileNumber?.let { proto.mobileNumber = it }
        this.isRealtor?.let { proto.isRealtor = it }
        this.image?.let { proto.image = it }
        return proto
    }
}

data class ModerateReq(val comment: String = "",
                       val status: Advert.STATUS = Advert.STATUS.ON_MODERATION): Req()


data class AdminReq(val userRole: User.ROLE? = null,
                    val userStatus: User.STATUS? = null): Req()
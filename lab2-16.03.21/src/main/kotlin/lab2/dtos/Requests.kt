package lab2.dtos

import lab2.models.Advert
import lab2.models.User

data class UserReq(val email: String = "",
                   val password: String = "",
                   val username: String) {

    fun toUser() = User(email, password).apply { this.name = username }
}

//TODO: сериализация
data class AdvertReq(val advertsIds: Array<Long> = emptyArray(),
                     val userId: Long = 0,
                     val cost: Int,
                     val typeOfAdvert: Advert.TYPE_OF_ADVERT = Advert.TYPE_OF_ADVERT.SALE,
                     val typeOfEstate: Advert.TYPE_OF_ESTATE = Advert.TYPE_OF_ESTATE.FLAT,
                     val location: String = "",
                     val quantityOfRooms: Int = 0,
                     val area: Int = 0,
                     val floor: Int = 0,
                     val description: String = "",
                     val name: String = "",
                     val mobileNumber: String = "",
                     val isRealtor: Boolean = false,
                     val image: String = "") {

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
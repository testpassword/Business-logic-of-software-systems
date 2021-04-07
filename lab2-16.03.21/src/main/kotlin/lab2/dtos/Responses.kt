package lab2.dtos

import kotlinx.serialization.Serializable
import lab2.models.Advert
import lab2.models.User

@Serializable open class Res(var msg: String = "")

data class AdvertRes(var adverts: List<Advert> = emptyList()): Res()

data class UserRes(var token: String = "", var users: List<User> = emptyList()): Res()
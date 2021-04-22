package lab2.dtos

import lab2.models.Advert
import lab2.models.User
import java.io.Serializable

open class Res(open var msg: String = ""): Serializable

data class AdvertRes(var adverts: List<Advert> = emptyList()): Res()

data class UserRes(var token: String = "", var users: List<User> = emptyList()): Res()

data class ModeratorRes(var adverts: List<Advert> = emptyList(), override var msg: String = ""): Res()
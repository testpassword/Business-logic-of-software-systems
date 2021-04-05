package lab2.dtos

import lab2.models.Advert
import lab2.models.User

data class AdvertRes(var adverts: List<Advert> = emptyList(),
                     var msg: String = "")

data class UserRes(var token: String = "",
                   var users: List<User> = emptyList(),
                   var msg: String = "")
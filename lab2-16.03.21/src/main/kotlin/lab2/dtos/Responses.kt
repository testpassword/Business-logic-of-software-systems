package lab2.dtos

import lab2.models.Advert
import lab2.models.User
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class AdvertRes(val adverts: List<Advert> = emptyList(),
                     val msg: String = "")

data class UserRes(val token: String = "",
                   val msg: String = "",
                   val users: List<User> = emptyList())

fun <T> bad(body: T): ResponseEntity<T> = ResponseEntity(body, HttpStatus.BAD_REQUEST)
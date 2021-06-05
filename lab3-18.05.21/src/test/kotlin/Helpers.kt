import khttp.responses.Response
import org.json.JSONObject
import kotlin.test.assertTrue

data class SimpleUser(val email: String,
                      val password: String,
                      val name: String,
                      var token: String = "")

data class SimpleRes(val json: JSONObject,
                     val code: Int) {

    fun setToken(su: SimpleUser) { su.token = this.json["token"].toString() }

    fun check() = assertTrue { this.code in (200..299) }
}

fun Response.simplify() = SimpleRes(this.jsonObject, this.statusCode)

val API_ROOT = "http://localhost:17502/"
val API = mapOf(
    "root" to API_ROOT,
    "admin" to "${API_ROOT}admin/",
    "moderate" to "${API_ROOT}moderate/",
    "user" to "${API_ROOT}user/",
    "advert" to "${API_ROOT}advert/"
)

val USER = SimpleUser("pspfanGTA@gmail.com", "root", "pupa")
val MODERATOR = SimpleUser("kulbakolearn@mail.ru", "root", "lupa")
val ADMIN = SimpleUser("kulbakoimportant@mail.ru", "toor", "god")
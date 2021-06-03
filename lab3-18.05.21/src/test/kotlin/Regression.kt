import khttp.*
import org.json.JSONObject
import org.junit.jupiter.api.*

data class SimpleUser(val email: String,
                      val password: String,
                      val name: String,
                      var token: String = "")

private fun JSONObject.setToken(su: SimpleUser) { su.token = this["token"].toString() }

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Regression {

    val API_ROOT = "http://localhost:17502/"
    val API = mapOf(
        "root" to API_ROOT,
        "admin" to "${API_ROOT}admin/",
        "moderate" to "${API_ROOT}user/",
        "user" to "${API_ROOT}user/",
        "advert" to "${API_ROOT}advert/"
    )
    val USER = SimpleUser("pspfanGTA@gmail.com", "root", "pupa")
    val MODERATOR = SimpleUser("kulbakolearn@mail.ru", "root", "lupa")
    val ADMIN = SimpleUser("god@lol.kek", "toor", "god")

    @Test @Order(1) fun `register user 1`() =
        with(put(
            url = "${API["user"]}register",
            json = mapOf(
                "email" to USER.email,
                "password" to USER.password,
                "name" to USER.name
            )
        ).jsonObject) {
            println(this)
            setToken(USER)
        }

    @Test @Order(2) fun `register user 2`() =
        with(put(
            url = "${API["user"]}register",
            json = mapOf(
                "email" to MODERATOR.email,
                "password" to MODERATOR.password,
                "name" to MODERATOR.name
            )
        ).jsonObject) {
            println(this)
            setToken(MODERATOR)
        }

    @Test @Order(3) fun `admin makes second user as moderator`() {
        with(post(
            url = "${API["user"]}login",
            json = mapOf(
                "email" to ADMIN.email,
                "password" to ADMIN.password,
            )
        ).jsonObject) {
            println(this)
            setToken(ADMIN)
        }
        println(patch(
            url = "${API["admin"]}change_role/3",
            json = mapOf(
                "userId" to 3,
                "userRole" to "MODERATOR"
            ),
            headers = mapOf(
                "Authorization" to "Bearer ${ADMIN.token}"
            )
        ).jsonObject)
    }

    @Test @Order(4) fun `user add advert`() =
        println(post(
            url = "${API["advert"]}add",
            json = mapOf(
                "cost" to 10000,
                "location" to "SPB",
                "quantityOfRooms" to 3,
                "area" to 86,
                "name" to "flat",
                "mobileNumber" to "88005553535"),
            headers = mapOf(
                "Authorization" to "Bearer ${USER.token}"
            )
        ).jsonObject)

    @Test @Order(5) fun `moderator approved advert`() =
        println(post(
            url = "${API["moderate"]}change/1",
            json = mapOf(
                "status" to "APPROVED"
            ),
            headers = mapOf(
                "Authorization" to "Bearer ${MODERATOR.token}"
            )
        ).jsonObject)

    @Test @Order(6) fun `user checks status changing`() =
        println(get(
            url = "${API["advert"]}1"
        ).jsonObject)
}
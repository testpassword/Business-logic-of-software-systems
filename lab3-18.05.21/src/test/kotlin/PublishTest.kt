import khttp.*
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.*
import java.io.File
import kotlin.test.assertEquals

@TestMethodOrder(MethodOrderer.OrderAnnotation::class) @TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PublishTest {

    @BeforeAll fun prepare() =
        with(RegistrationTest()) {
            `register user 1`()
            `register user 2`()
            `admin makes second user as moderator`()
        }

    @Test @Order(1) fun `user add advert`() =
        with(post(
            url = "${API["advert"]}add",
            json = mapOf(
                "cost" to 10000,
                "location" to "SPB",
                "quantityOfRooms" to 3,
                "area" to 86,
                "name" to "flat",
                "mobileNumber" to "88005553535",
                "image" to File("V:/itmo/3 course/business logic of software systems/lab3-18.05.21/src/test/resources/test_image.txt")
                    .readLines()
                    .joinToString(separator = "")
            ),
            headers = mapOf(
                "Authorization" to "Bearer ${USER.token}"
            )
        ).simplify()) {
            println(this)
            check()
        }

    @Test @Order(2) fun `moderator approved advert`() =
        with(post(
            url = "${API["moderate"]}change/1",
            json = mapOf(
                "status" to "APPROVED"
            ),
            headers = mapOf(
                "Authorization" to "Bearer ${MODERATOR.token}"
            )
        ).simplify()) {
            println(this)
            check()
        }

    @Test @Order(3) fun `user checks status changing`() =
        with(get(
            url = "${API["advert"]}1"
        ).simplify()) {
            println(this)
            assertEquals("APPROVED", ((this.json["adverts"] as JSONArray)[0] as JSONObject)["status"])
        }
}
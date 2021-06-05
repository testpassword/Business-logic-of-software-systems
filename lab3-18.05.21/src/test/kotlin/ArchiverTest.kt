import org.junit.jupiter.api.*
import khttp.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.test.assertTrue

@TestMethodOrder(MethodOrderer.OrderAnnotation::class) @TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArchiverTest {

    @BeforeAll @Order(1)
    fun prepare() =
        with(PublishTest()) {
            prepare()
            `user add advert`()
            `moderator approved advert`()
        }

    @Test @Order(2)
    fun `close advert`() =
        with(put(
            url = "${API["advert"]}1",
            json = mapOf(
                "status" to "CLOSED"
            ),
            headers = mapOf(
                "Authorization" to "Bearer ${USER.token}"
            )
        ).simplify()) {
            println(this)
            check()
        }

    @Test @Order(3)
    fun `get archived image`() {
        val getImage: () -> String = {
            ((get(url = "${API["advert"]}1").jsonObject["adverts"] as JSONArray)[0] as JSONObject)["image"] as String
        }
        val originalImage = getImage()
        Thread.sleep(60_000)
        val compressedImage = getImage()
        println("""
            original: $originalImage 
            compressed: $compressedImage
        """.trimIndent())
        assertTrue { compressedImage.count() < originalImage.count() }
    }
}
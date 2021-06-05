import khttp.*
import org.junit.jupiter.api.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class) @TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatisticTest {

    @Test @BeforeAll
    fun `admin login`() =
        with(post(
            url = "${API["user"]}login",
            json = mapOf(
                "email" to ADMIN.email,
                "password" to ADMIN.password,
            )
        ).simplify()) {
            println(this)
            setToken(ADMIN)
            check()
        }

    @Test fun `get cached statistic`() =
        with(get(
            url = "${API["admin"]}stat/",
            params = mapOf(
                "isCached" to "true"
            ),
            headers = mapOf(
                "Authorization" to "Bearer ${ADMIN.token}"
            )
        ).simplify()) {
            println(this)
            check()
        }

    @Test fun `send request to new statistic`() =
        with(get(
            url = "${API["admin"]}stat/",
            params = mapOf(
                "isCached" to "false"
            ),
            headers = mapOf(
                "Authorization" to "Bearer ${ADMIN.token}"
            )
        ).simplify()) {
            println(this)
            check()
        }
}
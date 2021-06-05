import khttp.*
import org.junit.jupiter.api.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class) @TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationTest {

    @Test @Order(1)
    fun `register user 1`() =
        with(
            put(
            url = "${API["user"]}register",
            json = mapOf(
                "email" to USER.email,
                "password" to USER.password,
                "name" to USER.name
            )
        ).simplify()) {
            println(this)
            setToken(USER)
            check()
        }

    @Test @Order(2)
    fun `register user 2`() =
        with(
            put(
            url = "${API["user"]}register",
            json = mapOf(
                "email" to MODERATOR.email,
                "password" to MODERATOR.password,
                "name" to MODERATOR.name
            )
        ).simplify()) {
            println(this)
            setToken(MODERATOR)
            check()
        }

    @Test @Order(3)
    fun `admin makes second user as moderator`() {
        with(
            post(
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
        with(
            patch(
            url = "${API["admin"]}change_role/3",
            json = mapOf(
                "userId" to 3,
                "userRole" to "MODERATOR"
            ),
            headers = mapOf(
                "Authorization" to "Bearer ${ADMIN.token}"
            )
        ).simplify()) {
            println(this)
            check()
        }
    }
}
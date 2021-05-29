package lab3.services

import com.beust.klaxon.Klaxon
import lab3.models.User
import lab3.repos.UserRepo
import lab3.utils.Postman
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.mail.MailSendException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Service class  UserService: UserDetailsService {

    @Value("\${admin.email}") private lateinit var adminEmail: String
    @Value("\${admin.password}") private lateinit var adminPass: String
    @Value("\${admin.username}") private lateinit var adminName: String
    @Autowired private lateinit var repo: UserRepo
    @Autowired private lateinit var encoder: BCryptPasswordEncoder
    @Autowired private lateinit var postman: Postman
    @Autowired private lateinit var advertService: AdvertService
    private val log = KotlinLogging.logger {}

    @PostConstruct @Transactional
    fun initAdmin() =
        try {
            repo getByEmail adminEmail
            log.debug { "admin user already exists in db" }
        } catch (e: EmptyResultDataAccessException) {
            repo.save(User(adminEmail, encoder.encode(adminPass)).apply {
                name = adminName
                role = User.ROLE.ADMIN
            })
            log.debug { "admin user created" }
        }

    @Transactional fun add(email: String, password: String, name: String) =
        User(email, encoder.encode(password)).apply {
            repo.save(this)
            postman(email, "Register", "yeah!")
        }

    @Transactional infix fun delete(email: String) {
        repo.delete(repo.getByEmail(email))
        postman(email, "Goodbye", ":(")
    }

    @Transactional infix fun resetPassword(email: String) =
        with(loadUserByUsername(email)) {
            try {
                val tempPassword = (('a'..'z') + ('A'..'Z') + ('0'..'9')).shuffled().take(12).joinToString("")
                postman(email,
                    "Temporary password",
                    """
                    You temporary password '${tempPassword}'.
                    You should set new one on first login.
                    """.trimIndent())
                password = encoder.encode(tempPassword)
                save(this)
                true
            } catch (e: MailSendException) {
                log.error { e.message }
                false
            }
    }

    @Transactional fun modify(email: String, modified: Map<String, String>) =
        repo.save(loadUserByUsername(email).apply {
            modified["username"]?.let { name = it }
            modified["password"]?.let { password = it }
            modified["email"]?.let { this.email = it }
            modified["isDescriber"]?.let { this.isDescriber = it.toBoolean() }
        })

    // once in a week, on sunday night
    @Scheduled(cron = "* 0 0 * * 0") fun sendNewAdvertsEmails() {
        val newAdverts = advertService.getAll().filter { Date().time - it.publishDate.time > TimeUnit.HOURS.toMillis(1) }
        getAll().filter { it.isDescriber }.forEach { u ->
            postman(u.email, "New adverts available!", Klaxon().toJsonString(newAdverts.filterNot { u == it.user }))
        }
    }

    fun getAll() = repo.findAll().toList()

    infix operator fun get(id: Long) = repo getByUserId id

    override infix fun loadUserByUsername(email: String) = repo getByEmail email

    infix fun save(user: User) = repo.save(user)
}
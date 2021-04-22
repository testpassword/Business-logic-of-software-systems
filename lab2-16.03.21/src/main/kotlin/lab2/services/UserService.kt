package lab2.services

import lab2.models.User
import lab2.repos.UserRepo
import lab2.utils.Postman
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.mail.MailSendException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct

@Service class UserService: UserDetailsService {

    @Value("\${admin.email}") private lateinit var adminEmail: String
    @Value("\${admin.password}") private lateinit var adminPass: String
    @Value("\${admin.username}") private lateinit var adminName: String
    @Autowired private lateinit var repo: UserRepo
    @Autowired private lateinit var encoder: BCryptPasswordEncoder
    @Autowired private lateinit var postman: Postman
    private val log = KotlinLogging.logger {}

    @PostConstruct @Transactional
    fun `add admin user`() =
        try {
            repo getByEmail adminEmail
            log.info { "admin user already exists in db" }
        } catch (e: EmptyResultDataAccessException) {
            repo.save(User(adminEmail, encoder.encode(adminPass)).apply {
                name = adminName
                role = User.ROLE.ADMIN
            })
            log.info { "admin user created" }
        }

    @Transactional fun add(email: String, password: String, name: String) =
        User(email, encoder.encode(password)).apply {
            repo.save(this)
            try {
                postman(email, "Register", "yeah!")
            } catch (e: MailSendException) {
                log.error { e.message }
            }
        }

    @Transactional infix fun delete(email: String) {
        repo.delete(repo.getByEmail(email))
        try {
            postman(email, "Goodbye", ":(")
        } catch (e: MailSendException) {
            log.error { e.message }
        }
    }

    @Transactional infix fun resetPassword(email: String) =
        with(loadUserByUsername(email)) {
            try {
                val tempPassword = (('a'..'z') + ('A'..'Z') + ('0'..'9')).shuffled().take(12).joinToString("")
                postman(email,
                    "Temporary password",
                    """
                    You temporary password: ${tempPassword}.
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
        })

    fun getAll(): List<User> = repo.findAll().toList()

    infix operator fun get(id: Long) = repo getByUserId id

    override infix fun loadUserByUsername(email: String) = repo getByEmail email

    infix fun save(user: User) = repo.save(user)
}
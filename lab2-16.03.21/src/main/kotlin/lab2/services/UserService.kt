package lab2.services

import lab2.UserRepo
import lab2.models.User
import lab2.utils.Postman
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSendException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val LOG = KotlinLogging.logger {}

@Service class UserService: UserDetailsService {

    @Autowired private lateinit var repo: UserRepo
    @Autowired private lateinit var encoder: BCryptPasswordEncoder

    @Transactional fun add(email: String, password: String, name: String) =
        User(email, encoder.encode(password)).apply {
            try {
                Postman(email, "Register", "yeah!")
            } catch (e: MailSendException) {
                LOG.error { e.stackTraceToString() }
            }
        }

    infix fun delete(email: String) {
        repo.delete(repo.getByEmail(email))
        try {
            Postman(email, "Goodbye", ":(")
        } catch (e: MailSendException) {
            LOG.error { e.stackTraceToString() }
        }
    }

    @Transactional infix fun resetPassword(email: String) =
        with(loadUserByUsername(email)) {
            try {
                val tempPassword = (('a'..'z') + ('A'..'Z') + ('0'..'9')).shuffled().take(12).joinToString("")
                Postman(email,
                    "Temporary password",
                    """
                    You temporary password: ${tempPassword}.
                    You should set new one on first login.
                    """.trimIndent())
                password = encoder.encode(tempPassword)
                status = User.STATUS.LOCKED
                save(this)
                true
            } catch (e: MailSendException) {
                LOG.error { e.stackTraceToString() }
                false
            }
    }

    fun modify(email: String, modified: Map<String, String>) =
        repo.save(loadUserByUsername(email).apply {
            modified["username"]?.let { name = it }
            modified["password"]?.let { password = it }
            modified["email"]?.let { this.email = it }
        })

    fun getAll(): List<User> = repo.findAll().toList()

    infix operator fun get(id: Long) = repo getByUserId id

    override infix fun loadUserByUsername(email: String) = repo getByEmail email

    @Transactional infix fun save(user: User) = repo.save(user)
}
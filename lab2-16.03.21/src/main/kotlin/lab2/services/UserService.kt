package lab2.services

import lab2.UserRepo
import lab2.models.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSendException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service class UserService: UserDetailsService {

    @Autowired private lateinit var repo: UserRepo
    @Autowired private lateinit var postman: EmailService
    @Autowired private lateinit var encoder: BCryptPasswordEncoder

    @Transactional fun add(email: String, password: String, name: String): User {
        save(User(email, encoder.encode(password)))
        try {
            postman.send(email, "Register", "yeah!")
        } catch (e: MailSendException) {
            println("[ERROR] ${e.localizedMessage}")
        }
        return loadUserByUsername(email)
    }

    infix fun delete(email: String) {
        repo.delete(repo.getByEmail(email))
        try {
            postman.send(email, "Goodbye", ":(")
        } catch (e: MailSendException) {
            println("[ERROR] " + e.localizedMessage)
        }
    }

    fun getAll(): List<User> = repo.findAll().toList()

    infix operator fun get(id: Long) = repo getByUserId id

    override infix fun loadUserByUsername(email: String) = repo getByEmail email

    @Transactional infix fun save(user: User) = repo.save(user)
}
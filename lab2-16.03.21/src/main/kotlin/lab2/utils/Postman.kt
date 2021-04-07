package lab2.utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import javax.mail.Message
import javax.mail.internet.InternetAddress

@Component object Postman {

    @Value("\${spring.mail.username}") private lateinit var from: String
    @Autowired private lateinit var sender: JavaMailSender

    operator fun invoke(to: String, subject: String, body: String) =
        sender.send {
            it.setRecipient(Message.RecipientType.TO, InternetAddress(to))
            it.setFrom(from)
            it.subject = subject
            it.setText(body)
    }
}
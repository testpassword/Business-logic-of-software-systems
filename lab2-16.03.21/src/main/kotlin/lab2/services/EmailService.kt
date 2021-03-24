package lab2.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import javax.mail.Message
import javax.mail.internet.InternetAddress

@Component class EmailService {

    @Value("\${spring.mail.username}") private lateinit var from: String
    @Autowired private lateinit var sender: JavaMailSender

    fun send(to: String, subject: String, body: String) =
        sender.send {
            it.setRecipient(Message.RecipientType.TO, InternetAddress(to))
            it.setFrom(from)
            it.subject = subject
            it.setText(body)
    }
}
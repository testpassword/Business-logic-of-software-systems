package testpassword.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;

//https://coderoad.ru/43142137/%D0%9E%D1%82%D0%BF%D1%80%D0%B0%D0%B2%D0%BA%D0%B0-email-%D0%B8%D0%B7-Spring-Boot-%D0%BF%D1%80%D0%B8%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D1%8F-%D1%81-%D0%B8%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5%D0%BC-Gmail
@Component public class EmailService {

    @Value("${spring.mail.username}") private String from; //https://stackoverflow.com/questions/30528255/how-to-access-a-value-defined-in-the-application-properties-file-in-spring-boot
    private final JavaMailSender sender;

    @Autowired public EmailService(JavaMailSender sender) { this.sender = sender; }

    public void send(String to, String subject, String body) {
        this.sender.send(msg -> {
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setFrom(from);
            msg.setSubject(subject);
            msg.setText(body);
        });
    }
}

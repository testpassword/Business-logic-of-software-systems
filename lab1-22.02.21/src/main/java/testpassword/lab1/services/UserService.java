package testpassword.lab1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import testpassword.lab1.models.User;
import testpassword.lab1.repos.UserRepo;
import javax.transaction.Transactional;

@Service public class UserService {

    private final UserRepo userRepo;
    private final EmailService postman;
    private final BCryptPasswordEncoder encoder;

    @Autowired public UserService(UserRepo userRepo, EmailService postman, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.postman = postman;
        this.encoder = encoder;
    }

    @Transactional public User add(String email, String password, String name) {
        var u = new User(email, encoder.encode(password), name, true);
        try {
            postman.send(email, "Register", "yeah!");
        } catch (MailSendException e) {
            e.printStackTrace();
        }
        return u;
    }

    @Transactional public void delete(String email, String password) {
        userRepo.delete(new User(email, password));
        try {
            postman.send(email, "Goodbye", ":(");
        } catch (MailSendException e) {
            e.printStackTrace();
        }
    }

    @Transactional public User get(String email) { return userRepo.getByEmail(email); }

    @Transactional public boolean isExist(String email) { return userRepo.existsByEmail(email); }

    @Transactional public void save(User u) { this.userRepo.save(u); }
}
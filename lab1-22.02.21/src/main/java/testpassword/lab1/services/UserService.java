package testpassword.lab1.services;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import testpassword.lab1.models.User;
import testpassword.lab1.repos.UserRepo;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service public class UserService {

    private final UserRepo repo;
    private final EmailService postman;
    private final BCryptPasswordEncoder encoder;

    @Autowired public UserService(UserRepo userRepo, EmailService postman, BCryptPasswordEncoder encoder) {
        this.repo = userRepo;
        this.postman = postman;
        this.encoder = encoder;
    }

    @Transactional public Optional<User> add(String email, String password, String name) {
        val u = new User(email, encoder.encode(password), name);
        this.save(u);
        try {
            postman.send(email, "Register", "yeah!");
        } catch (MailSendException e) { System.out.println("[ERROR] " + e.getLocalizedMessage()); }
        return this.get(email); //загружаем из базы данных, чтобы получить сущность уже с присвоенным id
    }

    @Transactional public void delete(String email) {
        repo.delete(this.repo.getByEmail(email));
        try {
            postman.send(email, "Goodbye", ":(");
        } catch (MailSendException e) { System.out.println("[ERROR] " + e.getLocalizedMessage()); }
    }

    public boolean restorePassword(String email) {
        try {
            postman.send(email, "Your password", this.get(email).get().getPassword());
            return true;
        } catch (MailSendException e) {
            System.out.println("[ERROR] " + e.getLocalizedMessage());
            return false;
        }
    }

    public List<User> getAll() { return StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList()); }

    public Optional<User> get(String email) { return Optional.ofNullable(repo.getByEmail(email)); }

    public Optional<User> get(long id) { return Optional.ofNullable(repo.getByUserId(id)); }

    public boolean exist(String email) { return repo.existsByEmail(email); }

    @Transactional public void save(User u) { this.repo.save(u); }
}
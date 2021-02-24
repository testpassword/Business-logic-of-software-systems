package testpassword.lab1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component public class ApplicationBeansProvider {

    @Bean public BCryptPasswordEncoder newPasswordEncoder() { return new BCryptPasswordEncoder(); }
}
package testpassword.lab1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import testpassword.lab1.security.JWTUtil;

@Configuration public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private JWTUtil jwtUtil;

    @Bean @Override public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and().authorizeRequests()
                .antMatchers("/user").permitAll()
                .antMatchers("/advert").permitAll()
                .anyRequest().authenticated()
                .and().apply(new JWTConfig(jwtUtil));
    }
}
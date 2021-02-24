package testpassword.lab1.config;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import testpassword.lab1.filters.JWTFilter;
import testpassword.lab1.security.JWTUtil;

public class JWTConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JWTUtil jwtUtil;

    public JWTConfig(JWTUtil jwtUtil) { this.jwtUtil = jwtUtil; }

    @Override public void configure(HttpSecurity builder) {
        builder.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
    }
}
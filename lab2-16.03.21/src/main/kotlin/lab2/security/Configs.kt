package lab2

import lab2.security.JWTFilter
import lab2.security.JWTTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component

@Component class ApplicationBeansProvider {
    @Bean fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}

@Configuration class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired private lateinit var jwtUtil: JWTTokenUtil

    @Bean override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    override fun configure(http: HttpSecurity) {
        http.httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .and().authorizeRequests()
            .antMatchers("/user/**").permitAll()
            .antMatchers("/advert/**").permitAll()
            .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
            .anyRequest().authenticated()
            .and().apply(object: SecurityConfigurerAdapter<DefaultSecurityFilterChain?, HttpSecurity>() {
                override fun configure(builder: HttpSecurity) {
                    builder.addFilterBefore(JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
                }
            })
    }
}
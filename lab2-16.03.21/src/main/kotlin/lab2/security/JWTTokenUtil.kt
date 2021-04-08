package lab2.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import lab2.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import javax.servlet.http.HttpServletRequest

@Component class JWTTokenUtil {

    @Value("\${jwt.key}") private val KEY: String = ""
    @Value("\${jwt.validity}") private val VALIDITY: Long = 0
    @Autowired private lateinit var userDetails: UserService

    fun generate(username: String, roles: List<String>) =
        Jwts.claims().setSubject(username).let {
            it["roles"] = roles
            val now = Date()
            Jwts.builder().setClaims(it).setIssuedAt(now).setExpiration(Date(now.time + VALIDITY))
                .signWith(SignatureAlgorithm.HS512, KEY).compact()
        }

    infix fun validate(token: String) = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).body.expiration.before(Date())

    // TODO: устранить баг
    infix fun isExpired(token: String) = getExpirationDate(token).before(Date())

    infix fun resolve(req: HttpServletRequest) =
        req.getHeader("Authorization")?.let {
            if (it.startsWith("Bearer ")/* && isExpired(it).not()*/) it.substring(7) else null
        }

    infix fun getAuthentication(token: String) =
        userDetails
            .loadUserByUsername(getUsername(token))
            .let { UsernamePasswordAuthenticationToken(it, "", it.authorities) }

    infix fun getUsername(token: String) = getClaim(token) { it.subject }

    infix fun getExpirationDate(token: String) = getClaim(token) { it.expiration }

    fun <T> getClaim(token: String, claimsResolver: Function<Claims, T>): T =
        claimsResolver.apply(Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).body)

    infix fun decode(req: HttpServletRequest) =
        try {
            getUsername(resolve(req)!!)
        } catch (e: NullPointerException) {
            throw JwtException("bad token")
        }

    infix fun getRoles(token: String) = getClaim(token) { it["roles"] } as List<String>
}
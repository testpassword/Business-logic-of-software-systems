package lab2.models

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import javax.validation.constraints.Email

@Entity @Table(name="users") @JsonSerialize(using = UserSerializer::class)
class User: UserDetails {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_gen")
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "users_id_seq")
    @Id var userId: Long = 0
    var email: @Email String = ""
    private var password: String = ""
    var name: String = ""
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER) val adverts: List<Advert> = emptyList()

    constructor()

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("User"))

    override fun getPassword(): String = password

    fun setPassword(password: String) { this.password = password }

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}

class UserSerializer(t: Class<User>): StdSerializer<User>(t) {

    override fun serialize(u: User, gen: JsonGenerator, provider: SerializerProvider) {
        gen.apply {
            writeStartObject()
            writeNumberField("userId", u.userId)
            writeFieldName("adverts")
            writeStartArray()
            u.adverts.forEach { writeNumber(it.advertId) }
            writeEndArray()
            writeEndObject()
        }
    }
}
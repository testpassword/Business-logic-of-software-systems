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
    @Enumerated(EnumType.STRING) var status: STATUS = STATUS.ACTIVE
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER) val adverts: List<Advert> = emptyList()

    constructor()

    constructor(email: String, password: String) {
        this.email = email
        this.password = password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("USER"))

    override fun getPassword(): String = password

    infix fun setPassword(password: String) { this.password = password }

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = status != STATUS.BANNED

    override fun isAccountNonLocked(): Boolean = status != STATUS.LOCKED

    override fun isCredentialsNonExpired(): Boolean = isAccountNonLocked

    override fun isEnabled(): Boolean = status != STATUS.DELETED

    enum class STATUS { LOCKED, BANNED, ACTIVE, DELETED }
}


class UserSerializer(t: Class<User>? = null): StdSerializer<User>(t) {

    override fun serialize(u: User, gen: JsonGenerator, provider: SerializerProvider) =
        with(gen) {
            writeStartObject()
            writeNumberField("userId", u.userId)
            writeFieldName("adverts")
            writeStartArray()
            u.adverts.map(Advert::advertId).forEach { writeNumber(it) }
            writeEndArray()
            writeEndObject()
        }
}
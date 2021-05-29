package lab3.models

import java.util.*
import javax.persistence.*

@Entity @Table(name = "adverts")
class Advert {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advert_seq_gen")
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "advert_id_seq")
    @Id var advertId: Long = 0
    @ManyToOne(optional = false, cascade = [CascadeType.ALL]) @JoinColumn(name = "user_id") lateinit var user: User
    var cost: Int = 0
    @Enumerated(EnumType.STRING) var typeOfAdvert: TYPE_OF_ADVERT = TYPE_OF_ADVERT.SALE
    @Enumerated(EnumType.STRING) var typeOfEstate: TYPE_OF_ESTATE = TYPE_OF_ESTATE.FLAT
    var location: String = ""
    var quantityOfRooms: Int = 1
    var area: Int = 1
    var floor: Int = 1
    var description: String = ""
    var name: String = ""
    var mobileNumber: String = ""
    var isRealtor: Boolean = false
    var image: String = "" // base64 string
    @Enumerated(EnumType.STRING) var status: STATUS = STATUS.ON_MODERATION
    var publishDate: Date = Date(0)
    var archived: Boolean = false

    enum class TYPE_OF_ADVERT { SALE, RENT }

    enum class TYPE_OF_ESTATE { FLAT, NEWFLAT, ROOM, HOUSE, COTTAGE, DACHA, TOWNHOUSE, LAND }

    enum class STATUS { APPROVED, ON_MODERATION, DECLINED, CLOSED }
}
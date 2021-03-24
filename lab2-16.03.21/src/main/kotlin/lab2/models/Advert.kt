package lab2.models

import javax.persistence.*

class Advert {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advert_seq_gen")
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "advert_id_seq")
    @Id var advertId: Long = 0
    @ManyToOne(optional = false, cascade = [CascadeType.ALL]) @JoinColumn(name = "user_id") lateinit var user: User
    var cost: Int = 0
    @Enumerated(EnumType.STRING) lateinit var typeOfAdvert: TYPE_OF_ADVERT
    @Enumerated(EnumType.STRING) lateinit var typeOfEstate: TYPE_OF_ESTATE
    var location: String = ""
    var quantityOfRooms: Int = 0
    var area: Int = 0
    var floor: Int = 0
    var description: String = ""
    var name: String = ""
    var mobileNumber: String = ""
    var isRealtor: Boolean = false
    var image: String = ""

    enum class TYPE_OF_ADVERT { SALE, RENT }

    enum class TYPE_OF_ESTATE { FLAT, NEWFLAT, ROOM, HOUSE, COTTAGE, DACHA, TOWNHOUSE, LAND }
}
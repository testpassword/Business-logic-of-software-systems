package testpassword.lab1.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.Nullable;
import javax.persistence.*;

//https://www.baeldung.com/spring-data-rest-relationships
@Data @Entity @Table(name = "adverts")
public class Advert {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "advert_seq_gen")
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "advert_id_seq")
    @Id private long advertId;
    @ManyToOne(optional = false, cascade = CascadeType.ALL) @JoinColumn(name = "user_id") private User user;
    private int cost;
    @Enumerated(EnumType.STRING) private TYPE_OF_ADVERT type_of_advert;
    @Enumerated(EnumType.STRING) private TYPE_OF_ESTATE type_of_estate;
    @Nullable private String location;
    private int quantityOfRooms;
    private int area;
    private int floor;
    private String description;
    @Nullable private String name;
    @Nullable private String mobileNumber;
    private boolean isRealtor;
    private String image;

    public Advert() {}

    public Advert(User u, int cost, TYPE_OF_ADVERT type_of_advert, TYPE_OF_ESTATE type_of_estate, String location,
                  int quantityOfRooms, int area, int floor, String description, String name, String mobileNumber,
                  boolean isRealtor, String image) {
        this.user = u;
        this.cost = cost;
        this.type_of_advert = type_of_advert;
        this.type_of_estate = type_of_estate;
        this.location = location;
        this.quantityOfRooms = quantityOfRooms;
        this.area = area;
        this.floor = floor;
        this.description = description;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.isRealtor = isRealtor;
        this.image = image;
    }

    public enum TYPE_OF_ADVERT { SALE, RENT }

    public enum TYPE_OF_ESTATE { FLAT, NEWFLAT, ROOM, HOUSE, COTTAGE, DACHA, TOWNHOUSE, LAND }
}
package testpassword.lab1.requests;

import lombok.*;
import testpassword.lab1.models.Advert;
import java.io.Serializable;

@Data @Builder @Value
public class AdvertReq implements Serializable {

    public static final long serialVersionUID = 4L;
    public int[] advertsIds;
    public long userId;
    public int cost;
    public Advert.TYPE_OF_ADVERT type_of_advert;
    public Advert.TYPE_OF_ESTATE type_of_estate;
    public String location;
    public int quantityOfRooms;
    public int area;
    public int floor;
    public String description;
    public String name;
    public String mobileNumber;
    public boolean isRealtor;
    public String image;

    public Advert toAdvert() {
        return new Advert(null, cost, type_of_advert, type_of_estate, location, quantityOfRooms, area, floor,
                description, name, mobileNumber, isRealtor, image);
    }
}
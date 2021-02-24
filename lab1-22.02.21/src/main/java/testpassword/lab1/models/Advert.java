package testpassword.lab1.models;

import lombok.Data;
import org.jetbrains.annotations.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

//https://www.baeldung.com/spring-data-rest-relationships
@Data @Entity public class Advert {

    @Id @GeneratedValue private long advertId;
    @ManyToOne private User user;
    @Nullable private int cost;
    private TYPE_OF_ADVERT type_of_advert;
    private TYPE_OF_ESTATE type_of_estate;
    @Nullable private String location;
    @Nullable private int quantityOfRooms;
    private int area;
    @Nullable private int floor;
    private String description;
    private String name;
    private String mobileNumber;
    private boolean isRealtor;
    private String image;

    public Advert() {}

    public User getUser() { return user; }

    public int getCost() { return cost; }

    public TYPE_OF_ADVERT getType_of_advert() { return type_of_advert; }

    public TYPE_OF_ESTATE getType_of_estate() { return type_of_estate; }

    public String getLocation() { return location; }

    public int getQuantityOfRooms() { return quantityOfRooms; }

    public int getArea() { return area; }

    public int getFloor() { return floor; }

    public String getDescription() { return description; }

    public String getName() { return name; }

    public String getMobileNumber() { return mobileNumber; }

    public boolean isRealtor() { return isRealtor; }

    public String getImage() { return image; }

    public static Builder newBuilder() { return new Advert().new Builder(); }

    public class Builder {

        private Builder() {}

        public Builder setUserId(User user) {
            Advert.this.user = user;
            return this;
        }

        public Builder setCost(int cost) {
            Advert.this.cost = cost;
            return this;
        }

        public Builder setType_of_advert(TYPE_OF_ADVERT type_of_advert) {
            Advert.this.type_of_advert = type_of_advert;
            return this;
        }

        public Builder setType_of_estate(TYPE_OF_ESTATE type_of_estate) {
            Advert.this.type_of_estate = type_of_estate;
            return this;
        }

        public Builder setLocation(String location) {
            Advert.this.location = location;
            return this;
        }

        public Builder setQuantityOfRooms(int quantityOfRooms) {
            Advert.this.quantityOfRooms = quantityOfRooms;
            return this;
        }

        public Builder setArea(int area) {
            Advert.this.area = area;
            return this;
        }

        public Builder setFloor(int floor) {
            Advert.this.floor = floor;
            return this;
        }

        public Builder setDescription(String description) {
            Advert.this.description = description;
            return this;
        }

        public Builder setName(String name) {
            Advert.this.name = name;
            return this;
        }

        public Builder setMobileNumber(String mobileNumber) {
            Advert.this.mobileNumber = mobileNumber;
            return this;
        }

        public Builder setRealtor(boolean realtor) {
            Advert.this.isRealtor = realtor;
            return this;
        }

        public Builder setImage(String image) {
            Advert.this.image = image;
            return this;
        }
    }
}

enum TYPE_OF_ADVERT { SALE, RENT }

enum TYPE_OF_ESTATE { FLAT, NEWFLAT, ROOM, HOUSE, COTTAGE, DACHA, TOWNHOUSE, LAND }
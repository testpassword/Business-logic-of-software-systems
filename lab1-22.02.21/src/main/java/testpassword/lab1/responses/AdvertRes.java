package testpassword.lab1.responses;

import lombok.*;
import testpassword.lab1.models.Advert;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Builder @Data @Value
public class AdvertRes implements Serializable {

    public static final long serialVersionUID = 4L;
    @Builder.Default public Set<Advert> adverts = new HashSet<>();
    @Builder.Default public String msg = "";
}

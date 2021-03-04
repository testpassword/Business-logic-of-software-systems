package testpassword.lab1.responses;

import lombok.*;
import testpassword.lab1.models.Advert;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Builder @Data @Value
public class AdvertRes implements Serializable {

    public static final long serialVersionUID = 4L;
    @Builder.Default public List<Advert> adverts = Collections.emptyList();
    @Builder.Default public String msg = "";
}

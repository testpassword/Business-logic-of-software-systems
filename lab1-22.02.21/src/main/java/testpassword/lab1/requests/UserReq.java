package testpassword.lab1.requests;

import lombok.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Data @Builder @Value
public class UserReq implements Serializable {

    public static final long serialVersionUID = 4L;
    @Email public String email;
    public String password;
    public String name;
}
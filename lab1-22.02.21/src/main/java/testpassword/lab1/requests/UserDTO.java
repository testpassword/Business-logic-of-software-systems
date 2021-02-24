package testpassword.lab1.requests;

import lombok.Data;
import javax.validation.constraints.Email;
import java.io.Serializable;

public class UserDTO implements Serializable {

    private static final long serialVersionUID = 4L;
    @Email public String email;
    public String password;
    public String name;
}
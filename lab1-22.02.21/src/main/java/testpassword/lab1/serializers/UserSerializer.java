package testpassword.lab1.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import testpassword.lab1.models.Advert;
import testpassword.lab1.models.User;
import java.io.IOException;

//https://www.baeldung.com/jackson-custom-serialization
public class UserSerializer extends StdSerializer<User> {

    //НЕ УДАЛЯТЬ! (нужен при сериализации)
    public UserSerializer() { this(null); }

    public UserSerializer(Class<User> t) { super(t); }

    @Override public void serialize(User user, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("userId", user.getUserId());
        jgen.writeFieldName("adverts");
        jgen.writeStartArray();
        for (Advert a: user.getAdverts()) jgen.writeNumber(a.getAdvertId());
        jgen.writeEndArray();
        jgen.writeEndObject();
    }
}

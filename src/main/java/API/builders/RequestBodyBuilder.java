package API.builders;

import API.models.Register;
import API.models.UserTime;

public class RequestBodyBuilder {

    public static Register buildValidRegisterBody() {
        return new Register("eve.holt@reqres.in", "pistol");
    }

    public static Register buildInvalidRegisterBody() {
        return new Register("sydney@fife", "");
    }

    public static UserTime buildUserTimeBody() {
        return new UserTime("morpheus", "zion resident");
    }
}

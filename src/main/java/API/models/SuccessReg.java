package API.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessReg {
    private final Integer id;
    private final String token;

    @JsonCreator
    public SuccessReg(
            @JsonProperty("id") Integer id,
            @JsonProperty("token") String token) {
        this.id = id;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}

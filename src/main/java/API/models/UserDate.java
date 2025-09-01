package API.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDate {
    private final Integer id;
    private final String email;
    private final String first_name;
    private final String last_name;
    private final String avatar;

    @JsonCreator
    public UserDate(
            @JsonProperty("id") Integer id,
            @JsonProperty("email") String email,
            @JsonProperty("first_name") String first_name,
            @JsonProperty("last_name") String last_name,
            @JsonProperty("avatar") String avatar) {
        this.id = id;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getAvatar() {
        return avatar;
    }
}

package API.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTimeResponse extends UserTime {
    private final String updatedAt;

    @JsonCreator
    public UserTimeResponse(
            @JsonProperty("name") String name,
            @JsonProperty("job") String job,
            @JsonProperty("updatedAt") String updatedAt) {
        super(name, job);
        this.updatedAt = updatedAt;
    }

    public String getUpdateAt() {
        return updatedAt;
    }
}

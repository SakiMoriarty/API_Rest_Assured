package API.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class invalidRegister {
    private String error;

    @JsonCreator
    public invalidRegister(
            @JsonProperty("error") String error){
        this.error = error;
    }

    public String getError() {
        return error;
    }
}

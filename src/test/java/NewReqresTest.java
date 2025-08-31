import API.Specifications;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class NewReqresTest {
    private final static String URL = "https://reqres.in";
    private final static String ApiHeader = "x-api-key";
    private final static String ApiKey = "reqres-free-v1";
    private final String Register_Endpoint = "/api/register";
    private final String UsersCheck_Endpoint = "/api/users?page=2";

    @Test
    @DisplayName("Avatar Check")
    public void checkAvatarTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());

        Response response = given()
                .header(ApiHeader, ApiKey)
                .when()
                .get(UsersCheck_Endpoint)
                .then()
                .body("page", equalTo(2))
                .body("data.id", notNullValue())
                .body("data.email", notNullValue())
                .body("data.first_name", notNullValue())
                .body("data.last_name", notNullValue())
                .body("data.avatar", notNullValue())
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        List<String> emails = jsonPath.get("data.email");
        List<Integer> id = jsonPath.get("data.id");
        List<String> avatars = jsonPath.get("data.avatar");

        for(int i = 0; i < avatars.size(); i++){
            assertTrue(avatars.get(i).contains(id.get(i).toString()));
        }

        assertTrue(emails.stream().allMatch(x->x.endsWith("@reqres.in")));
    }

    @Test
    @DisplayName("Success Registration")
    public void successRegTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        Map<String, String> user = new HashMap<>();
        user.put("email", "eve.holt@reqres.in");
        user.put("password", "pistol");
        Response response = given()
                .header(ApiHeader, ApiKey)
                .body(user)
                .when()
                .post(Register_Endpoint)
                .then()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        int id = jsonPath.get("id");
        String token = jsonPath.get("token");
        assertTrue(id > 0);
        assertNotNull(token);
    }

    @Test
    @DisplayName("Invalid Registration")
    public void invalidRegUser(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec400());
        Map<String, String> user = new HashMap<>();
        user.put("email", "sydney@fife");
        Response response = given()
                .header(ApiHeader, ApiKey)
                .body(user)
                .when()
                .post(Register_Endpoint)
                .then()
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        String error = jsonPath.get("error");
        assertEquals("Missing password", error);
    }
}

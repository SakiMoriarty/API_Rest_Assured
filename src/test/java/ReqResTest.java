
import API.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ReqResTest {
    private final static String URL = "https://reqres.in";
    private final static String ApiHeader = "x-api-key";
    private final static String ApiKey = "reqres-free-v1";
    private final String register = "/api/register";
    private final String usersCheck = "/api/users?page=2";
    private final String years = "/api/unknown";
    private final String userDelete = "/api/users/2";
    private final String time = "/api/users/2";

    @Test
    @DisplayName("Avatar Check")
    public void checkAvatarAndIdTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());

        List<UserDate> users = given()
                .header(ApiHeader, ApiKey)
                .when()
                .get(usersCheck)
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserDate.class);
        users.forEach(x -> assertTrue(x.getAvatar().contains(x.getId().toString())));
        assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));

        List<String> avatars = users.stream().map(UserDate::getAvatar).toList();
        List<String> ids = users.stream().map(x->x.getId().toString()).toList();
    }

    @Test
    @DisplayName("Success Registration")
    public void successRegTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());

        Register user = new Register("eve.holt@reqres.in", "pistol");

        SuccessReg successReg = given()
                .header(ApiHeader, ApiKey)
                .body(user)
                .when()
                .post(register)
                .then().log().all()
                .extract().as(SuccessReg.class);
        assertNotNull(successReg.getId());
        assertNotNull(successReg.getToken());

        assertTrue(successReg.getId() > 0);
        assertNotNull(successReg.getToken());
    }

    @Test
    @DisplayName("Invalid Registration")
    public void invalidRegUser(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec400());

        Register user = new Register("sydney@fife", "");
        invalidRegister invalidRegister = given()
                .header(ApiHeader, ApiKey)
                .body(user)
                .post(register)
                .then().log().all()
                .extract().as(invalidRegister.class);
        assertEquals("Missing password", invalidRegister.getError());
    }

    @Test
    @DisplayName("Sorting Years")
    public void sortedYears(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());
        List<ColorsData> colors = given()
                .header(ApiHeader, ApiKey)
                .when()
                .get(years)
                .then()
                .extract().body().jsonPath().getList("data", ColorsData.class);
        List<Integer> years = colors.stream().map(ColorsData::getYear).toList();
        List<Integer> sortedYears = years.stream().sorted().toList();

        assertEquals(sortedYears, years);
        System.out.println(sortedYears);
    }

    @Test
    @DisplayName("Deleting user")
    public void deleteUserTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecUnique(204));
        given()
                .header(ApiHeader, ApiKey)
                .when()
                .delete(userDelete);
    }

    @Test
    @DisplayName("Check Time")
    public void timeTest(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpec200());

        UserTime user = new UserTime("morpheus", "zion resident");
        UserTimeResponse response = given()
                .header(ApiHeader, ApiKey)
                .body(user)
                .when()
                .put(time)
                .then()
                .extract().as(UserTimeResponse.class);

        Instant currentTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant responseTime = Instant.parse(response.getUpdateAt()).truncatedTo(ChronoUnit.SECONDS);
        assertTrue(Math.abs(currentTime.getEpochSecond() - responseTime.getEpochSecond()) <= 1);
        System.out.println("Current Time: " + currentTime);
        System.out.println("Response Time: " + responseTime);
    }
}



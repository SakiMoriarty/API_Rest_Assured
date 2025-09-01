
import API.*;
import static org.junit.jupiter.api.Assertions.*;

import API.models.*;
import API.ReqresEndpoints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ReqResTest {

    @Test
    @DisplayName("Avatar Check")
    public void checkAvatarAndIdTest(){
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpec200());

        List<UserDate> users = given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .when()
                .get(ReqresEndpoints.Users_Page_2_Endpoint)
                .then().log().all()
                .extract().body().jsonPath().getList("data", UserDate.class);
        users.forEach(x -> assertTrue(x.getAvatar().contains(x.getId().toString())));
        assertTrue(users.stream().allMatch(x->x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    @DisplayName("Success Registration")
    public void successRegTest(){
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpec200());

        Register user = new Register("eve.holt@reqres.in", "pistol");

        SuccessReg successReg = given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .body(user)
                .when()
                .post(ReqresEndpoints.Register_Endpoint)
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
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpec400());

        Register user = new Register("sydney@fife", "");
        invalidRegister invalidRegister = given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .body(user)
                .post(ReqresEndpoints.Register_Endpoint)
                .then().log().all()
                .extract().as(invalidRegister.class);
        assertEquals("Missing password", invalidRegister.getError());
    }

    @Test
    @DisplayName("Sorting Years")
    public void sortedYears(){
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpec200());
        List<ColorsData> colors = given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .when()
                .get(ReqresEndpoints.years)
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
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpecUnique(204));
        given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .when()
                .delete(ReqresEndpoints.userDelete);
    }

    @Test
    @DisplayName("Check Time")
    public void timeTest(){
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpec200());

        UserTime user = new UserTime("morpheus", "zion resident");
        UserTimeResponse response = given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .body(user)
                .when()
                .put(ReqresEndpoints.time)
                .then()
                .extract().as(UserTimeResponse.class);

        Instant currentTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant responseTime = Instant.parse(response.getUpdateAt()).truncatedTo(ChronoUnit.SECONDS);
        assertTrue(Math.abs(currentTime.getEpochSecond() - responseTime.getEpochSecond()) <= 1);
        System.out.println("Current Time: " + currentTime);
        System.out.println("Response Time: " + responseTime);
    }
}



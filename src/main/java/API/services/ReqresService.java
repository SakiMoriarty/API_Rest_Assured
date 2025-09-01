package API.services;

import API.ReqresEndpoints;
import API.models.*;
import API.Specifications;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ReqresService {

    public List<UserDate> getUsersPage2() {
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpec200());
        return given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .get(ReqresEndpoints.Users_Page_2_Endpoint)
                .then()
                .extract().body().jsonPath().getList("data", UserDate.class);
    }

    public SuccessReg registerUser(Register user) {
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpec200());
        return given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .body(user)
                .post(ReqresEndpoints.Register_Endpoint)
                .then()
                .extract().as(SuccessReg.class);
    }

    public invalidRegister registerInvalidUser(Register user) {
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpec400());
        return given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .body(user)
                .post(ReqresEndpoints.Register_Endpoint)
                .then()
                .extract().as(invalidRegister.class);
    }

    public List<ColorsData> getColors() {
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpec200());
        return given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .get(ReqresEndpoints.years)
                .then()
                .extract().body().jsonPath().getList("data", ColorsData.class);
    }

    public void deleteUser() {
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpecUnique(204));
        given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .delete(ReqresEndpoints.userDelete);
    }

    public UserTimeResponse updateUser(UserTime user) {
        Specifications.installSpecification(Specifications.requestSpec(ReqresEndpoints.URL), Specifications.responseSpec200());
        return given()
                .header(ReqresEndpoints.ApiHeader, ReqresEndpoints.ApiKey)
                .body(user)
                .put(ReqresEndpoints.time)
                .then()
                .extract().as(UserTimeResponse.class);
    }
}

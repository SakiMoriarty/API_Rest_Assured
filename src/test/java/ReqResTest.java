import API.builders.RequestBodyBuilder;
import API.models.*;
import API.services.ReqresService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReqResTest {

    ReqresService service = new ReqresService();

    @Test
    @DisplayName("Avatar Check")
    public void checkAvatarAndIdTest() {
        List<UserDate> users = service.getUsersPage2();
        users.forEach(x -> assertTrue(x.getAvatar().contains(x.getId().toString())));
        assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@reqres.in")));
    }

    @Test
    @DisplayName("Success Registration")
    public void successRegTest() {
        Register user = RequestBodyBuilder.buildValidRegisterBody();
        SuccessReg successReg = service.registerUser(user);
        assertNotNull(successReg.getId());
        assertNotNull(successReg.getToken());
        assertTrue(successReg.getId() > 0);
    }

    @Test
    @DisplayName("Invalid Registration")
    public void invalidRegUser() {
        Register user = RequestBodyBuilder.buildInvalidRegisterBody();
        invalidRegister response = service.registerInvalidUser(user);
        assertEquals("Missing password", response.getError());
    }

    @Test
    @DisplayName("Sorting Years")
    public void sortedYears() {
        List<ColorsData> colors = service.getColors();
        List<Integer> years = colors.stream().map(ColorsData::getYear).toList();
        List<Integer> sortedYears = years.stream().sorted().toList();
        assertEquals(sortedYears, years);
    }

    @Test
    @DisplayName("Deleting user")
    public void deleteUserTest() {
        service.deleteUser();
    }

    @Test
    @DisplayName("Check Time")
    public void timeTest() {
        UserTime user = RequestBodyBuilder.buildUserTimeBody();
        UserTimeResponse response = service.updateUser(user);

        Instant currentTime = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant responseTime = Instant.parse(response.getUpdateAt()).truncatedTo(ChronoUnit.SECONDS);
        assertTrue(Math.abs(currentTime.getEpochSecond() - responseTime.getEpochSecond()) <= 1);
    }
}

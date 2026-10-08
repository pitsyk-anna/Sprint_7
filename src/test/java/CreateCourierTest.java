import data.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.CourierLoginModel;
import model.CourierModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;
import static org.hamcrest.Matchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class CreateCourierTest  extends BaseApiTest {
    private CourierSteps courierSteps;
    private String login;
    private String password;
    private String firstName;
    private CourierModel courier;
    private CourierLoginModel loginModel;

    @Before
    public void createTest() {
        courierSteps = new CourierSteps();
        login = CourierData.getUniqueLogin();
        password = CourierData.getPassword();
        firstName = CourierData.getFirstName();
        courier = new CourierModel(login, password, firstName);
        loginModel = new CourierLoginModel(login, password);
    }

    @After
    public void cleanUp() {
        Response loginResponse = courierSteps.loginCourier(loginModel);

        if (loginResponse.statusCode() == SC_OK) {
            int courierId = loginResponse.jsonPath().getInt("id");
            courierSteps.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка, что курьера можно создать")
    public void createNewCourier() {
        Response response = courierSteps.createCourier(courier);
        response.then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Проверка, что нельзя создать курьеров с одинаковыми логинами")
    public void createTwoIdenticalLogin() {
        courierSteps.createCourier(courier)
                .then()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        courierSteps.createCourier(courier)
                .then()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка, что нельзя создать без логина")
    public void createNotLogin() {
        CourierModel courier = new CourierModel("", password, firstName);

                courierSteps.createCourier(courier)
                        .then()
                        .statusCode(SC_BAD_REQUEST)
                        .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка, что нельзя создать курьера без пароля")
    public void createNotPassword() {
        CourierModel courier = new CourierModel(login, "", firstName);

                courierSteps.createCourier(courier)
                        .then()
                        .statusCode(SC_BAD_REQUEST)
                        .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}

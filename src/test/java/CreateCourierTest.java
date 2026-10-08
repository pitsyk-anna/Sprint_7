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

public class CreateCourierTest  extends BaseApiTest {
    private CourierSteps courierSteps;
    private String login;
    private String password;
    private String firstName;
    private Integer courierId = null;

    @Before
    public void createTest() {
        courierSteps = new CourierSteps();
        login = CourierData.getUniqueLogin();
        password = CourierData.getPassword();
        firstName = CourierData.getFirstName();
        courierId = null;
    }

    @After
    public void cleanUp() {
        if (courierId != null) {
            courierSteps.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверка, что курьера можно создать")
    public void createNewCourier() {

        CourierModel courier = new CourierModel(login, password, firstName);
        CourierLoginModel loginModel = new CourierLoginModel(login, password);

        Response response = courierSteps.createCourier(courier);
        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
        courierId = courierSteps
                .loginCourier(loginModel)
                .jsonPath()
                .getInt("id");
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Проверка, что нельзя создать курьеров с одинаковыми логинами")
    public void createTwoIdenticalLogin() {
        CourierModel courier = new CourierModel(login, password, firstName);
        CourierLoginModel loginModel = new CourierLoginModel(login,password);

        courierSteps.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierSteps.createCourier(courier)
                .then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        courierId = courierSteps.loginCourier(loginModel)
                .jsonPath()
                .getInt("id");
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка, что нельзя создать без логина")
    public void createNotLogin() {
        CourierModel courier = new CourierModel("", password, firstName);

                courierSteps.createCourier(courier)
                        .then()
                        .statusCode(400)
                        .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка, что нельзя создать курьера без пароля")
    public void createNotPassword() {
        CourierModel courier = new CourierModel(login, "", firstName);

                courierSteps.createCourier(courier)
                        .then()
                        .statusCode(400)
                        .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}

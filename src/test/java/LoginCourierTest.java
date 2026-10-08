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

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginCourierTest extends BaseApiTest {
    private CourierSteps courierSteps;
    private String login;
    private String password;
    private String firstName;
    private Integer courierId = null;

    @Before
    public void loginTest() {
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
    @DisplayName("Залогин курьера")
    @Description("Проверка, что курьер может залогиниться с валидными данными")
    public void loginCourier() {
        CourierModel courier = new CourierModel(login, password, firstName);
        CourierLoginModel loginModel = new CourierLoginModel(login, password);
        // создаём курьера
        courierSteps.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        // получаем id
        courierId = courierSteps
                .loginCourier(loginModel)
                .jsonPath()
                .getInt("id");
        Response response = courierSteps.loginCourier(loginModel);
        int id = response.jsonPath().getInt("id");
        response.then()
                .statusCode(200)
                .body("id", equalTo(id));
    }


    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверка невозможности авторизации без логина")
    public void loginCourierNoLogin() {
        CourierModel courier = new CourierModel(login, password, firstName);
        CourierLoginModel loginModel = new CourierLoginModel(login, password);

        courierSteps.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierSteps.loginCourier(loginModel)
                .jsonPath()
                .getInt("id");

        Response response = courierSteps.loginCourier(new CourierLoginModel("", password));
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Проверка невозможности авторизации без пароля")
    public void loginCourierNoPassword() {
        CourierModel courier = new CourierModel(login, password, firstName);
        CourierLoginModel loginModel = new CourierLoginModel(login, password);

        courierSteps.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierSteps.loginCourier(loginModel)
                .jsonPath()
                .getInt("id");

        Response response = courierSteps.loginCourier(new CourierLoginModel(login, ""));
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверка невозможности авторизации с неверным логином")
    public void loginCourierIncorrectLogin() {
        CourierModel courier = new CourierModel(login, password, firstName);
        CourierLoginModel loginModel = new CourierLoginModel(login, password);

        courierSteps.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierSteps.loginCourier(loginModel)
                .jsonPath()
                .getInt("id");

        Response response = courierSteps.loginCourier(new CourierLoginModel("incorrect_" + login, password));
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка невозможности авторизации с неверным паролем")
    public void loginCourierIncorrectPassword() {
        CourierModel courier = new CourierModel(login, password, firstName);
        CourierLoginModel loginModel = new CourierLoginModel(login, password);

        courierSteps.createCourier(courier)
                .then()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierSteps.loginCourier(loginModel)
                .jsonPath()
                .getInt("id");

        Response response = courierSteps.loginCourier(new CourierLoginModel(login, "incorrect_password"));
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Авторизация несуществующего юзера")
    @Description("Проверка невозможности авторизации несуществующим юзером")
    public void loginCourierNotExists() {
        String fakeLogin = CourierData.getUniqueLogin();
        String fakePassword = CourierData.getPassword();
        Response response = courierSteps.loginCourier(new CourierLoginModel(fakeLogin, fakePassword));
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}

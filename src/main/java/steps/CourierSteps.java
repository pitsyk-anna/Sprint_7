package steps;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import model.CourierLoginModel;
import model.CourierModel;

import static data.ApiEndpoints.*;
import static io.restassured.RestAssured.given;

public class CourierSteps {


    @Step("Создание нового курьера")
     public Response createCourier(CourierModel courier){
         return given()
                 .log().all()
                 .contentType(ContentType.JSON)
                 .body(courier)
                 .when()
                 .post(COURIER_CREATE_PATH)
                 .then()
                 .log().all()
                 .extract().response();
     }

    @Step("Авторизация курьера")
    public Response loginCourier(CourierLoginModel courierLoginModel) {
         return given()
                 .log().all()
                .contentType(ContentType.JSON)
                .body(courierLoginModel)
                .when()
                 .log().all()
                .post(COURIER_LOGIN_PATH);
    }

    @Step("Удаление курьера")
    public void deleteCourier(int id) {
        given()
                .log().all()
                .when()
                .delete(COURIER_DELETE_PATH + id)
                .then()
                .log().all()
                .extract().response();
    }

    @Step("Получение id курьера")
    public int getCourierId(String login, String password) {
        CourierLoginModel model = new CourierLoginModel(login, password);
        return loginCourier(model).jsonPath().getInt("id");
    }
}

package steps;

import data.ApiEndpoints;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.OrderModel;

public class OrderSteps {


    @Step("Создание заказа")
    public Response createOrder(OrderModel order) {
        return RestAssured.given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .log().all()
                .post(ApiEndpoints.ORDER_CREATE_PATH);
    }
    @Step("Получение списка заказов")
    public Response getOrderList() {
        return RestAssured.given()
                .log().all()
                .when()
                .log().all()
                .get(ApiEndpoints.ORDER_LIST_PATH);
    }
}

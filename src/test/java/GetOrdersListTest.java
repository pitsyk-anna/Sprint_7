import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersListTest extends BaseApiTest {

    private OrderSteps orderSteps;

    @Before
    public void createTest() {
        orderSteps = new OrderSteps();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что можно получить список заказов")
    public void getOrdersList() {
        Response response = orderSteps.getOrderList();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
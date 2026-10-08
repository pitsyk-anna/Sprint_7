import data.OrderData;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.OrderModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderSteps;
import static org.hamcrest.Matchers.notNullValue;
import java.util.List;
import static org.apache.http.HttpStatus.*;


@RunWith(Parameterized.class)
public class CreateOrderTest  extends BaseApiTest {
    private final List<String> color;
    private OrderSteps orderSteps;

    public CreateOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] getScooterColor() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()}
        };
    }

    @Before
    public void createTest() {
        orderSteps = new OrderSteps();
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка, что заказ создаётся с разными наборами цветов и возвращает track")
    public void orderCanBeCreated() {
        OrderModel order = OrderData.order(color);

        Response response = orderSteps.createOrder(order);

        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
    }
}
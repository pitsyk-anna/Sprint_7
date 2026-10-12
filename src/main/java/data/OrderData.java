package data;

import model.OrderModel;

import java.util.List;

public class OrderData {
    public static OrderModel order (List<String> color) {
        String firstName = "Mars";
        String lastName = "Petrov";
        String address = "Nova, 32";
        String metroStation = "Purple";
        String phone = "+79001234567";
        int rentTime = 6;
        String deliveryDate = "2026-10-17";
        String comment = "Позвонить за час";

        return new OrderModel(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color
        );
    }
}

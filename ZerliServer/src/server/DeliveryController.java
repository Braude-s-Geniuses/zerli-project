package server;

import communication.Message;
import communication.MessageFromServer;
import order.Order;
import order.OrderStatus;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DeliveryController {

    public static Connection connection = Server.databaseController.getConnection();

    public static Message getPreDeliveredOrdersFromServer() {
        List<Order> orders = new ArrayList<Order>();
        ResultSet resultSet = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `order` WHERE (order_status = 'NORMAL_CONFIRMED' OR order_status = 'EXPRESS_CONFIRMED') AND delivery_address IS NOT NULL;");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setCustomerId(resultSet.getInt("customer_id"));
                order.setBranch(resultSet.getString("branch"));
                order.setOrderStatus(resultSet.getString("order_status"));
                order.setGreetingCard(resultSet.getString("greeting_card"));
                order.setPrice(resultSet.getFloat("price"));
                order.setDiscountPrice(resultSet.getFloat("discount_price"));
                order.setOrderDate(resultSet.getTimestamp("order_date"));
                order.setDeliveryDate(resultSet.getTimestamp("delivery_date"));
                order.setDeliveryAddress(resultSet.getString("delivery_address"));
                order.setRecipientName(resultSet.getString("recipient_name"));
                order.setRecipientPhone(resultSet.getString("recipient_phone"));
                order.setCancelTime(resultSet.getTimestamp("cancel_time"));

                orders.add(order);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.IMPORT_DELIVERY_TABLE_NOT_SUCCEED);
        }

        return new Message(orders, MessageFromServer.IMPORT_DELIVERY_TABLE_SUCCEED);
    }


    public static Message sendDelivery(Order order) {
        Timestamp deliveryTime = Timestamp.valueOf(LocalDateTime.now());

        OrderStatus newStatus = getNewStatus(String.valueOf(order.getOrderStatus()));

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE `order` SET delivery_date = ?, order_status = ? WHERE order_id = ?");
            preparedStatement.setTimestamp(1, deliveryTime);
            preparedStatement.setString(2, newStatus.name());
            preparedStatement.setInt(3, order.getOrderId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.DELIVERY_UPDATE_FAIL);
        }
        return new Message(null, MessageFromServer.DELIVERY_UPDATE_SUCCESS);
    }

    private static OrderStatus getNewStatus(String oldStatus) {
        if (oldStatus == "Express Confirmed"){
            return OrderStatus.EXPRESS_COMPLETED;
        }
        return OrderStatus.NORMAL_COMPLETED;
    }


    public static Message getHistoryDeliveredOrdersFromServer() {
        List<Order> orders = new ArrayList<Order>();
        ResultSet resultSet = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `order` WHERE (order_status = 'NORMAL_COMPLETED' OR order_status = 'EXPRESS_COMPLETED');");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt("order_id"));
                order.setCustomerId(resultSet.getInt("customer_id"));
                order.setBranch(resultSet.getString("branch"));
                order.setOrderStatus(resultSet.getString("order_status"));
                order.setGreetingCard(resultSet.getString("greeting_card"));
                order.setPrice(resultSet.getFloat("price"));
                order.setDiscountPrice(resultSet.getFloat("discount_price"));
                order.setOrderDate(resultSet.getTimestamp("order_date"));
                order.setDeliveryDate(resultSet.getTimestamp("delivery_date"));
                order.setDeliveryAddress(resultSet.getString("delivery_address"));
                order.setRecipientName(resultSet.getString("recipient_name"));
                order.setRecipientPhone(resultSet.getString("recipient_phone"));
                order.setCancelTime(resultSet.getTimestamp("cancel_time"));

                orders.add(order);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.IMPORT_DELIVERY_TABLE_NOT_SUCCEED);
        }

        return new Message(orders, MessageFromServer.IMPORT_DELIVERY_TABLE_SUCCEED);
    }


}
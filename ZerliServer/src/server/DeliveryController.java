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

    /**
     *This function requests from the database all the orders that are ready to be delivered
     * @return Message with ArrayList of orders
     */
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
            return new Message(null, MessageFromServer.DELIVERIES_GET_FAIL);
        }

        return new Message(orders, MessageFromServer.DELIVERIES_GET_SUCCESS);
    }

    /**
     * This function requests from the database to set the order as delivered
     * @param order
     * @return
     */
    public static Message sendDelivery(Order order) {
        Timestamp deliveryTime = Timestamp.valueOf(LocalDateTime.now());

        OrderStatus newStatus = getNewStatus(order.getOrderStatus());

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

    /**
     * This function sets a new status to the order that is being delivered.
     * @param oldStatus
     * @return
     */
    private static OrderStatus getNewStatus(OrderStatus oldStatus) {
        return oldStatus == OrderStatus.EXPRESS_CONFIRMED ? OrderStatus.EXPRESS_COMPLETED : OrderStatus.NORMAL_COMPLETED;
    }

    /**
     * This function fetches from the database all the orders that were already delivered.
     * @return Message with ArrayList of orders.
     */
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
            return new Message(null, MessageFromServer.DELIVERIES_GET_FAIL);
        }

        return new Message(orders, MessageFromServer.DELIVERIES_GET_SUCCESS);
    }

    /**
     * This function updates the database of the customer that is being refunded.
     * @param data order with details of customer that is being refunded.
     * @return
     */
    public static Message refundOrder(Order data) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("UPDATE customer SET balance = balance + ? WHERE  customer_id = ?");
            preparedStatement.setFloat(1, data.getDiscountPrice());
            preparedStatement.setInt(2, data.getCustomerId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.DELIVERY_ORDER_REFUND_FAIL);
        }
        return new Message(null, MessageFromServer.DELIVERY_ORDER_REFUND_SUCCESS);
    }

}
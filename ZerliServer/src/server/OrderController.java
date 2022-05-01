package server;

import com.mysql.cj.xdevapi.Client;
import communication.Message;
import communication.MessageFromServer;
import order.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class OrderController {

    public Message getAllOrdersFromServer() {
        System.out.println("test for Order controller");

        List<Order> orders = new ArrayList<Order>();                                                                           //TODO change 4
        ResultSet resultSet = DatabaseController.getInstance().getAllOrders("SELECT * FROM orders WHERE customer_id = 4;");
        System.out.println("before While orders.");

        try {
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderId(resultSet.getInt(1));
                order.setCustomerId(resultSet.getInt(2));
                order.setBranch(resultSet.getString(3));
                order.setOrderStatus(resultSet.getString(4));
                order.setGreetingCard(resultSet.getString(5));
                order.setPrice(resultSet.getFloat(6));
                order.setDiscountPrice(resultSet.getFloat(7));
                order.setOrderDate(resultSet.getTimestamp(8));
                order.setDeliveryDate(resultSet.getTimestamp(9));
                order.setDeliveryAddress(resultSet.getString(10));
                order.setRecipientName(resultSet.getString(11));
                order.setRecipientPhone(resultSet.getString(12));
                order.setDeliveryDate(resultSet.getTimestamp(13));

                orders.add(order);
                System.out.println("result of query:" + orders.toString());
            }
             resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.IMPORT_ORDERS_TABLE_NOT_SUCCEED);
        }
        // want to return ArrayList of orders.add(null) if not successes;
        return new Message(orders, MessageFromServer.IMPORT_ORDERS_TABLE_SUCCEED);

    }


}

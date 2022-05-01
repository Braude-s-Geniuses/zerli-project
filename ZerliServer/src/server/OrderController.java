package server;

import com.mysql.cj.xdevapi.Client;
import communication.Message;
import order.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class OrderController {

    public Message getAllOrders() {
        List<Order> orders = new ArrayList<Order>();
        ResultSet resultSet = DatabaseController.getInstance().getAllOrders("SELECT * FROM orders WHERE customer_id = ?;");
        try {
            while (resultSet.next()) {
                Order order = new Order();
                order.setOrderNumber(resultSet.getInt(1));
                order.setPrice(resultSet.getFloat(2));
                order.setGreetingCardString(resultSet.getString(3));
                order.setColor(resultSet.getString(4));
                order.setDOrder(resultSet.getString(5));
                order.setShop(resultSet.getString(6));
                order.setDeliveryDate(resultSet.getTimestamp(7));
                order.setOrderDate(resultSet.getTimestamp(8));
                orders.add(order);
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

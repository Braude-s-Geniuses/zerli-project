package server;

import com.mysql.cj.xdevapi.Client;
import communication.Message;
import communication.MessageFromServer;
import order.Order;
import user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;







public class OrderController {

    public static Connection connection = Server.databaseController.getConnection();
            //DatabaseController.getInstance().getConnection();   //I think this one is null.

    public static Message getAllOrdersFromServer(int userId) {

        List<Order> orders = new ArrayList<Order>();
        ResultSet resultSet = null;
        System.out.println("userId in  getAllOrdersFromServer "+  userId);

        try {
            System.out.println("test before preparedStatement" );

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `order` WHERE `customer_id`=?;");
            System.out.println("test after preparedStatement" );

            preparedStatement.setInt(1, userId);
            System.out.println("test before executeQuery" );

            resultSet = preparedStatement.executeQuery();
            System.out.println("test after executeQuery" );


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
            }
            System.out.println("orders form DB " + orders);
             resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.IMPORT_ORDERS_TABLE_NOT_SUCCEED);
        }
        // want to return ArrayList of orders.add(null) if not successes;
        return new Message(orders, MessageFromServer.IMPORT_ORDERS_TABLE_SUCCEED);

    }

}

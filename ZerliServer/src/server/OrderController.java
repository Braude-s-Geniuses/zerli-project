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
            System.out.println("orders form DB " + orders);
             resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.IMPORT_ORDERS_TABLE_NOT_SUCCEED);
        }
        // want to return ArrayList of orders.add(null) if not successes;
        return new Message(orders, MessageFromServer.IMPORT_ORDERS_TABLE_SUCCEED);

    }

    public static Message getAllBranches() {
            ArrayList<String> branches = new ArrayList<>();
            Statement stmt;
            try {
                stmt = connection.createStatement();
                System.out.println("before execute");
                ResultSet resultSet = stmt.executeQuery("SELECT branch FROM branch;");
                System.out.println("after execute");
                while (resultSet.next()) {
                    branches.add(resultSet.getString("branch"));
                }
            } catch (SQLException e) {
               e.printStackTrace();
                return new Message(null, MessageFromServer.IMPORT_BRANCHES_NOT_SUCCEDD);
            }
            return new Message(branches, MessageFromServer.IMPORT_BRANCHES_SUCCEDD);
        }
    }

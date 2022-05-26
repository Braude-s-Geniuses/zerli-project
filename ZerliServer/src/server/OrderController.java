package server;

import communication.Message;
import communication.MessageFromServer;
import order.Order;
import order.OrderProduct;
import order.Product;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrderController {
    public static Connection connection = Server.databaseController.getConnection();

    public static Message getAllOrdersFromServer(int userId) {

        List<Order> orders = new ArrayList<Order>();
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `order` WHERE `customer_id`=?;");
            preparedStatement.setInt(1, userId);
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
            return new Message(null, MessageFromServer.IMPORT_ORDERS_TABLE_NOT_SUCCEED);
        }

        return new Message(orders, MessageFromServer.IMPORT_ORDERS_TABLE_SUCCEED);

    }

    public static Message getAllBranches() {
        ArrayList<String> branches = new ArrayList<>();
        Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT branch FROM branch;");
            while (resultSet.next()) {
                branches.add(resultSet.getString("branch"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.IMPORT_BRANCHES_NOT_SUCCEDD);
        }
        return new Message(branches, MessageFromServer.IMPORT_BRANCHES_SUCCEDD);
    }

    public static Message AddNewOrder(Order order) {
        int orderId = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `order` (order_id, customer_id, branch, order_status, greeting_card, price, discount_price, order_date, delivery_date, delivery_address, recipient_name, recipient_phone, cancel_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, order.getOrderId());
            preparedStatement.setInt(2, order.getCustomerId());
            preparedStatement.setString(3, order.getBranch());
            preparedStatement.setString(4, order.getOrderStatus().name());
            preparedStatement.setString(5, order.getGreetingCard());
            preparedStatement.setFloat(6, order.getPrice());
            preparedStatement.setFloat(7, order.getDiscountPrice());
            preparedStatement.setTimestamp(8, order.getOrderDate());
            preparedStatement.setTimestamp(9, order.getDeliveryDate());
            preparedStatement.setString(10, order.getDeliveryAddress());
            preparedStatement.setString(11, order.getRecipientName());
            preparedStatement.setString(12, order.getRecipientPhone());
            preparedStatement.setTimestamp(13, order.getCancelTime());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderId = generatedKeys.getInt(1);
                    updateOrderProductsInDB(order.getProductList(), orderId);
                } else {
                    return new Message(null, MessageFromServer.ADDED_ORDER_NOT_SUCCESSFULLY);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ADDED_ORDER_NOT_SUCCESSFULLY);
        }
        return new Message(orderId, MessageFromServer.ADDED_ORDER_SUCCESSFULLY);
    }

    private static void updateOrderProductsInDB(ArrayList<OrderProduct> productList, int orderId) {
        for (OrderProduct p : productList) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `order_product` (order_id,product_id, quantity) VALUES (?,?,?);");
                preparedStatement.setInt(1, orderId);
                preparedStatement.setInt(2, p.getProduct().getProductId());
                preparedStatement.setInt(3, p.getQuantity());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Message getOrderDetails(Integer orderId) {
        ArrayList<OrderProduct> op = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT p.*, op.quantity\n" +
                    "FROM `product` p, `order_product` op \n" +
                    "WHERE p.product_id = op.product_id AND op.order_id = ? ;");
            preparedStatement.setInt(1, orderId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                OrderProduct orderProduct = new OrderProduct();
                product.setProductId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getInt("price"));
                product.setDiscountPrice(resultSet.getInt("discount_price"));
                SerialBlob blob = new SerialBlob(resultSet.getBlob("image"));
                product.setImage(blob);
                product.setCustomMade(resultSet.getBoolean("custom_made"));
                product.setDominantColor(resultSet.getString("dominant_color"));
                orderProduct.setProduct(product);
                orderProduct.setQuantity(resultSet.getInt("quantity"));
                op.add(orderProduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(op, MessageFromServer.ORDER_PRODUCTS_DELIVERED_SUCCESSFULLY);
    }

    public static Message updateBalance(ArrayList<Object> data) {
        int customerId = (int) data.get(0);
        float balance = (float) data.get(1);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `customer`\n" +
                    "SET balance = ? \n" +
                    "WHERE customer_id = ? ;");
            preparedStatement.setFloat(1, balance);
            preparedStatement.setInt(2, customerId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.UPDATE_BALANCE_FAILED);
        }
        return new Message(null, MessageFromServer.UPDATE_BALANCE_SUCCEED);
    }

    public static Message updateCreditCard(ArrayList<Object> data) {
        int customerId = (int) data.get(0);
        ArrayList<String> cardDetails = (ArrayList<String>)data.get(1);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `customer`\n" +
                    "SET credit_card = ?, exp_date = ? ,cvv = ?  \n" +
                    "WHERE customer_id = ? ;");
            preparedStatement.setString(1, cardDetails.get(0));
            preparedStatement.setString(2, cardDetails.get(1));
            preparedStatement.setString(3, cardDetails.get(2));
            preparedStatement.setInt(4, customerId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            return new Message(null, MessageFromServer.UPDATE_CARD_FAILED);
        }
        return new Message(null, MessageFromServer.UPDATE_CARD_SUCCEED);
    }

    public static Message getLastReport() {
        String lastReport = null;
        Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT name FROM report WHERE report_id = (select MAX(report_id) FROM report);");
            while (resultSet.next()) {
                lastReport = resultSet.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.IMPORT_LAST_REPORT_NOT_SUCCEDD);
        }
        return new Message(lastReport, MessageFromServer.IMPORT_LAST_REPORT_SUCCEDD);

    }
}

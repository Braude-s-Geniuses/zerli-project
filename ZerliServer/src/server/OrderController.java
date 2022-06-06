package server;

import communication.Message;
import communication.MessageFromServer;
import order.*;
import product.Item;
import product.Product;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class OrderController {
    public static Connection connection = Server.databaseController.getConnection();

    /**
     * Get all order of specific customer from DB
     * @param userId - customer id
     * @return list of orders
     */
    public static Message getAllOrdersFromServer(int userId) {

        List<Order> orders = new ArrayList<Order>();
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `order` WHERE `customer_id`=? ORDER BY FIELD(order_status, 'EXPRESS_PENDING', 'NORMAL_PENDING', 'CANCEL_PENDING', 'NORMAL_CONFIRMED', 'EXPRESS_CONFIRMED', 'CANCEL_CONFIRMED', 'NORMAL_COMPLETED', 'EXPRESS_COMPLETED');");
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
            return new Message(null, MessageFromServer.ORDERS_GET_FAIL);
        }

        return new Message(orders, MessageFromServer.ORDERS_GET_SUCCESS);

    }

    /**
     * get all branches for combo box
     * @return list of all branches
     */
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
            return new Message(null, MessageFromServer.ORDER_BRANCHES_GET_FAIL);
        }
        return new Message(branches, MessageFromServer.ORDER_BRANCHES_GET_SUCCESS);
    }

    /**
     * Add new completed order to DB
     * @param order
     * @return message of success\fail
     */
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
                    return new Message(null, MessageFromServer.ORDER_CREATE_NEW_FAIL);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ORDER_CREATE_NEW_FAIL);
        }
        return new Message(orderId, MessageFromServer.ORDER_CREATE_NEW_SUCCESS);
    }

    /**
     * Insert product to completed order in DB
     * @param productList - the products that were purchased in the order
     * @param orderId
     */
    private static void updateOrderProductsInDB(ArrayList<OrderProduct> productList, int orderId) {
        for (OrderProduct p : productList) {
            try {
                if(p.getProduct().isCustomerProduct())
                    p.getProduct().setProductId(addCustomerBuiltProduct(p.getProduct()));

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

    /**
     * Gets the products of a specific order
     * @param orderId - wanted order id
     * @return
     */
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
        return new Message(op, MessageFromServer.ORDER_PRODUCTS_GET_SUCCESS);
    }

    /**
     * Update customer balance in DB
     * @param data - contains wanted customer id, new balance
     */
    public static void updateBalance(ArrayList<Object> data) {
        int customerId = (int) data.get(0);
        float balance = (float) data.get(1);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `customer`\n" +
                    "SET `balance` = ? \n" +
                    "WHERE `customer_id` = ? ;");
            preparedStatement.setFloat(1, balance);
            preparedStatement.setInt(2, customerId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update customer's credit card in DB
     * @param data- contains customer id, new credit card details
     */
    public static void updateCreditCard(ArrayList<Object> data) {
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
            e.printStackTrace();
        }
    }

    /**
     * Customer is no longer a new customer' updates it in DB
     * @param data - contains customer id
     */
    public static void updateNewCustomer(Integer data) {
        int customerId = data;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `customer`\n" +
                    "SET new_customer = 0  \n" +
                    "WHERE customer_id = ? ;");
            preparedStatement.setInt(1, customerId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param order
     * @return
     */
    public static Message UpdateOrderStatus(Order order) {
        String express = "";
        try {
            if(order.getOrderStatus() == OrderStatus.EXPRESS_CONFIRMED)
                express = ", delivery_date = '" + Timestamp.valueOf((LocalDateTime.now()).plusHours(3)) + "'";
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `order` SET order_status = ?" + express + " WHERE order_id = ?");
            preparedStatement.setString(1,order.getOrderStatus().name());
            preparedStatement.setInt(2,order.getOrderId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ORDER_STATUS_FAIL);
        }

        return new Message(null, MessageFromServer.ORDER_STATUS_SUCCESS);
    }

    public static Message UpdateOrderCancel(Order order) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `order` SET cancel_time = ? WHERE order_id = ?");
            preparedStatement.setTimestamp(1,order.getCancelTime());
            preparedStatement.setInt(2,order.getOrderId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ORDER_CANCEL_TIME_FAIL);
        }

        return new Message(null, MessageFromServer.ORDER_CANCEL_TIME_SUCCESS);
    }


    public static Message getBalance(int customerID) {
        float balance =0;
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM customer where customer_id = ?;");
            preparedStatement.setInt(1, customerID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                balance = resultSet.getFloat(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ORDER_GET_BALANCE_FAIL);
        }
        return new Message(balance, MessageFromServer.ORDER_GET_BALANCE_SUCCESS);
    }

    /**
     * Inserts into the database a customer's custom-built product before an order has been processed.
     * @param product - Product instance to insert
     * @return the newly created product_id; 0 on failure
     */
    private static int addCustomerBuiltProduct(Product product) {
        int product_id = 0;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `product` (name, price, discount_price, image, custom_made, dominant_color, in_catalog, customer_product) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setFloat(2, product.getPrice());
            preparedStatement.setFloat(3, product.getDiscountPrice());
            preparedStatement.setBlob(4, product.getImage());
            preparedStatement.setBoolean(5, product.isCustomMade());
            preparedStatement.setString(6, product.getDominantColor());
            preparedStatement.setBoolean(7, product.isInCatalog());
            preparedStatement.setBoolean(8, product.isCustomerProduct());
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (!generatedKeys.next())
                return 0;

            product_id = generatedKeys.getInt(1);

            for(Item item : product.getItems().keySet()) {
                preparedStatement = connection.prepareStatement("INSERT INTO product_item (product_id, item_id, quantity) VALUES (?, ?, ?)");
                preparedStatement.setInt(1, product_id);
                preparedStatement.setInt(2, item.getItemId());
                preparedStatement.setInt(3, product.getItems().get(item));

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product_id;
    }

    public static Message getBranch(int userID) {
        String branch = null;
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT branch FROM branch where manager_id = ?;");
            preparedStatement.setInt(1, userID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                branch = resultSet.getString("branch");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.ORDER_GET_BRANCH_FAIL);
        }

        return new Message(branch, MessageFromServer.ORDER_GET_BRANCH_SUCCESS);
    }

    /**
     * Get all order of specific customer from DB
     * @param branch - the branch to filter by
     * @return list of orders
     */
    public static Message getAllOrdersByBranch(String branch) {

        List<Order> orders = new ArrayList<Order>();
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `order` WHERE `branch` = ? ORDER BY FIELD(order_status, 'EXPRESS_PENDING', 'NORMAL_PENDING', 'CANCEL_PENDING', 'NORMAL_CONFIRMED', 'EXPRESS_CONFIRMED', 'CANCEL_CONFIRMED', 'NORMAL_COMPLETED', 'EXPRESS_COMPLETED');");
            preparedStatement.setString(1, branch);
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
            return new Message(null, MessageFromServer.ORDERS_GET_BY_BRANCH_FAIL);
        }

        return new Message(orders, MessageFromServer.ORDERS_GET_BY_BRANCH_SUCCESS);

    }
}
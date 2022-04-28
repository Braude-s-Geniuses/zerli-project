package server;

import common.Message;
import common.MessageFromServer;
import common.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DatabaseController represents the controller used to interact with the MySQL database.
 *
 */
public class DatabaseController {

    /** represents the only (static) instance of a databaseController.
     *
     */
    private static DatabaseController databaseController = null;

    /** represents the database connection instance.
     *
     */
    private Connection connection;

    /** stores the Application Database Schema name.
     *
     */
    private String dbName;

    /** stores the Application Database Username.
     *
     */
    private String dbUser;

    /** stores the Application Database Password.
     *
     */
    private String dbPassword;

    /** Getter of the <code>databaseController</code> (Singleton) instance.
     *
     * @return <code>databaseController</code> instance.
     */
    public static DatabaseController getInstance() {
        if (databaseController == null) {
            databaseController = new DatabaseController();
        }
        return databaseController;
    }

    private DatabaseController() { }

    /** The method is used to make a connection to the database.
     *
     * @return the result of the connection to it can be displayed on the gui.
     */
    public String connect() {
        String result = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            result += "\nDriver definition succeed";
        } catch (Exception ex) {
            /* handle the error*/
            result += "\nDriver definition failed";
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName + "?serverTimezone=IST", dbUser, dbPassword);
            result += "\nSQL connection succeed";

        } catch (SQLException ex) {/* handle any erroresultSet*/
            result += "\nSQLException: " + ex.getMessage();
            result += "\nSQLState: " + ex.getSQLState();
            result += "\nVendorError: " + ex.getErrorCode();
        }
        return result;
    }

    /** Getter for <code>connection</code>.
     *
     * @return <code>connection</code>
     */
    public Connection getConnection() {
        return connection;
    }

    /** The method is used to get all orders from the database
     *
     * @return <code>Message</code> and all orders inside packaged as <code>ArrayList</code> to be sent to the client or empty response if unsuccessful.
     */
    public Message getAllOrders() {
        List<Order> orders = new ArrayList<Order>();
        Statement stmt;
        try {
            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM orders;");
            while (resultSet.next()) {
                Order order = new Order(0, 0, null, null, null, null, null, null);
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

    /** The method is used to update an order in the database.
     *
     * @param message contains an <code>Order</code> instance with the data needed to be updated.
     * @return <code>Message</code> to be sent back to the client if update was successful or not.
     */
    public Message updateOrder(Message message) {
        PreparedStatement stmt;
        String column;

        try {
            column = message.getTask().name();
            column = column.toLowerCase().substring(7);
            stmt = connection.prepareStatement("UPDATE orders SET " + column + " =? WHERE order_number=?;");

            switch (message.getTask()) {
                case UPDATE_COLOR:
                    stmt.setString(1, ((Order) message.getData()).getColor());
                    break;
                case UPDATE_DATE:
                    stmt.setTimestamp(1, ((Order) message.getData()).getDeliveryDate());
                    break;
                default:
                    break;
            }
            stmt.setInt(2, ((Order) message.getData()).getOrderNumber());
            stmt.executeUpdate();

            /** Verifies using an SQL query if the update operation was successful.
             *  If not, returns to the client an unsuccessful message.
             */
            if (!verifyUpdateSuccessful((Order) message.getData(), column))
                return new Message(null, MessageFromServer.UPDATE_NOT_SUCCEED);

        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.UPDATE_NOT_SUCCEED);
        }
        return new Message(null, MessageFromServer.UPDATE_SUCCEED);
    }


    /** The method is used internally to verify an update was completed against the database.
     *
     * @param order which order to verify, containing the data for comparison.
     * @param column which field in <code>order</code> to verify.
     * @return <code>true</code> on update verified;<code>false</code> otherwise.
     */
    private boolean verifyUpdateSuccessful(Order order, String column) {
        PreparedStatement stmt;
        ResultSet resultSet;

        try {
            stmt = connection.prepareStatement("SELECT " + column + " FROM orders WHERE order_number=?;");
            stmt.setInt(1, order.getOrderNumber());
            resultSet = stmt.executeQuery();
            resultSet.next();

            switch(column) {
                case "color":
                    String color = null;
                    try {
                        color = resultSet.getString("color");
                    } catch (SQLException ex) { }
                    if(color.equals(order.getColor()))
                        return true;
                    break;
                case "date":
                    Timestamp date = null;
                    try {
                        date = resultSet.getTimestamp("date");
                    } catch (SQLException ex) { }
                    if(date.equals(order.getDeliveryDate()))
                        return true;
                    break;
            }

        } catch (SQLException e) { }

        return false;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}


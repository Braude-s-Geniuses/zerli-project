package server;

import communication.Message;
import communication.MessageFromServer;
import user.Customer;
import user.User;
import user.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController {

    private ZerliServer server;
    private Connection con;

    public UserController() {
        server = ServerController.getServer();
        con = Server.databaseController.getConnection();
    }

    /**
     * This method receives customer information - username and password and sends it to the
     * Database to check if the user exists in the system - otherwise it will return the other
     * relevant details otherwise, send a message that the user does not exist.
     *
     * @param data
     * @return
     */

    public Message login(User data) {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM user WHERE username=?");
            preparedStatement.setString(1, data.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Compares user password */
            if(!resultSet.next() || !data.getPassword().equals(resultSet.getString("password")))
                return new Message(null, MessageFromServer.LOGIN_NOT_SUCCEED);

            /* Checks if user is already logged in */
            if(resultSet.getBoolean("logged_in"))
                return new Message(null, MessageFromServer.ALREADY_LOGGED_IN);


            data.setUserId(resultSet.getInt("user_id"));
            data.setUserType(UserType.valueOf(resultSet.getString("user_type")));
            data.setLoggedIn(true);

            setUserAsLoggedIn(data.getUserId());

            data.setFirstName(resultSet.getString("first_name"));
            data.setLastName(resultSet.getString("last_name"));
            data.setId(resultSet.getString("id"));
            data.setEmail(resultSet.getString("email"));
            data.setPhone(resultSet.getString("phone"));

            if(data.getUserType() == UserType.CUSTOMER){
                Customer newCustomer= new Customer(data);
                addCustomerDetails(newCustomer);
                if(newCustomer.isBlocked())
                    return new Message(newCustomer, MessageFromServer.CUSTOMER_IS_BLOCKED);
                return new Message(newCustomer, MessageFromServer.LOGIN_SUCCEED);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.LOGIN_NOT_SUCCEED);
        }

        return new Message(data, MessageFromServer.LOGIN_SUCCEED);
    }

    public Message logout(int id) {
        if (setUserAsLoggedOut(id))
            return new Message(null, MessageFromServer.LOGOUT_SUCCEED);
        return new Message(null, MessageFromServer.LOGOUT_NOT_SUCCEED);
    }

    public void setUserAsLoggedIn(int id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("UPDATE user SET logged_in=1 WHERE user_id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean setUserAsLoggedOut(int id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("UPDATE user SET logged_in=0 WHERE user_id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void addCustomerDetails(Customer data) throws SQLException{
        PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM customer WHERE customer_id=?");
        preparedStatement.setInt(1, data.getUserId());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        data.setUserId(resultSet.getInt(1));
        data.setBlocked(resultSet.getBoolean(2));
        data.setCreditCard(resultSet.getString(3));
        data.setExpDate(resultSet.getString(4));
        data.setCvv(resultSet.getString(5));
        data.setBalance(resultSet.getFloat(6));
    }
}
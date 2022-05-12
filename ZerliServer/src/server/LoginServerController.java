package server;

import communication.Message;
import communication.MessageFromServer;
import user.Customer;
import user.User;
import user.UserType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginServerController {

    private ZerliServer server;
    private Connection con;

    public LoginServerController() {
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

    public Message tryToLogin(User data) {
        Statement stmt;
        try {
            stmt = con.createStatement();
            String queryToExecute = "select * FROM zerli.user where username= \"" + data.getUsername() + "\" AND password= \"" + data.getPassword() + "\";";
            ResultSet resultSet = stmt.executeQuery(queryToExecute);
            if (!resultSet.next()) {
                data.setUserType(UserType.UNREGISTERED);
                return new Message(data, MessageFromServer.LOGIN_NOT_REGISTERED);
            } else {
                data.setUserId(resultSet.getInt(1));
                data.setUserType(setUserType(resultSet.getString(4)));
                if (!resultSet.getBoolean(5)) {
                    registrationLoginInDB(data.getUserId());
                    data.setLoggedIn(resultSet.getBoolean(5));
                } else {
                    data.setUserType(UserType.LOGGED_IN);
                    return new Message(data, MessageFromServer.ALREADY_LOGGED_IN);
                }
                data.setFirstName(resultSet.getString(6));
                data.setLastName(resultSet.getString(7));
                data.setId(resultSet.getString(8));
                data.setEmail(resultSet.getString(9));
                data.setPhone(resultSet.getString(10));

                if(data.getUserType() ==UserType.CUSTOMER){
                    Customer newCustomer= new Customer(data);
                    addCustomerDetails(newCustomer);
                    if(newCustomer.isBlocked())
                        return new Message(newCustomer, MessageFromServer.CUSTOMER_IS_BLOCKED);
                    return new Message(newCustomer, MessageFromServer.LOGIN_SUCCEED);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.LOGIN_NOT_SUCCEED);
        }

        return new Message(data, MessageFromServer.LOGIN_SUCCEED);
    }

    public Message tryToLogout(int id) {
        if (registrationLogoutDB(id))
            return new Message(null, MessageFromServer.LOGOUT_SUCCEED);
        return new Message(null, MessageFromServer.LOGOUT_NOT_SUCCEED);
    }

    public UserType setUserType(String type) {
        switch (type.toUpperCase()) {
            case "CUSTOMER":
                return UserType.CUSTOMER;
            case "BRANCH_EMPLOYEE":
                return UserType.BRANCH_EMPLOYEE;
            case "BRANCH_MANAGER":
                return UserType.BRANCH_MANAGER;
            case "SERVICE_EMPLOYEE":
                return UserType.SERVICE_EMPLOYEE;
            case "EXPERT_SERVICE_EMPLOYEE":
                return UserType.EXPERT_SERVICE_EMPLOYEE;
            case "DELIVERY_OPERATOR":
                return UserType.DELIVERY_OPERATOR;
            case "CEO":
                return UserType.CEO;
            case "LOGGED_IN":
                return UserType.LOGGED_IN;
        }
        return UserType.UNREGISTERED;
    }

    public void registrationLoginInDB(int id) {
        Statement stmt;
        String queryToExecute = new String();
        try {
            stmt = con.createStatement();
            queryToExecute = "UPDATE zerli.user SET logged_in = 1 WHERE user_id = \"" + id + "\";";
            stmt.executeUpdate(queryToExecute);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return;
    }

    public boolean registrationLogoutDB(int id) {
        Statement stmt;
        String queryToExecute = new String();
        try {
            stmt = con.createStatement();
            queryToExecute = "UPDATE zerli.user SET logged_in = 0 WHERE user_id = \"" + id + "\";";
            stmt.executeUpdate(queryToExecute);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addCustomerDetails(Customer data) throws SQLException{ // TODO
        Statement stmt;
        stmt = con.createStatement();
        String queryToExecute = "select * FROM customer where customer_id = \"" + data.getUserId() + "\";";
        ResultSet resultSet = stmt.executeQuery(queryToExecute);
        resultSet.next();
        data.setUserId(resultSet.getInt(1));
        data.setBlocked(resultSet.getBoolean(2));
        data.setCreditCard(resultSet.getString(3));
        data.setExpDate(resultSet.getString(4));
        data.setCvv(resultSet.getString(5));
        data.setBalance(resultSet.getFloat(6));

        return;
    }
}
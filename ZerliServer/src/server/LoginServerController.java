package server;

import communication.Message;
import communication.MessageFromServer;
import user.User;
import user.UserType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginServerController {

    private ZerliServer server;
    private Connection con;

    public LoginServerController(){
        server=ServerController.getServer();
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
            String queryToExecute = "select * FROM zerlinew.user where username= \"" + data.getUsername() + "\" AND password= \"" + data.getPassword() + "\";";
            ResultSet resultSet = stmt.executeQuery(queryToExecute);
            resultSet.next();
              if (resultSet.getString(4) == null) {
                data.setUserType(UserType.UNREGISTERED);
                return new Message(data, MessageFromServer.LOGIN_NOT_REGISTERED);
           }
            else{
                data.setUserId(resultSet.getInt(1));
                data.setUserType(setUserType(resultSet.getString(4)));
                data.setLoggedIn(resultSet.getBoolean(5));
                data.setFirstName(resultSet.getString(6));
                data.setLastName(resultSet.getString(7));
                data.setId(resultSet.getString(8));
                data.setEmail(resultSet.getString(9));
                data.setPhone(resultSet.getString(10));
           }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null,MessageFromServer.LOGIN_NOT_SUCCEED);
        }
        return new Message(data, MessageFromServer.LOGIN_SUCCEED);
    }

    public UserType setUserType(String type){
        switch(type.toUpperCase()){
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
        }
        return UserType.UNREGISTERED;
    }


}

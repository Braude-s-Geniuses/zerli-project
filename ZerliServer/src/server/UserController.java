package server;

import communication.Message;
import communication.MessageFromServer;
import user.BranchEmployee;
import user.Customer;
import user.User;
import user.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserController {

    private  ZerliServer server;
    private final Connection con;

    public UserController() {
        server = ServerController.getServer();
        con = Server.databaseController.getConnection();
    }

    public UserController(Connection con){
        this.con=con;
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
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM `user` WHERE username=?");
            preparedStatement.setString(1, data.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Compares user password */
            if(!resultSet.next() || !data.getPassword().equals(resultSet.getString("password")))
                return new Message(null, MessageFromServer.LOGIN_FAIL);

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
                return new Message(newCustomer, MessageFromServer.LOGIN_SUCCESS);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.LOGIN_FAIL);
        }

        return new Message(data, MessageFromServer.LOGIN_SUCCESS);
    }

    /**
     * Logs a given userId out of the system
     * @param id
     * @return
     */
    public Message logout(int id) {
        if (setUserAsLoggedOut(id))
            return new Message(null, MessageFromServer.LOGOUT_SUCCESS);
        return new Message(null, MessageFromServer.LOGOUT_FAIL);
    }

    /**
     * Sets a given userId as logged into the system
     * @param id
     */
    public void setUserAsLoggedIn(int id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("UPDATE `user` SET logged_in=1 WHERE user_id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a given userId as logged out of the system
     * @param id
     */
    public boolean setUserAsLoggedOut(int id) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("UPDATE `user` SET logged_in=0 WHERE user_id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Gets customer data from the database
     * @param data
     * @throws SQLException
     */
    public void addCustomerDetails(Customer data) throws SQLException{
        PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM `customer` WHERE customer_id=?");
        preparedStatement.setInt(1, data.getUserId());
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();

        data.setUserId(resultSet.getInt(1));
        data.setBlocked(resultSet.getBoolean(2));
        data.setCreditCard(resultSet.getString(3));
        data.setExpDate(resultSet.getString(4));
        data.setCvv(resultSet.getString(5));
        data.setBalance(resultSet.getFloat(6));
        data.setNewCustomer(resultSet.getBoolean(7));
    }

    /**
     * Gets a user's information according to its type for user management
     * @param userIdAndManagerId
     * @return
     */
    public Message getUserInformation(List<String> userIdAndManagerId) {
        User user = new User();
        try {
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM `user` WHERE id=?");
            preparedStatement.setString(1, userIdAndManagerId.get(0));
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next())
                return new Message(null, MessageFromServer.USER_INFORMATION_GET_FAIL);
            user.setUserType(UserType.valueOf(resultSet.getString("user_type")));
            user.setUserId(resultSet.getInt("user_id"));
            if(user.getUserType()==UserType.UNREGISTERED) {
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setId(userIdAndManagerId.get(0));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                return new Message(user, MessageFromServer.USER_INFORMATION_GET_SUCCESS);
            }
            else if(user.getUserType()==UserType.CUSTOMER) {
                Customer customer = new Customer(user);
                preparedStatement = con.prepareStatement("SELECT * FROM `customer` WHERE customer_id=?");
                preparedStatement.setInt(1, user.getUserId());
                resultSet = preparedStatement.executeQuery();
                if(!resultSet.next())
                    return new Message(null, MessageFromServer.USER_INFORMATION_GET_FAIL);
                customer.setBalance(resultSet.getFloat("balance"));
                customer.setBlocked(resultSet.getBoolean("blocked"));
                return new Message(customer, MessageFromServer.USER_INFORMATION_GET_SUCCESS);
            }
            else if (user.getUserType()==UserType.BRANCH_EMPLOYEE) {
                BranchEmployee branchEmployee = new BranchEmployee(user);
                preparedStatement = con.prepareStatement("SELECT * FROM `user` WHERE id=?");
                preparedStatement.setString(1, userIdAndManagerId.get(1));
                resultSet = preparedStatement.executeQuery();
                if(!resultSet.next())
                    return new Message(null, MessageFromServer.USER_INFORMATION_GET_FAIL);
                int userID = resultSet.getInt("user_id");

                preparedStatement = con.prepareStatement("SELECT * FROM `branch` WHERE manager_id=?");
                preparedStatement.setInt(1, userID);
                resultSet = preparedStatement.executeQuery();
                if(!resultSet.next())
                    return new Message(null, MessageFromServer.USER_INFORMATION_GET_FAIL);
                String branch = resultSet.getString("branch");

                preparedStatement = con.prepareStatement("SELECT * FROM `branch_employee` WHERE user_id=? AND" +
                        " branch=?");
                preparedStatement.setInt(1, user.getUserId());
                preparedStatement.setString(2, branch);
                resultSet = preparedStatement.executeQuery();
                if(!resultSet.next())
                    return new Message(null, MessageFromServer.USER_INFORMATION_GET_FAIL);
                branchEmployee.setSurvey(resultSet.getBoolean("survey"));
                branchEmployee.setCatalogue(resultSet.getBoolean("catalogue"));
                branchEmployee.setDiscount(resultSet.getBoolean("discount"));
                return new Message(branchEmployee, MessageFromServer.USER_INFORMATION_GET_SUCCESS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Message(null, MessageFromServer.USER_INFORMATION_GET_FAIL);
    }

    /**
     * Updates branch employee's permission within the system
     * @param branchEmployee
     * @return
     */
    public Message changeBranchEmployeePermission(BranchEmployee branchEmployee)
    {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("UPDATE `branch_employee` SET survey=?, discount=?" +
                    ", catalogue=? WHERE user_id= ? ;");
            preparedStatement.setBoolean(1, branchEmployee.isSurvey());
            preparedStatement.setBoolean(2, branchEmployee.isDiscount());
            preparedStatement.setBoolean(3, branchEmployee.isCatalogue());
            preparedStatement.setInt(4, branchEmployee.getUserId());
            boolean result =preparedStatement.execute();
            return new Message(true, MessageFromServer.EMPLOYEE_PERMISSION_CHANGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(false, MessageFromServer.EMPLOYEE_PERMISSION_CHANGE);
    }

    /**
     * Creates a new customer in the database after approving the user as authorized customer
     * @param data
     * @return
     */
    public Message createNewCustomer(Customer data) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("INSERT INTO `customer` VALUES (?,?,?,?,?,?,?);");
            preparedStatement.setInt(1,data.getUserId());
            preparedStatement.setBoolean(2,data.isBlocked());
            preparedStatement.setString(3,data.getCreditCard());
            preparedStatement.setString(4,data.getExpDate());
            preparedStatement.setString(5,data.getCvv());
            preparedStatement.setDouble(6,data.getBalance());
            preparedStatement.setBoolean(7,true);//new customer
            boolean result = preparedStatement.execute();
            preparedStatement = con.prepareStatement("UPDATE `user` SET user_type=? WHERE user_id=?");
            preparedStatement.setString(1, UserType.CUSTOMER.name());
            preparedStatement.setInt(2, data.getUserId());
            result = preparedStatement.execute();
            return new Message(true, MessageFromServer.CUSTOMER_CREATE_NEW);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(false, MessageFromServer.CUSTOMER_CREATE_NEW);

    }

    /**
     * Freezes or unfreezes a given customer's account
     * @param customer
     * @return
     */
    public Message FreezeCustomer(Customer customer) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = con.prepareStatement("UPDATE customer SET blocked=? WHERE customer_id=?");
            preparedStatement.setBoolean(1,customer.isBlocked());
            preparedStatement.setInt(2,customer.getUserId());
            boolean result = preparedStatement.execute();
            return new Message(true, MessageFromServer.CUSTOMER_FREEZE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(false, MessageFromServer.CUSTOMER_FREEZE);
    }

    /**
     * Gets branch employee's permission within the system
     * @param user
     * @return
     */
    public Message getUserPermission(User user) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet;
        switch (user.getUserType())
        {
            case BRANCH_EMPLOYEE:
                try {
                    preparedStatement = con.prepareStatement("SELECT * FROM `branch_employee` WHERE user_id=?");
                    preparedStatement.setInt(1,user.getUserId());
                    resultSet = preparedStatement.executeQuery();
                    if(!resultSet.next())
                        return new Message(null, MessageFromServer.GET_USER_PERMISSION_NOT_SUCCEED);
                    BranchEmployee branchEmployee = new BranchEmployee(user);
                    branchEmployee.setSurvey(resultSet.getBoolean("survey"));
                    branchEmployee.setDiscount(resultSet.getBoolean("discount"));
                    branchEmployee.setCatalogue(resultSet.getBoolean("catalogue"));
                    return new Message(branchEmployee, MessageFromServer.GET_USER_PERMISSION_SUCCEED);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        return new Message(null, MessageFromServer.GET_USER_PERMISSION_NOT_SUCCEED);
    }

    /**
     * gets a customer's email by user id
     * @param customerId - the user id to look for
     * @return String email
     */
    public Message getCustomerEmail(int customerId) {
        PreparedStatement preparedStatement = null;
        String result = null;
        try {
            preparedStatement = con.prepareStatement("SELECT email FROM `user` WHERE user_id = ?");
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next())
                return new Message(null, MessageFromServer.CUSTOMER_GET_EMAIL_FAIL);
            result = resultSet.getString("email");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Message(result, MessageFromServer.CUSTOMER_GET_EMAIL_SUCCESS);
    }
}
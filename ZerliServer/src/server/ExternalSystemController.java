package server;

import servergui.ServerUIController;
import user.User;
import user.UserType;
import java.sql.*;
import java.util.ArrayList;

public class ExternalSystemController {
    public static Connection connection;

    /**
     * Connect to the DB where external system is in
     * @return a string of success/ fails
     */
    public static boolean connect() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            /* handle the error*/
            ServerUIController.printToServerConsoleUI("Driver definition failed");
            return false;
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + Server.databaseController.getDbName() + "?serverTimezone=Asia/Jerusalem", Server.databaseController.getDbUser(), Server.databaseController.getDbPassword());
            ServerUIController.printToServerConsoleUI("External System connection succeed");

        } catch (SQLException ex) {/* handle any erroresultSet*/
            ServerUIController.printToServerConsoleUI("External System connection failed, there has been an SQLException: " + ex.getMessage());
            ServerUIController.printToServerConsoleUI("SQLState: " + ex.getSQLState());
            ServerUIController.printToServerConsoleUI("VendorError: " + ex.getErrorCode());
            return false;
        }

        return true;
    }

    /**
     * Imports the users from the external system to the right tables in zerli DB
     */
    public static void importUsers() {
        ArrayList<ArrayList<Object>> externalUsers = new ArrayList<>();
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `external_system`;");
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ArrayList<Object> userPlusBranch = new ArrayList<>();
                User user = new User();
                user.setUserId(0);
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setUserType(UserType.valueOf(resultSet.getString("user_type")));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setId(resultSet.getString("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                userPlusBranch.add(user);
                userPlusBranch.add(resultSet.getString("branch"));
                externalUsers.add(userPlusBranch);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        importUsersForUserTable(externalUsers);
    }

    /**
     * Filling the user table with the users from external system
     * @param externalUsers
     */
    private static void importUsersForUserTable(ArrayList<ArrayList<Object>> externalUsers) {
        for (ArrayList<Object> userPlusBranch : externalUsers) {
            User user = (User) userPlusBranch.get(0);
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `user`\n" +
                        "(user_id, username,password,user_type,logged_in,first_name,last_name,id,email,phone) \n" +
                        "VALUES (?,?,?,?,?,?,?,?,?,?) ;", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, 0);
                preparedStatement.setString(2, user.getUsername());
                preparedStatement.setString(3, user.getPassword());
                preparedStatement.setString(4, user.getUserType().name());
                preparedStatement.setBoolean(5, false);
                preparedStatement.setString(6, user.getFirstName());
                preparedStatement.setString(7, user.getLastName());
                preparedStatement.setString(8, user.getId());
                preparedStatement.setString(9, user.getEmail());
                preparedStatement.setString(10, user.getPhone());
                preparedStatement.executeUpdate();


                //if it gets here, the user is a new user
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys(); //saves the key for later use
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                }

                if(user.getUserType().toString().equals("Branch Manager")){
                    addManagerToBranch(userPlusBranch);
                }
                if(user.getUserType().toString().equals("Branch Employee")){
                    addEmployeeToBranchEmployee(userPlusBranch);
                }
            } catch (SQLException e) {
            }
        }
    }

    /**
     * updates the branch managers of the branches in branches table
     * @param userPlusBranch
     */
    private static void addManagerToBranch(ArrayList<Object> userPlusBranch){
        User user = (User) userPlusBranch.get(0);
        String branch = (String) userPlusBranch.get(1);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `branch`\n" +
                    "SET manager_id = ? \n" +
                    "WHERE branch = ? ;");
            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.setString(2, branch);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add branch employee to branch employee table in zerli
     * @param userPlusBranch
     */
    private static void addEmployeeToBranchEmployee(ArrayList<Object> userPlusBranch) {
        User user = (User) userPlusBranch.get(0);
        String branch = (String) userPlusBranch.get(1);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `branch_employee`\n" +
                    "(user_id, branch,survey,discount,catalogue) \n" +
                    "VALUES (?,?,?,?,?) ;");
            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.setString(2, branch);
            preparedStatement.setInt(3, 0);
            preparedStatement.setInt(4, 0);
            preparedStatement.setInt(5, 0);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
        }
    }
}
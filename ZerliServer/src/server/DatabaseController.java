package server;

import servergui.ServerUIController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
    protected Connection connection;

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

    protected DatabaseController() { }

    /** The method is used to make a connection to the database.
     *
     * @return the result of the connection to it can be displayed on the gui.
     */
    public String connect() {
        String result = "";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            ServerUIController.printToServerConsoleUI("Driver definition succeed");
            result += "\nDriver definition succeed";
        } catch (Exception ex) {
            /* handle the error*/
            ServerUIController.printToServerConsoleUI("Driver definition failed");
            result += "\nDriver definition failed";
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + dbName + "?serverTimezone=Asia/Jerusalem", dbUser, dbPassword);
            ServerUIController.printToServerConsoleUI("SQL connection succeed");
            result += "\nSQL connection succeed";

        } catch (SQLException ex) {/* handle any erroresultSet*/
            ServerUIController.printToServerConsoleUI("SQLException: " + ex.getMessage());
            ServerUIController.printToServerConsoleUI("SQLState: " + ex.getSQLState());
            ServerUIController.printToServerConsoleUI("VendorError: " + ex.getErrorCode());
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

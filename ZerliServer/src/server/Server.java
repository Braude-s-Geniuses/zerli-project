package server;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import servergui.ServerUIController;

import java.io.IOException;

/** Server represents the main ZerliServer Runnable
 *
 */
public class Server extends Application {



    /** Represents the instance of the server gui controller.
     *
     */
    public static ServerUIController serverUIController;

    /** represents the only (static) instance of a server controller.
     *
     */
    public static ServerController serverController;


    /** represents the only (static) instance of a database controller.
     *
     */
    public static DatabaseController databaseController;

    /** the default port on which <code>ZerliServer</code> listens to.
     *
     */
    final public static int DEFAULT_PORT = 5555;

    /** main method invoked once the Application starts.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    /** Initializes the <code>ZerliServer</code>> instance, with the UI and displays it.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        serverUIController = new ServerUIController();
        serverUIController.start(primaryStage);

        serverController = new ServerController();

        /** Once the server window is closed, close the server connection safely.
         *
         */
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Server.stopServer();
                System.exit(0);
            }
        });
    }

    /** The method is used to start listening for connections on <code>ZerliServer</code>
     * and also the connection to the database through the <code>databaseController</code>
     *
     * @param dbName Database Schema
     * @param dbUser Database User
     * @param dbPassword Database Password
     * @return the result of both connections to be displayed on the gui.
     */
    public static String startServer(String dbName, String dbUser, String dbPassword) {
        String result = "";
        String resultSQL = "";

        databaseController = DatabaseController.getInstance();
        databaseController.setDbName(dbName);
        databaseController.setDbUser(dbUser);
        databaseController.setDbPassword(dbPassword);

        resultSQL = databaseController.connect();
        System.out.println(resultSQL);
        result += resultSQL;

        if (resultSQL.contains("SQL connection succeed")) {
            try {
                ServerController.getServer().listen();
                ServerUIController.printToServerConsoleUI("Connected to Server!");
                ServerUIController.printToServerConsoleUI("Server listening for connections on port " + ServerController.getServer().getPort());
                result += "\nConnected to Server!";
                result += "\nServer listening for connections on port " + ServerController.getServer().getPort();
            } catch (Exception e) {
                ServerUIController.printToServerConsoleUI("[ERROR] Failed to start Server");
                result = "[ERROR] Failed to start Server";
            }
        }

        /* A Task that checks once a day if new reports need to be created */
        ReportController.setConnection(Server.databaseController.getConnection());
        ReportController.TaskGenerateReport();

        return result;
    }

    /** The method is used to stop listening for connections on <code>ZerliServer</code>
     *
     */
    public static void stopServer() {
        try {
            ServerController.getServer().close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
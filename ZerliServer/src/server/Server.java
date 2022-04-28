package server;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/** Server represents the main ZerliServer Runnable
 *
 */
public class Server extends Application {

    /** represents the only (static) instance of a server.
     *
     */
    public static ZerliServer zerliServer;

    /** represents the only (static) instance of a database contrller.
     *
     */
    public static DatabaseController databaseController;

    /**
     * the default port on which <code>ZerliServer</code> listens to.
     */
    final public static int DEFAULT_PORT = 5555;

    /** main method invoked once the Application starts.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception {
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
        zerliServer = new ZerliServer(DEFAULT_PORT, primaryStage);

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

        if (resultSQL.contains("SQL connection succeed")) {
            result += resultSQL;
            try {
                zerliServer.listen();
                result += "\nConnected to Server!";
                result += "\nServer listening for connections on port " + zerliServer.getPort();
            } catch (Exception e) {
                result = "[ERROR] Failed to start Server";
            }
        }

        return result;
    }

    /** The method is used to stop listening for connections on <code>ZerliServer</code>
     *
     */
    public static void stopServer() {
        try {
            zerliServer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}

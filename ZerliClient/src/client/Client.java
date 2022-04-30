package client;

import clientgui.InputHostnameFormController;
import communication.Message;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import user.User;
import order.Order;

import static communication.MessageFromClient.LOGIN_REQUEST;

/**
 * Client represents the main ZerliClient Runnable
 */
public class Client extends Application {

    /**
     * represents the only (static) instance of a client.
     */
    public static ClientController clientController;

    public static CatalogController catalogController;

    /**
     * represents the controller instance of the hostname form gui.
     */
    public InputHostnameFormController inputHostnameFormController;

    /**
     * main method invoked once the Application starts.
     *
     * @param args
     * @throws Exception
     */

    /** represents instance of Order Controller.
     *
     */
    public static OrderController orderController;


    public static void main(String args[]) throws Exception {
        launch(args);
    }

    /**
     * Initializes the Hostname Input Form Controller and displays it.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        inputHostnameFormController = new InputHostnameFormController();
        inputHostnameFormController.start(primaryStage);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.exit(0);
            }
        });
    }

    /**
     * Initializes the Client Controller once the Client clicks connect with a valid hostname.
    /** Initializes the Client Controller once the Client clicks connect with a valid hostname. TODO
     *
     * @param hostname <code>ZerliServer</code> to connect to
     */
    public static void initController(String hostname) {
        clientController = new ClientController(hostname);
        catalogController = new CatalogController();
        orderController = new OrderController();
    }

}

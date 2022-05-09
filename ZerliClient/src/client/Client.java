package client;

import clientgui.InputHostnameFormController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Client represents the main ZerliClient Runnable
 */
public class Client extends Application {

    /**
     * represents the only (static) instance of a client.
     */
    public static ClientController clientController;

    public static LoginClientController loginClientController;

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
        inputHostnameFormController.start();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.exit(0);
            }
        });
    }

    /**
     * Initializes the Client Controller once the Client clicks connect with a valid hostname.
     * @param hostname <code>ZerliServer</code> to connect to
     */
    public static void initController(String hostname) {
        clientController = new ClientController(hostname);
        loginClientController = new LoginClientController();
        catalogController = new CatalogController();
        orderController = new OrderController();
    }

    /** Creates and passes to a given scene
     *
     * @param event
     * @param fxml
     */
    public static void setScene(ActionEvent event, java.net.URL fxml) {
        Stage currentStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(fxml);
            root = (Parent) loader.load();
            Scene scene = new Scene(root);


            currentStage.setScene(scene);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

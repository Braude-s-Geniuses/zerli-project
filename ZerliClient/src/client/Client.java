package client;

import clientgui.InputHostnameFormController;
import clientgui.MainDashboardController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * Client represents the main ZerliClient Runnable
 */
public class Client extends Application {

    public static MainDashboardController mainDashboardController;

    /**
     * represents the only (static) instance of a client.
     */
    public static ClientController clientController;

    public static UserController userController;

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
        mainDashboardController = new MainDashboardController();
        clientController = new ClientController(hostname);
        userController = new UserController();
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

    public static ScrollPane loadContentScene() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(mainDashboardController);
        ScrollPane contentPane = (ScrollPane) loader.load(Client.class.getClassLoader().getResourceAsStream("clientgui/MainDashboard.fxml"));
        return contentPane;
    }
}

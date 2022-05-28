package client;

import clientgui.InputHostnameFormController;
import javafx.application.Application;
import javafx.event.EventHandler;
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
    public static UserController userController;
    public static DeliveryController deliveryController;
    public static CatalogController catalogController;
    public static ItemController itemController;
    public static ProductController productController;
    public static SurveyController surveyController;
    public static ComplaintController complaintController;
    public static ReportController reportController;
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
        InputHostnameFormController inputHostnameFormController = new InputHostnameFormController();
        inputHostnameFormController.start();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.exit(0);
            }
        });
    }

    /**
     * Initializes the Controllers once the Client clicks connect with a valid hostname.
     * @param hostname <code>ZerliServer</code> to connect to
     */
    public static void initController(String hostname) {
        clientController = new ClientController(hostname);
        userController = new UserController();
        deliveryController = new DeliveryController();
        catalogController = new CatalogController();
        orderController = new OrderController();
        itemController = new ItemController();
        productController = new ProductController();
        surveyController = new SurveyController();
        complaintController = new ComplaintController();
        reportController = new ReportController();
    }
}

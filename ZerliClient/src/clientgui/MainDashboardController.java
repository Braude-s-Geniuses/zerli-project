package clientgui;

import client.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import util.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainDashboardController implements Initializable {
    public static Pane topNavigationBox;
    public static Button btnLogInOrOut;
    public static Button btnCart;
    public static VBox sideNavigationBox;
    private static AnchorPane contentBox;

    @FXML
    private Pane topNavigation;

    @FXML
    private VBox sideNavigation;

    @FXML
    private AnchorPane contentPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contentBox = contentPane;
        sideNavigationBox = sideNavigation;
        topNavigationBox = topNavigation;
        buildUserNavigation();
    }

    public void clickLogo() {
        setContentFromFXML(
                Client.userController.getLoggedInUser() != null ?
                        "UserHomePage.fxml" :
                        "BrowseCatalogPage.fxml"
        );
    }

    /** Displays a custom alert message inside the dashboard's content AnchorPane for a specified duration.
     *
     * @param message - The alert message to show
     * @param alert - Which type of alert (Refer to <code>util.Alert</code>). The only difference between each type
     *              is the colors used.
     * @param duration - For how long to show the already (e.g. Duration.seconds(2), Duration.minutes(1), etc...)
     * @param layoutX - X position in the AnchorPane to place the alert.
     * @param layoutY - Y position in the AnchorPane to place the alert.
     */
    public static void createAlert(String message, Alert alert, Duration duration, double layoutX, double layoutY) {
        TextFlow textFlow = new TextFlow();
        Text text = new Text(message);

        switch(alert) {
            case PRIMARY:
                text.setFill(Color.web("#084298"));
                break;
            case SECONDARY:
                text.setFill(Color.web("#41464b"));
                break;
            case SUCCESS:
                text.setFill(Color.web("#0f5132"));
                break;
            case DANGER:
                text.setFill(Color.web("#842029"));
                break;
            case WARNING:
                text.setFill(Color.web("#664d03"));
                break;
            case INFO:
                text.setFill(Color.web("#055160"));
                break;
            case LIGHT:
                text.setFill(Color.web("#636464"));
                break;
            case DARK:
                text.setFill(Color.web("#141619"));
                break;
        }
        textFlow.getChildren().add(text);
        textFlow.getStyleClass().add("text-flow");
        textFlow.getStyleClass().add("text-flow-" + alert);
        textFlow.setLayoutX(layoutX);
        textFlow.setLayoutY(layoutY);

        MainDashboardController.getContentBox().getChildren().add(textFlow);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        duration,
                        event -> {
                            MainDashboardController.getContentBox().getChildren().remove(textFlow);
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void buildUserNavigation() {
        /* Clear all previously add buttons except the logo */
        sideNavigationBox.getChildren().remove(1, (int)sideNavigationBox.getChildren().stream().count());
        /* Clear all previously add buttons */
        topNavigationBox.getChildren().remove(0, (int)topNavigationBox.getChildren().stream().count());

        Button buttonBrowseCatalog = new Button("Browse Catalog");
        buttonBrowseCatalog.setOnAction(event -> setContentFromFXML("BrowseCatalogPage.fxml"));

        Button buttonAddItem = new Button("Add Item");
        buttonAddItem.setOnAction(event -> setContentFromFXML("ItemAddPage.fxml"));

        Button buttonManageItems = new Button("Manage Items");
        buttonManageItems.setOnAction(event -> setContentFromFXML("ItemsManagePage.fxml"));

        Button buttonAddProduct = new Button("Add Product");
        buttonAddProduct.setOnAction(event -> setContentFromFXML("ProductAddPage.fxml"));

        Button buttonManageProducts = new Button("Manage Products");
        buttonManageProducts.setOnAction(event -> setContentFromFXML("ProductsManagePage.fxml"));

        Button buttonSurveyAnswers = new Button("Submit Survey Answers");
        buttonSurveyAnswers.setOnAction(event -> setContentFromFXML("SurveyAnswerSubmitPage.fxml"));

        /* If current Client is a guest, shows only Browse Catalog in side-menu */
        if(Client.userController.getLoggedInUser() == null) {
            addSideNavButton(buttonBrowseCatalog);
        }
        else {
            switch (Client.userController.getLoggedInUser().getUserType()) {
                case CUSTOMER:
                    btnCart = new Button();
                    btnCart.setLayoutX(770.0);
                    btnCart.setMinWidth(150.0);
                    btnCart.getStyleClass().add("cart-btn");
                    btnCart.setGraphic(new ImageView(new Image("cart.png")));
                    btnCart.setText("My Cart");
                    btnCart.setOnAction(event -> setContentFromFXML("MyCartPage.fxml"));
                    addTopNavButton(btnCart);

                    addSideNavButton(buttonBrowseCatalog);

                    Button buttonMyOrders = new Button("My Orders");
                    buttonMyOrders.setOnAction(event -> setContentFromFXML("MyOrdersPage.fxml"));
                    addSideNavButton(buttonMyOrders);
                    break;
                case BRANCH_EMPLOYEE:
                    addSideNavButton(buttonManageProducts);
                    addSideNavButton(buttonSurveyAnswers);
                    break;
                case BRANCH_MANAGER:
                    addSideNavButton(buttonAddItem);
                    addSideNavButton(buttonManageItems);
                    addSideNavButton(buttonAddProduct);
                    addSideNavButton(buttonManageProducts);
                    addSideNavButton(buttonSurveyAnswers);
                    break;
                case SERVICE_EMPLOYEE:
                    Button buttonMyComplaints = new Button("View Complaints");
                    buttonMyComplaints.setOnAction(event ->setContentFromFXML("MyComplaintsPage.fxml"));
                    addSideNavButton(buttonMyComplaints);
                    break;
                case EXPERT_SERVICE_EMPLOYEE:
                    Button buttonSurveyDataView = new Button("View Survey Data");
                    buttonSurveyDataView.setOnAction(event -> setContentFromFXML("SurveySelectPage.fxml"));
                    addSideNavButton(buttonSurveyDataView);

                    Button buttonSurveySummaryUpload = new Button("Upload Survey Summary");
                    buttonSurveySummaryUpload.setOnAction(event -> setContentFromFXML("SurveySummaryUploadPage.fxml"));
                    addSideNavButton(buttonSurveySummaryUpload);
                    break;
                case DELIVERY_OPERATOR:
                    break;
                case CEO:
                    Button buttonAlerts = new Button("Alerts Test");
                    buttonAlerts.setOnAction(event -> setContentFromFXML("AlertTest.fxml"));
                    addSideNavButton(buttonAlerts);

                    addSideNavButton(buttonAddItem);
                    addSideNavButton(buttonManageItems);
                    addSideNavButton(buttonAddProduct);
                    addSideNavButton(buttonManageProducts);

                    Button buttonViewReports = new Button("View Reports");
                    buttonViewReports.setOnAction(event -> setContentFromFXML("MonthlyReportPageCEO.fxml"));
                    addSideNavButton(buttonViewReports);
            }
        }

        /* All user types see the relevant login/out button */
        btnLogInOrOut = new Button();
        btnLogInOrOut.setLayoutX(933.0);
        btnLogInOrOut.setPrefWidth(80.0);

        refreshLoginButton();
        addTopNavButton(btnLogInOrOut);
    }

    public static void refreshLoginButton() {
        if(Client.userController.getLoggedInUser() == null) {
            btnLogInOrOut.setText("Login");
            btnLogInOrOut.getStyleClass().remove("btn-red");
            btnLogInOrOut.getStyleClass().add("btn");
            btnLogInOrOut.setOnAction(event -> setContentFromFXML("LoginPage.fxml"));
        } else {
            btnLogInOrOut.setText("Logout");
            btnLogInOrOut.getStyleClass().remove("btn");
            btnLogInOrOut.getStyleClass().add("btn-red");
            btnLogInOrOut.setOnAction(event -> {
                Client.userController.logout(Client.userController.getLoggedInUser().getUserId());
                setContentFromFXML("BrowseCatalogPage.fxml");
                buildUserNavigation();
                refreshLoginButton();
            });
        }
    }

    public static void refreshCartCounter() {
        String count = Client.orderController.getCart().size() > 0 ? " (" + Client.orderController.getCart().size() + ")" : "";
        btnCart.setText("My Cart" + count);
    }

    private static void addTopNavButton(Button button) {
        button.setLayoutY(10.0);
        topNavigationBox.getChildren().add(button);
    }

    public static void addSideNavButton(Button button) {
        button.getStyleClass().add("sidenav-button");
        button.setPrefWidth(200.0);
        sideNavigationBox.getChildren().add(button);
    }

    public static void setContentFromFXML(String fxml) {
        Node node = null;
        try {
            node = (Node) FXMLLoader.load(MainDashboardController.class.getResource(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getContentBox().getChildren().setAll(node);
    }

    public static AnchorPane getContentBox(){
        return MainDashboardController.contentBox;
    }
}

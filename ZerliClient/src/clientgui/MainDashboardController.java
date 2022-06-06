package clientgui;

import client.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import user.BranchEmployee;
import user.Customer;
import user.UserType;
import util.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainDashboardController implements Initializable {
    public static Pane topNavigationBox;
    public static Button btnLogInOrOut;
    public static Button btnCart;
    public static Label lblCurrentBalance;
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
        /* Clear all previously added buttons except the logo */
        sideNavigationBox.getChildren().remove(1, (int)sideNavigationBox.getChildren().stream().count());
        /* Clear all previously added buttons */
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
                    lblCurrentBalance = new Label("Balance: " + ((Customer) Client.userController.getLoggedInUser()).getBalance() + " \u20AA");
                    lblCurrentBalance.setLayoutX(700.0);
                    lblCurrentBalance.setPadding(new Insets(10, 5, 0, 0));
                    lblCurrentBalance.getStyleClass().add("balance-btn");
                    addTopNavLabel(lblCurrentBalance);

                    btnCart = new Button();
                    btnCart.setLayoutX(780.0);
                    btnCart.setMinWidth(150.0);
                    btnCart.getStyleClass().add("cart-btn");
                    btnCart.setGraphic(new ImageView(new Image("cart.png")));
                    btnCart.setText("My Cart");
                    btnCart.setOnAction(event -> setContentFromFXML("MyCartPage.fxml"));
                    btnCart.setCursor(Cursor.HAND);
                    addTopNavButton(btnCart);

                    addSideNavButton(buttonBrowseCatalog);

                    Button buttonMyOrders = new Button("My Orders");
                    buttonMyOrders.setOnAction(event -> setContentFromFXML("MyOrdersPage.fxml"));
                    addSideNavButton(buttonMyOrders);

                    Button buttonProductBuilder = new Button("Product Builder");
                    buttonProductBuilder.setOnAction(event -> setContentFromFXML("CustomerProductBuilderPage.fxml"));

                    if(((Customer) Client.userController.getLoggedInUser()).isBlocked())
                        buttonProductBuilder.setDisable(true);

                    addSideNavButton(buttonProductBuilder);
                    break;
                case BRANCH_EMPLOYEE:
                    addSideNavButton(buttonManageProducts);
                    addSideNavButton(buttonSurveyAnswers);
                    break;
                case BRANCH_MANAGER:
                    Button buttonManageOrders = new Button("Manage Orders");
                    buttonManageOrders.setOnAction(event -> setContentFromFXML("OrderManagerPage.fxml"));

                    Button buttonUserManagement = new Button("User Management");
                    buttonUserManagement.setOnAction(event -> setContentFromFXML("BranchManagerCustomerManagementPage.fxml"));

                    Button buttonViewReport = new Button("View Reports");
                    buttonViewReport.setOnAction(event -> setContentFromFXML("ReportPageBranchManager.fxml"));

                    addSideNavButton(buttonViewReport);
                    addSideNavButton(buttonUserManagement);
                    addSideNavButton(buttonManageOrders);
                    addSideNavButton(buttonSurveyAnswers);

                    addSideNavButton(buttonAddProduct);
                    addSideNavButton(buttonManageProducts);
                    addSideNavButton(buttonAddItem);
                    addSideNavButton(buttonManageItems);
                    break;
                case SERVICE_EMPLOYEE:
                    Button buttonAddComplaint = new Button("Add Complaint");
                    buttonAddComplaint.setOnAction(event -> setContentFromFXML("ComplaintPage.fxml"));
                    addSideNavButton(buttonAddComplaint);

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
                    Button buttonMyDeliveries = new Button("Available Deliveries");
                    buttonMyDeliveries.setOnAction(event -> setContentFromFXML("MyDeliveriesPage.fxml"));
                    addSideNavButton(buttonMyDeliveries);

                    Button buttonMyHistory = new Button("Delivery History");
                    buttonMyHistory.setOnAction(event -> setContentFromFXML("MyDeliveryHistoryPage.fxml"));
                    addSideNavButton(buttonMyHistory);
                    break;
                case CEO:
                    Button buttonViewReports = new Button("View Reports");
                    buttonViewReports.setOnAction(event -> setContentFromFXML("ReportPageCEO.fxml"));
                    addSideNavButton(buttonViewReports);
                    addSideNavButton(buttonAddProduct);
                    addSideNavButton(buttonManageProducts);
                    addSideNavButton(buttonAddItem);
                    addSideNavButton(buttonManageItems);
            }
        }

        /* All user types see the relevant login/out button */
        btnLogInOrOut = new Button();
        btnLogInOrOut.setLayoutX(933.0);
        btnLogInOrOut.setPrefWidth(80.0);
        btnLogInOrOut.setCursor(Cursor.HAND);

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
    public static void refreshBalanceLabel() {
        String balance = "Balance: " + ((Customer) Client.userController.getLoggedInUser()).getBalance() + " \u20AA";
        lblCurrentBalance.setText(balance);
    }

    private static void addTopNavButton(Button button) {
        button.setLayoutY(10.0);
        topNavigationBox.getChildren().add(button);
    }
    private static void addTopNavLabel(Label label) {
        label.setLayoutY(10.0);
        topNavigationBox.getChildren().add(label);
    }

    public static void addSideNavButton(Button button) {
        button.getStyleClass().add("sidenav-button");
        button.setPrefWidth(200.0);
        sideNavigationBox.getChildren().add(button);
    }

    public static void setContentFromFXML(String fxml) {
        if(fxml == "ProductsManagePage.fxml" || fxml =="SurveyAnswerSubmitPage.fxml") {
            if (Client.userController.getLoggedInUser().getUserType() == UserType.BRANCH_EMPLOYEE && !isUserHavePermission(fxml)) {
                createAlert("You are not authorized to perform this action", Alert.DANGER,Duration.seconds(3),200,0);
                return;
            }
        }

        Node node = null;
        try {
            node = FXMLLoader.load(MainDashboardController.class.getResource(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getContentBox().getChildren().setAll(node);
    }

    private static boolean isUserHavePermission(String fxml)
    {
        Client.userController.getPermissions(Client.userController.getLoggedInUser());
        BranchEmployee branchEmployee;
        switch (fxml)
        {
            case "ProductsManagePage.fxml":
                branchEmployee = Client.userController.getBranchEmployeeForInformation();
                if(branchEmployee.isCatalogue())
                    return true;
                break;
            case "SurveyAnswerSubmitPage.fxml":
                branchEmployee = Client.userController.getBranchEmployeeForInformation();
                if(branchEmployee.isSurvey())
                    return true;
                break;
        }
        return false;
    }

    public static AnchorPane getContentBox(){
        return MainDashboardController.contentBox;
    }
}

package clientgui;

import client.Client;
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
import user.UserType;

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

    public static void buildUserNavigation() {
        /* Clear all previously add buttons except the logo */
        sideNavigationBox.getChildren().remove(1, (int)sideNavigationBox.getChildren().stream().count());
        /* Clear all previously add buttons */
        topNavigationBox.getChildren().remove(0, (int)topNavigationBox.getChildren().stream().count());

        Button buttonBrowseCatalog = new Button("Browse Catalog");
        buttonBrowseCatalog.setOnAction(event -> setContentFromFXML("BrowseCatalogPage.fxml"));

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
                    break;
                case BRANCH_MANAGER:
                    break;
                case SERVICE_EMPLOYEE:
                    break;
                case EXPERT_SERVICE_EMPLOYEE:
                    break;
                case DELIVERY_OPERATOR:
                    break;
                case CEO:
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
        MainDashboardController.getContentBox().getChildren().setAll(node);
    }

    public static AnchorPane getContentBox(){
        return MainDashboardController.contentBox;
    }
}

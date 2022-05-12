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
        buildCustomerNavigation();
    }

    public static void buildCustomerNavigation() {
        /* Clear all previously add buttons except the logo */
        sideNavigationBox.getChildren().remove(1, (int)sideNavigationBox.getChildren().stream().count());
        /* Clear all previously add buttons */
        topNavigationBox.getChildren().remove(0, (int)topNavigationBox.getChildren().stream().count());

        Button buttonBrowseCatalog = new Button("Browse Catalog");
        buttonBrowseCatalog.setOnAction(event -> buttonLoadFXMLOnAction("BrowseCatalogPage.fxml"));
        addSideNavButton(buttonBrowseCatalog);

        btnLogInOrOut = new Button();
        btnLogInOrOut.setLayoutX(933.0);
        btnLogInOrOut.setPrefWidth(80.0);
        swapToLoginButton(true);
        addTopNavButton(btnLogInOrOut);

        /* If used is logged in as Customer, adds all relevant buttons */
        if(Client.userController.getLoggedInUser() != null && Client.userController.getLoggedInUser().getUserType() == UserType.CUSTOMER) {
            Button buttonMyOrders = new Button("My Orders");
            buttonMyOrders.setOnAction(event -> buttonLoadFXMLOnAction("MyOrdersPage.fxml"));
            addSideNavButton(buttonMyOrders);

            btnCart = new Button();
            btnCart.setLayoutX(770.0);
            btnCart.setMinWidth(150.0);
            btnCart.getStyleClass().add("cart-btn");
            btnCart.setGraphic(new ImageView(new Image("cart.png")));
            btnCart.setText("My Cart");
            btnCart.setOnAction(event -> buttonLoadFXMLOnAction("MyCartPage.fxml"));
            addTopNavButton(btnCart);
        }
    }

    public static void swapToLoginButton(boolean swap) {
        if(swap) {
            btnLogInOrOut.setText("Login");
            btnLogInOrOut.getStyleClass().remove("btn-red");
            btnLogInOrOut.getStyleClass().add("btn");
            btnLogInOrOut.setOnAction(event -> buttonLoadFXMLOnAction("LoginPage.fxml"));
        } else {
            btnLogInOrOut.setText("Logout");
            btnLogInOrOut.getStyleClass().remove("btn");
            btnLogInOrOut.getStyleClass().add("btn-red");
            btnLogInOrOut.setOnAction(event -> {
                Client.userController.logout(Client.userController.getLoggedInUser().getUserId());
                buttonLoadFXMLOnAction("BrowseCatalogPage.fxml");
                swapToLoginButton(true);
                buildCustomerNavigation();
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

    public static void buttonLoadFXMLOnAction(String fxml) {
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

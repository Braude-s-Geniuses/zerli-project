package clientgui;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomePageClientController {

    @FXML
    private Label CustomerNameLabel;
    @FXML
    private Button btnBrowseCatalog;
    @FXML
    private Button btnBrowseOrders;
    @FXML
    private Button btnViewCart;
    @FXML
    private AnchorPane picturePane;

    /**
     * Browse catalog page
     * @param event
     */
    @FXML
    void clickBtnBrowseCatalog(ActionEvent event) {
        //TODO Combine
    }

    /**
     * View Customer`s orders
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnBrowseOrders(ActionEvent event) throws IOException {
        Client.setScene(event, getClass().getResource("OrdersPage.fxml"));
    }
    /**
     * View Customer`s cart
     * @param event
     * @throws IOException
     */
    @FXML
   void clickBtnViewCart(ActionEvent event) throws IOException {
       Client.setScene(event, getClass().getResource("CartPage.fxml"));
   }
}

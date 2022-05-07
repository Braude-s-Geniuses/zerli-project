package clientgui;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CompletedOrderPageController implements Initializable {

    @FXML
    private Button btnBrowseCatalog;

    @FXML
    private Button btnBrowseOrders;

    @FXML
    private Button btnViewCart;
    @FXML
    private Button btnContinueShopping;
    @FXML
    private Label lblOrderId;

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * <tt>null</tt> if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or <tt>null</tt> if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        lblOrderId.setText(String.valueOf((int)Client.orderController.getResponse().getData()));
        lblOrderId.setUnderline(true);
    }

    /**
     *  GOTO Catalog page
     * @param event
     */
    @FXML
    void clickBtnBrowseCatalog(ActionEvent event) {

    }
    /** View customer`s orders
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnBrowseOrders(ActionEvent event) throws IOException {
        Client.setScene(event, getClass().getResource("OrdersPage.fxml"));
    }
    /** View customer`s cart
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnViewCart(ActionEvent event) throws IOException {
        Client.setScene(event, getClass().getResource("CartPage.fxml"));
    }

    @FXML
    void clickBtnContinueShopping(ActionEvent event) throws IOException {
        //TODO as browse catalog
    }

}

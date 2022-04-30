package clientgui;

import client.OrderClientController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import order.Product;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CartPageController implements Initializable {

    @FXML
    private TableColumn<Product, Integer> amountColumn;

    @FXML
    private Button btnBrowseCatalog;

    @FXML
    private Button btnBrowseOrders;

    @FXML
    private Button btnViewCart;

    @FXML
    private TableView<Product> cartTable;

    @FXML
    private TableColumn<Product, String> colorColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Integer> priceColumn;

    @FXML
    void clickBtnBrowseOrders(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        OrderClientController orderController = new OrderClientController();
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("amount"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("color"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("price"));


        ArrayList<Product> result = orderController.getCart();

        if(result != null) {
            ObservableList<Product> orders = FXCollections.observableArrayList(result);
            cartTable.setItems(orders);
        }
    }


}

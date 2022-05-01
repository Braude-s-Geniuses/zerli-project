package clientgui;

import client.Client;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import order.OrderProduct;
import order.Product;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CartPageController implements Initializable {


    @FXML
    private Button btnBrowseCatalog;

    @FXML
    private Button btnBrowseOrders;

    @FXML
    private Button btnViewCart;

    @FXML
    private TableColumn<OrderProduct, String> nameColumn;

    @FXML
    private TableColumn<OrderProduct, Integer> amountColumn;

    @FXML
    private TableColumn<OrderProduct, String> colorColumn;

    @FXML
    private TableColumn<OrderProduct, String> customMadeColumn;

    @FXML
    private TableColumn<OrderProduct, String> priceColumn;

    @FXML
    private TableView<OrderProduct> cartTable;

    @FXML
    void clickBtnBrowseOrders(ActionEvent event) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("hii");
        amountColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct, Integer>("quantity"));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        colorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getDominantColor()));
        customMadeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().customMadeToString()));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduct().getProductPrice())));

        ArrayList<OrderProduct> result = Client.orderController.getCart();
        System.out.println("result" + result.toString());
        if (result != null) {
            ObservableList<OrderProduct> itemsInCart = FXCollections.observableArrayList(result);
            cartTable.setItems(itemsInCart);

        }
    }
}


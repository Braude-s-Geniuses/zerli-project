package clientgui;

import client.Client;
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
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<OrderProduct, Integer> amountColumn;

    @FXML
    private TableColumn<Product, String> colorColumn;

    @FXML
    private TableColumn<Product, Integer> priceColumn;

    @FXML
    private TableView<OrderProduct> cartTable;

    @FXML
    void clickBtnBrowseOrders(ActionEvent event) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<OrderProduct> result = Client.orderController.getCart();
        ArrayList<Product> products = new ArrayList<>();
        for (OrderProduct p:  result){
             products.add(p.getProduct());

        }
        amountColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct, Integer>("quantity"));



        if(result != null) {

            ObservableList<OrderProduct> itemsInCart = FXCollections.observableArrayList(result);
            cartTable.setItems(itemsInCart);
        }
        nameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        colorColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("dominantColor"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("price"));


        if(products != null) {

            ObservableList<Product> itemsInCart = FXCollections.observableArrayList(products);
            cartTable.setItems(itemsInCart);
        }

    }
}

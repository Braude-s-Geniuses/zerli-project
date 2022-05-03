package clientgui;

import client.Client;
import communication.Message;
import communication.MessageFromServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import order.Order;
import order.OrderStatus;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrdersPageController implements Initializable {

    @FXML
    private Button btnBrowseCatalog;

    @FXML
    private Button btnBrowseOrders;

    @FXML
    private Button btnViewCart;

    @FXML
    private TableView<Order> ordersTable;

    @FXML
    private TableColumn<Order, Integer> orderIdColumn;

    @FXML
    private TableColumn<Order, String> branchColumn;

    @FXML
    private TableColumn<Order, Float> priceColumn;

    @FXML
    private TableColumn<Order, Timestamp> dateTimeColumn;

    @FXML
    private TableColumn<Order, OrderStatus> statusColumn;

    @FXML
    private Button btsShowOrderDetails;

    @FXML
    void clickBtnBrowseOrders(ActionEvent event) {
    }

    @FXML       //Omer
    void clickBtnViewCart(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("CartPage.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli Client");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String s = MessageFromServer.IMPORT_ORDERS_TABLE_NOT_SUCCEED.toString();
        System.out.println(s);
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderId"));
        branchColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("branch"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Order, Float>("discountPrice"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("orderDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Order, OrderStatus>("orderStatus"));

        ArrayList<Order> result = Client.orderController.requestOrders();
         if (result != null) {
            ObservableList<Order> orders = FXCollections.observableArrayList(result);
             ordersTable.setItems(orders);

        }
    }

}

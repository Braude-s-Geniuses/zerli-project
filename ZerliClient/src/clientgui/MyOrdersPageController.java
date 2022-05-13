package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import order.Order;
import order.OrderStatus;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyOrdersPageController implements Initializable {
    @FXML
    private AnchorPane baseAnchor;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderId"));
        branchColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("branch"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Order, Float>("discountPrice"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<Order, Timestamp>("orderDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Order, OrderStatus>("orderStatus"));
        Client.orderController.requestOrders();
        ArrayList<Order> result = (ArrayList<Order>) Client.orderController.getResponse().getData();
        if (result != null) {
            ObservableList<Order> orders = FXCollections.observableArrayList(result);
            ordersTable.setItems(orders);

        }
    }

}

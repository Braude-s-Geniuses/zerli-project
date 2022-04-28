package clientgui;

import client.Client;
import common.Order;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ViewOrdersTableController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnBrowseOrders;

    @FXML
    private Button btnModifyOrder;

    @FXML
    private TableView<Order> tableOrders;

    @FXML
    private TableColumn<Order, Integer> columnOrderNumber;

    @FXML
    private TableColumn<Order, Integer> columnPrice;

    @FXML
    private TableColumn<Order, String> columnGreetingCard;

    @FXML
    private TableColumn<Order, String> columnColor;

    @FXML
    private TableColumn<Order, String> columnOrderDescription;

    @FXML
    private TableColumn<Order, String> columnShopName;

    @FXML
    private TableColumn<Order, String> columnETA;

    @FXML
    private TableColumn<Order, String> columnOrderDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnOrderNumber.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderNumber"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Order, Integer>("price"));
        columnGreetingCard.setCellValueFactory(new PropertyValueFactory<Order, String>("greetingCardString"));
        columnColor.setCellValueFactory(new PropertyValueFactory<Order, String>("color"));
        columnOrderDescription.setCellValueFactory(new PropertyValueFactory<Order, String>("dOrder"));
        columnShopName.setCellValueFactory(new PropertyValueFactory<Order, String>("shop"));
        columnETA.setCellValueFactory(new PropertyValueFactory<Order, String>("deliveryDate"));
        columnOrderDate.setCellValueFactory(new PropertyValueFactory<Order, String>("orderDate"));

        ArrayList<Order> result = Client.clientController.requestOrders();

        if(result != null) {
            ObservableList<Order> orders = FXCollections.observableArrayList(result);
            tableOrders.setItems(orders);
        }
    }

    public void clickBtnBrowseOrders(ActionEvent event) throws Exception {
        ((Node) event.getSource()).getScene().getWindow().hide();

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("ViewOrdersTable.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli Client");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Client.clientController.attachExitEventToStage(primaryStage);
    }

    public void clickBtnModifyOrder(ActionEvent event) throws Exception {
        if (tableOrders.getSelectionModel().getSelectedItem() != null) {
            Order selectedOrder = tableOrders.getSelectionModel().getSelectedItem();

            ((Node) event.getSource()).getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UpdateOrderForm.fxml"));
            Parent root = (Parent) loader.load();

            Stage primaryStage = new Stage();
            Scene scene = new Scene(root);

            UpdateOrderFormController updateOrderFormController = loader.getController();
            updateOrderFormController.setLblOrderNumber(selectedOrder.getOrderNumber());


            primaryStage.setTitle("Zerli Client");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            Client.clientController.attachExitEventToStage(primaryStage);
        }
    }

}

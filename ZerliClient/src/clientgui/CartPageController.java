package clientgui;

import client.Client;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import order.OrderProduct;
import order.Product;

import java.io.IOException;
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
    void clickBtnBrowseOrders(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("OrdersPage.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli Client");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        amountColumn.setCellValueFactory(new PropertyValueFactory<OrderProduct, Integer>("quantity"));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName()));
        colorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getDominantColor()));
        customMadeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProduct().customMadeToString()));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getProduct().getProductPrice())));

        ArrayList<OrderProduct> result = Client.orderController.getCart();

        if (result != null) {
            ObservableList<OrderProduct> itemsInCart = FXCollections.observableArrayList(result);

            cartTable.setItems(itemsInCart);

        }
    }


}


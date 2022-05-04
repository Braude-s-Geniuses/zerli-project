package clientgui;

import client.Client;

import client.OrderController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Stage;;
import order.Order;
import order.OrderProduct;
import javafx.scene.control.ListView;
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
    private Button btnCheckOut;

    ObservableList<String> quantityPicker =
            FXCollections.observableArrayList("0", "1", "2", "3","4","5","6","7","8","9","10");


    @FXML
    private ListView<Object> cartAsListView;

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

    private ArrayList<ComboBox> comboBoxQuantityArray = new ArrayList<>();

    EventHandler<ActionEvent> handler;


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
    @FXML
    void clickBtnBrowseCatalog(ActionEvent event) {

    }
    @FXML
    void clickBtnCheckOut(ActionEvent event) {

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
       //if( Client.orderController.getCart().size() == 0 )  {btnCheckOut.setDisable(true);};

        btnViewCart.getStyleClass().add("sidenav-button-active");

        ArrayList<OrderProduct> arrivedList = Client.orderController.getCart();
        initHandler();
        for (OrderProduct op : arrivedList) {
            Label NameLabel = new Label(op.getProduct().getName(), null);
            Label colorLabel = new Label(op.getProduct().getDominantColor(), null);
            Label customMadeLabel = new Label(op.getProduct().customMadeToString(), null);
           // Label priceLabel = new Label(String.valueOf(op.getProduct().getProductPrice()), null);
            Label priceLabel = new Label(op.getProduct().priceToString(), null);

            Image img = new Image("/../ZerliCommon/images.png");
            ImageView view = new ImageView(img);
            view.setFitHeight(80);
            view.setPreserveRatio(true);
            NameLabel.setGraphic(view);

            NameLabel.setFont(new Font(18));
            NameLabel.setPrefWidth(100);
            NameLabel.setAlignment(Pos.BASELINE_CENTER);
            NameLabel.setStyle("-fx-text-fill: #77385a");
            colorLabel.setFont(new Font(18));
            colorLabel.setPrefWidth(100);
            colorLabel.setAlignment(Pos.BASELINE_CENTER);
            colorLabel.setStyle("-fx-text-fill: #77385a");
            customMadeLabel.setFont(new Font(18));
            customMadeLabel.setPrefWidth(100);
            customMadeLabel.setAlignment(Pos.BASELINE_CENTER);
            customMadeLabel.setStyle("-fx-text-fill: #77385a");
            priceLabel.setFont(new Font(18));
            priceLabel.setPrefWidth(100);
            priceLabel.setAlignment(Pos.BASELINE_CENTER);
            priceLabel.setStyle("-fx-text-fill: #77385a");
            ComboBox<String> comboBoxQuantity = new ComboBox<>(quantityPicker);
            comboBoxQuantity.setValue(quantityPicker.get(3).toString());
            comboBoxQuantity.setOnAction(handler);
            comboBoxQuantity.getSelectionModel().select(op.getQuantity());
            comboBoxQuantity.setBackground(Background.EMPTY);
            HBox h = new HBox( 110,NameLabel,colorLabel, comboBoxQuantity, customMadeLabel, priceLabel);
            //h.setPrefWidth(987);
            HBox.setHgrow(NameLabel, Priority.max(Priority.ALWAYS, Priority.ALWAYS));
            h.setAlignment(Pos.BASELINE_CENTER);
            cartAsListView.getItems().addAll(h);
            comboBoxQuantityArray.add(comboBoxQuantity);
        }

    }

    public void initHandler(){
         handler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               ComboBox comboBox = (ComboBox)event.getSource();
               int rownumber = comboBoxQuantityArray.indexOf(comboBox);     //Get row number.
                //System.out.println("test for handler" + rownumber);
                cartAsListView.getSelectionModel().select(rownumber);   //mark row
                HBox h = (HBox) cartAsListView.getSelectionModel().getSelectedItem();   //get Hbox
                Label l = (Label) h.getChildren().get(0);       //Get name column
                String productName = l.getText();               //Get product name
                String  i = (String) comboBox.getValue();       //get comboBox value.
                int newAmount = Integer.parseInt(i);
                System.out.println( "amount" + newAmount);
                if(newAmount == 0){
                    cartAsListView.getItems().remove(h);    //TODO if cart is empty
                }
                if (Client.orderController.changeAmountOfProduct(productName, newAmount)){
                    btnCheckOut.setDisable(true);
                }

            }
        };
    }
}


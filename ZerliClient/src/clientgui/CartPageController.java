package clientgui;

import client.Client;

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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CartPageController implements Initializable {
    ObservableList<String> quantityPicker = FXCollections.observableArrayList("0", "1", "2", "3","4","5","6","7","8","9","10");
    @FXML
    private Button btnBrowseCatalog;
    @FXML
    private Button btnBrowseOrders;
    @FXML
    private Button btnViewCart;
    @FXML
    private Button btnCheckOut;
    @FXML
    private Label totalLabel;
    @FXML
    private ListView<Object> cartAsListView;
    private ArrayList<ComboBox> comboBoxQuantityArray = new ArrayList<>();
    private EventHandler<ActionEvent> handler;


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
    public void initialize(URL location, ResourceBundle resources) {
        btnViewCart.getStyleClass().add("sidenav-button-active");
        ArrayList<OrderProduct> arrivedList = Client.orderController.getCart();
        initHandler();
        for (OrderProduct op : arrivedList) {

            Label nameLabel = new Label(op.getProduct().getName() + "\n"  + op.getProduct().getDominantColor() + "\n" + op.getProduct().customMadeToString(), null);
            Label priceLabel = new Label(String.valueOf(op.getQuantity()* op.getProduct().getProductPrice()) + " \u20AA");
            Label discountPriceLabel = new Label(String.valueOf(op.getQuantity()* op.getProduct().getDiscountPrice()) + " \u20AA");
            if(op.getProduct().getProductPrice() > op.getProduct().getDiscountPrice()){
                priceLabel.getStyleClass().add("order-label");
                discountPriceLabel.setStyle("-fx-text-fill: red");
                discountPriceLabel.setFont(new Font(18));
                discountPriceLabel.setPrefWidth(80);
            }
            else {
                discountPriceLabel.setVisible(false);
            }
            Image img = new Image("/../ZerliCommon/images.png");
            ImageView view = new ImageView(img);
            view.setFitHeight(80);
            view.setPreserveRatio(true);
            nameLabel.setGraphic(view);

            nameLabel.setFont(new Font(18));
            nameLabel.setAlignment(Pos.CENTER_LEFT);
            nameLabel.setStyle("-fx-text-fill: #77385a");
            nameLabel.setPrefWidth(300);
            nameLabel.setWrapText(true);

            priceLabel.setFont(new Font(18));
            priceLabel.setPrefWidth(80);
            priceLabel.setStyle("-fx-text-fill: #77385a");

            ComboBox<String> comboBoxQuantity = new ComboBox<>(quantityPicker);
            comboBoxQuantity.setOnAction(handler);
            comboBoxQuantity.getSelectionModel().select(op.getQuantity());
            comboBoxQuantity.setBackground(Background.EMPTY);

            HBox h = new HBox( 30, nameLabel, comboBoxQuantity, priceLabel, discountPriceLabel);
            HBox.setHgrow(nameLabel, Priority.max(Priority.ALWAYS, Priority.ALWAYS));
            h.setAlignment(Pos.CENTER_LEFT);
            //h.setAlignment(Pos.CENTER);
            cartAsListView.getItems().addAll(h);
            comboBoxQuantityArray.add(comboBoxQuantity);

        }
        totalLabel.setText(totalLabel.getText() + " " + Client.orderController.sumOfCart() + " \u20AA");

    }

    /**
     * Change the amount of product in cart
     */
    public void initHandler(){
         handler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               ComboBox comboBox = (ComboBox)event.getSource();
               int rowNumber = comboBoxQuantityArray.indexOf(comboBox);
                cartAsListView.getSelectionModel().select(rowNumber);
                HBox h = (HBox) cartAsListView.getSelectionModel().getSelectedItem();
                Label l = (Label) h.getChildren().get(0);
                String productName = l.getText();
                String  i = (String) comboBox.getValue();
                int newAmount = Integer.parseInt(i);
                if(newAmount == 0){
                    cartAsListView.getItems().remove(h);
                }
                if (Client.orderController.changeAmountOfProduct(productName, newAmount)){//if cart is empty
                    btnCheckOut.setDisable(true);
                }

            }
        };
    }
    /**
     * starting place order process, passing into delivery page
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnCheckOut(ActionEvent event) throws IOException {
        Client.setScene(event, getClass().getResource("DeliveryPage.fxml"));
    }
    /**
     * View customer`s orders
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnBrowseOrders(ActionEvent event) throws IOException {
        initCurrentOrder();
        Client.setScene(event, getClass().getResource("OrdersPage.fxml"));
    }

    /**
     * Browse catalog page
     * @param event
     */
    @FXML
    void clickBtnBrowseCatalog(ActionEvent event) {
        initCurrentOrder();
        //TODO combine.
    }
    /**
     * Set order object in order controller to a new Order.
     */
    private void initCurrentOrder() {//TODO
        Client.orderController.setCurrentOrder(new Order());
    }
}


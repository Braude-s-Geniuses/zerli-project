package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import order.Order;
import order.OrderProduct;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyCartPageController implements Initializable {
    ObservableList<String> quantityPicker = FXCollections.observableArrayList("0", "1", "2", "3","4","5","6","7","8","9","10");

    @FXML
    private Button btnCheckOut;
    @FXML
    private Label totalLabel;
    @FXML
    private ListView<Object> cartAsListView;
    private final ArrayList<ComboBox> comboBoxQuantityArray = new ArrayList<>();
    private EventHandler<ActionEvent> handler;

    private float totalPrice;

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
        ArrayList<OrderProduct> arrivedList = Client.orderController.getCart();
        cartAsListView.setFocusTraversable( false );

        btnCheckOut.setDisable(arrivedList.isEmpty());

        initHandler();
        for (OrderProduct op : arrivedList) {
            ImageView iv = new ImageView();
            iv.setFitHeight(150);
            iv.setFitWidth(130);
            Client.productController.createProductImage(op.getProduct());
            iv.setImage(Client.productController.getProductImages().get(op.getProduct().getProductId()));
            Label imageLabel = new Label(null,iv);
            Label nameLabel = new Label(op.getProduct().getName() + "\n"  + op.getProduct().getDominantColor() + "\n" + op.getProduct().customMadeToString());
            Label priceLabel = new Label(op.getQuantity() * op.getProduct().getPrice() + " \u20AA");
            Label discountPriceLabel = new Label(op.getQuantity() * op.getProduct().getDiscountPrice() + " \u20AA");
            if(op.getProduct().getPrice() > op.getProduct().getDiscountPrice()){
                priceLabel.getStyleClass().add("order-label");
                discountPriceLabel.setStyle("-fx-text-fill: red");
                discountPriceLabel.setFont(new Font(18));
                discountPriceLabel.setPrefWidth(80);
            }
            else {
                discountPriceLabel.setVisible(false);
            }
            nameLabel.setFont(new Font(18));
            nameLabel.setAlignment(Pos.CENTER_LEFT);
            nameLabel.setStyle("-fx-text-fill: #77385a");
            nameLabel.setPrefWidth(200);
            nameLabel.setWrapText(true);

            priceLabel.setFont(new Font(18));
            priceLabel.setPrefWidth(80);
            priceLabel.setStyle("-fx-text-fill: #77385a");

            ComboBox<String> comboBoxQuantity = new ComboBox<>(quantityPicker);
            comboBoxQuantity.setOnAction(handler);
            comboBoxQuantity.getSelectionModel().select(op.getQuantity());
            comboBoxQuantity.setBackground(Background.EMPTY);

            HBox h = new HBox( 30, imageLabel,nameLabel, comboBoxQuantity, priceLabel, discountPriceLabel);
            HBox.setHgrow(nameLabel, Priority.max(Priority.ALWAYS, Priority.ALWAYS));
            h.setAlignment(Pos.CENTER_LEFT);
            cartAsListView.getItems().addAll(h);
            comboBoxQuantityArray.add(comboBoxQuantity);

        }
        editTotalLabel();


    }

    private void editTotalLabel() {
        totalPrice = Client.orderController.getOrderPrice(true);
        totalLabel.setText("Total: " + totalPrice + " \u20AA");

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
                HBox productHBox = (HBox) cartAsListView.getSelectionModel().getSelectedItem();
                Label nameLabel = (Label) productHBox.getChildren().get(1); //get name label
                String productName = nameLabel.getText();
                String  productAmount = (String) comboBox.getValue();
                int newAmount = Integer.parseInt(productAmount);
                String[] splitNameLabelString = productName.split("\n");
                OrderProduct op = Client.orderController.getProductByName(splitNameLabelString[0]);
                if(newAmount == 0){
                    cartAsListView.getItems().remove(productHBox);
                    Client.orderController.getCart().remove(op);
                    MainDashboardController.refreshCartCounter();
                    if(Client.orderController.getCart().isEmpty()){
                        btnCheckOut.setDisable(true);
                    }
                } else {
                    Client.orderController.getCart().get( Client.orderController.getCart().indexOf(op)).setQuantity(newAmount);
                    if (op == null) {//if cart is empty
                        return;
                    }
                    ((Label) productHBox.getChildren().get(3)).setText(op.getQuantity() * op.getProduct().getPrice() + " \u20AA");
                    ((Label) productHBox.getChildren().get(4)).setText(op.getQuantity() * op.getProduct().getDiscountPrice() + " \u20AA");
                }

                editTotalLabel();
            }
        };
    }
    /**
     * starting place order process, passing into delivery page
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnCheckOut(ActionEvent event) {
        Client.orderController.setCurrentOrder(new Order());
        Client.orderController.getCurrentOrder().setProductList(Client.orderController.getCart());
        Client.orderController.getCurrentOrder().setDiscountPrice(totalPrice);
        Client.orderController.getCurrentOrder().setPrice(Client.orderController.getOrderPrice(false));
        Client.orderController.getCurrentOrder().setCustomerId(Client.userController.getLoggedInUser().getUserId());
        MainDashboardController.setContentFromFXML("OrderDeliveryPage.fxml");


    }

}

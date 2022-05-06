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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import order.Order;
import order.OrderProduct;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DeliveryPageController implements Initializable {

    @FXML
    private Button btnBrowseCatalog;
    @FXML
    private Button btnBrowseOrders;
    @FXML
    private Button btnViewCart;
    @FXML
    private ListView<Object> cartAsListView;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnProceed;
    @FXML
    private Button btnDeliveryData;
    @FXML
    private Button btnRecipientInfo;

    @FXML
    private Button btnPayment;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label cityLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private ComboBox<String> cbTime;

    @FXML
    private ComboBox<String> cbBranch;

    @FXML
    private RadioButton btnRadioSelfPickup;

    @FXML
    private RadioButton btnRadioDelivery;

    @FXML
    private Label deliveryLabel;

    @FXML
    private ComboBox<?> cbCity;

    @FXML
    private TextField addressField;
    @FXML
    private Label lblDeliveryError;
    @FXML
    private Label lblBranchError;

    @FXML
    private Label lblCityAndAddressError;

    @FXML
    private Label lblDateTimeError;

    @FXML
    private ProgressBar progressBar;


    @FXML
    private Label totalLabel;
    EventHandler<ActionEvent> handler;

    @FXML
    void clickBtnRecipientInfo(ActionEvent event) {

    }
    @FXML
    void clickBtnDeliveryData(ActionEvent event) {

    }

    @FXML
    void clickBtnPayment(ActionEvent event) {

    }
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
    @FXML
    void clickBtnBack(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("CartPage.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli Client");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    @FXML
    void clickBtnProceed(ActionEvent event) throws IOException {
            if(validateInput()){
                Client.orderController.getCurrentOrder().setBranch(cbBranch.getSelectionModel().getSelectedItem());
                if(btnRadioDelivery.isSelected()) {
                    Client.orderController.getCurrentOrder().setDeliveryAddress( cbCity.getSelectionModel().getSelectedItem() + " " + addressField.getText());
                    Client.orderController.getCurrentOrder().setPrice(Client.orderController.sumOfCart()+Client.orderController.DELIVERY_PRICE);
                }
                else{
                    Client.orderController.getCurrentOrder().setPrice(Client.orderController.sumOfCart());
                }
                Timestamp timestamp = Timestamp.valueOf(datePicker.getValue().toString() + " " + cbTime.getSelectionModel().getSelectedItem().toString() + ":00");
                Client.orderController.getCurrentOrder().setDeliveryDate(timestamp);    //setDeliveryDate need to be change? omer
                //System.out.println(Client.orderController.getCurrentOrder());
                ((Node) event.getSource()).getScene().getWindow().hide();
                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("RecipientPage.fxml"));
                Scene scene = new Scene(root);

                primaryStage.setTitle("Zerli Client");
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.show();
            }

    }
    private boolean validateInput(){
        int invalidFields = 0;
        if(cbBranch.getSelectionModel().isEmpty()){
            lblBranchError.setVisible(true);
            invalidFields++;
        }
        if(!btnRadioDelivery.isSelected() && !btnRadioSelfPickup.isSelected()){
            lblDeliveryError.setVisible(true);
            invalidFields++;
        }
        if(btnRadioDelivery.isSelected() && cbCity.getSelectionModel().isEmpty()){
            lblCityAndAddressError.setVisible(true);
            invalidFields++;
        }
        if(btnRadioDelivery.isSelected() && addressField.getText() == null){
            lblCityAndAddressError.setVisible(true);
            invalidFields++;
        }

        if(datePicker.getValue() == null){
            lblDateTimeError.setVisible(true);
            invalidFields++;
        }
        if(cbTime.getSelectionModel().isEmpty()){
            lblDateTimeError.setVisible(true);
            invalidFields++;
        }
        return  invalidFields == 0 ? true: false;
    }
    @FXML
    void clickBtnRadioDelivery(ActionEvent event) {
        btnRadioSelfPickup.setSelected(false);
        cityLabel.setDisable(false);
        addressLabel.setDisable(false);
        cbCity.setDisable(false);
        addressField.setDisable(false);
        deliveryLabel.setText("Delivery: " + Client.orderController.DELIVERY_PRICE + " \u20AA");
        totalLabel.setText("Total: " + (Client.orderController.sumOfCart()+Client.orderController.DELIVERY_PRICE) + " \u20AA");

    }

    @FXML
    void clickBtnRadioSelfPickup(ActionEvent event) {
        btnRadioDelivery.setSelected(false);
        cityLabel.setDisable(true);
        addressLabel.setDisable(true);
        cbCity.setDisable(true);
        addressField.setDisable(true);
        deliveryLabel.setText("Delivery: " + 0.0 + " \u20AA");
        totalLabel.setText("Total: " + Client.orderController.sumOfCart() + " \u20AA");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> timeList = new ArrayList<String>();

        btnViewCart.getStyleClass().add("sidenav-button-active");
        Client.orderController.getBranches();

        /**
         * Add hours to Time ComboBox.
         */
        for (int i = 8; i <= 18; i++) {
            timeList.add(String.valueOf(i) + ":00");
            if (i != 18)
                timeList.add(String.valueOf(i) + ":30");
        }
        LocalDate minDate = LocalDate.now();

        /**
         * Initializes the DatePicker for date selection.
         */
        datePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(minDate) || item.isAfter(minDate.plusMonths(2)));
                    }});


        ObservableList<String> times = FXCollections.observableArrayList(timeList);
        cbTime.getItems().addAll(times);

        ObservableList<String> branches = FXCollections.observableArrayList((ArrayList<String>)Client.orderController.getResponse().getData());
        cbBranch.getItems().addAll(branches);
        ArrayList<OrderProduct> cart = Client.orderController.getCart();
        Client.orderController.setCurrentOrder(new Order());
        initHandler();
        cbCity.setOnAction(handler);
        cbBranch.setOnAction(handler);
        cbTime.setOnAction(handler);


        for (OrderProduct op : cart) {
            Label nameLabel = new Label(op.getProduct().getName() + "\n"  + op.getProduct().getDominantColor() + "\n" + op.getProduct().customMadeToString(), null);
            Label priceLabel = new Label(String.valueOf(op.getQuantity()) + "X " + op.getProduct().priceToString() , null);

            Image img = new Image("/../ZerliCommon/images.png");
            ImageView view = new ImageView(img);
            view.setFitHeight(100);
            view.setPreserveRatio(true);
            nameLabel.setGraphic(view);

            nameLabel.setFont(new Font(14));
            nameLabel.setPrefWidth(260);
            nameLabel.setStyle("-fx-text-fill: #77385a");
            nameLabel.setWrapText(true);

            priceLabel.setFont(new Font(14));
            priceLabel.setPrefWidth(120);
            priceLabel.setStyle("-fx-text-fill: #77385a");
            priceLabel.setWrapText(true);

            HBox h = new HBox(nameLabel, priceLabel);

            HBox.setHgrow(nameLabel, Priority.max(Priority.ALWAYS, Priority.ALWAYS));

            h.setAlignment(Pos.BASELINE_CENTER);
            cartAsListView.getItems().addAll(h);

        }
        totalLabel.setText(totalLabel.getText() + " " + Client.orderController.sumOfCart() + " \u20AA");
        totalLabel.setWrapText(true);

    }
    public void initHandler(){
        handler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ComboBox comboBox = (ComboBox)event.getSource();

            }
        };
    }

}

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
    private Button btnViewCart;
    @FXML
    private ListView<Object> cartAsListView;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label lblCity;
    @FXML
    private Label lblAddress;
    @FXML
    private Label lblDelivery;
    @FXML
    private Label lblDeliveryError;
    @FXML
    private Label lblBranchError;
    @FXML
    private Label lblCityAndAddressError;
    @FXML
    private Label lblDateTimeError;
    @FXML
    private Label lblTotal;
    @FXML
    private ComboBox<String> cbTime;
    @FXML
    private ComboBox<String> cbBranch;
    @FXML
    private ComboBox<String> cbCity;
    @FXML
    private RadioButton btnRadioSelfPickup;
    @FXML
    private RadioButton btnRadioDelivery;
    @FXML
    private TextField addressField;

    EventHandler<ActionEvent> handler;

    /**
     *
     *  @param location
      * The location used to resolve relative paths for the root object, or
      * <tt>null</tt> if the location is not known.
      *
      * @param resources
     * The resources used to localize the root object, or <tt>null</tt> if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(Client.orderController.getCurrentOrder() != null && Client.orderController.getCurrentOrder().getBranch() != null) {
            restoreDeliveryData();
        }
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

        /**
         * Initializes Branches and City comboBox.
         */
        ObservableList<String> branches = FXCollections.observableArrayList((ArrayList<String>)Client.orderController.getResponse().getData());
        cbBranch.getItems().addAll(branches);
        cbCity.getItems().addAll(branches);
        ArrayList<OrderProduct> cart = Client.orderController.getCart();
        Client.orderController.setCurrentOrder(new Order());

        /**
         * For each product order product in the cart - presented in the list
         */
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
        Client.orderController.getCurrentOrder().setPrice(Client.orderController.sumOfCart()) ;
        lblTotal.setText(lblTotal.getText() + " " +   Client.orderController.getCurrentOrder().getPrice() + " \u20AA");
        lblTotal.setWrapText(true);

    }

    /**
     * This method is called if the delivery data was already entered.
     * It restores the delivery data that it gets from the order object in order controller.
     */
    private void restoreDeliveryData() {
            String fullAddress;
            String fullTime = Client.orderController.getCurrentOrder().getDeliveryDate().toString();
            int year = Integer.parseInt(fullTime.substring(0, fullTime.indexOf('-')));
            int month = Integer.parseInt(fullTime.substring(5, 7));
            int day = Integer.parseInt(fullTime.substring(8, fullTime.indexOf(' ')));

            cbBranch.setValue(Client.orderController.getCurrentOrder().getBranch());
            if ((fullAddress = Client.orderController.getCurrentOrder().getDeliveryAddress()) != null) {
                btnRadioDelivery.setSelected(true);
                cbCity.setValue(fullAddress.substring(0, fullAddress.indexOf(' ')));
                addressField.setText(fullAddress.substring(fullAddress.indexOf(' ') + 1, fullAddress.length()));
            }
            else{
                btnRadioSelfPickup.setSelected(true);
                lblCity.setDisable(true);
                lblAddress.setDisable(true);
                cbCity.setDisable(true);
                addressField.setDisable(true);
            }
            datePicker.setValue(LocalDate.of(year, month, day));      //set date
            cbTime.setValue(fullTime.substring(11, 16));             //set time

    }

    /**
     * view order list
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnBrowseOrders(ActionEvent event) throws IOException {
        initCurrentOrder();
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
        //TODO combine.
    }

    /**
     * view Customer's Cart.
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnViewCart(ActionEvent event) throws IOException {
        initCurrentOrder();
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("CartPage.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Zerli Client");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Cancel order and return to cart (CartPage).
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnCancelOrder(ActionEvent event) throws IOException {
        initCurrentOrder();
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("CartPage.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli Client");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Set order object in order controller to a new Order.
     */
    private void initCurrentOrder() {//TODO
        Client.orderController.setCurrentOrder(new Order());
    }

    /**
     * If the user clicked on the delivery button then he will need to entered delivery data.
     * and a delivery price will be added to his total order price.
     * @param event
     */
    @FXML
    void clickBtnRadioDelivery(ActionEvent event) {
        btnRadioSelfPickup.setSelected(false);
        lblCity.setDisable(false);
        lblAddress.setDisable(false);
        cbCity.setDisable(false);
        addressField.setDisable(false);
        lblDelivery.setText("Delivery: " + Client.orderController.DELIVERY_PRICE + " \u20AA");
        lblTotal.setText("Total: " + (Client.orderController.sumOfCart()+Client.orderController.DELIVERY_PRICE) + " \u20AA");

    }

    /**
     * If the user clicked on self pick-up button
     * then he will not need to enter delivery data or pay a delivery fee.
     * @param event
     */
    @FXML
    void clickBtnRadioSelfPickup(ActionEvent event) {
        btnRadioDelivery.setSelected(false);
        lblCity.setDisable(true);
        lblAddress.setDisable(true);
        cbCity.setDisable(true);
        addressField.setDisable(true);
        lblDelivery.setText("Delivery: " + 0.0 + " \u20AA");
        lblTotal.setText("Total: " + Client.orderController.sumOfCart() + " \u20AA");
    }

    /**
     * This method checks if all the data that was entered for the order are correct.
     * if tes, it will proceed to the next page in the order process (RecipientPage).
     * @param event
     * @throws IOException
     */
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

    /**
     * This method checks if all the data that was entered for the order are correct.
     * @return true if yes, else false.
     */
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


}

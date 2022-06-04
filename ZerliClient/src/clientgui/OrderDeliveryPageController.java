package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import order.Order;
import order.OrderProduct;
import order.OrderStatus;
import user.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderDeliveryPageController implements Initializable {
    private List<String> timeList = new ArrayList<String>();
    private ObservableList<String> times;
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
    private Label lblExpressOrderError;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblDateTime;
    @FXML
    private Label lblDiscount;
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
    @FXML
    private RadioButton btnRadioExpressYes;

    @FXML
    private RadioButton btnRadioExpressNo;

    private float discountPrice;
    private float price;

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
        setDeliveryDisable(true);
        setTimeDateDisable(true);
        lblDelivery.setText("Delivery: 0.0 \u20AA");
        lblTotal.setWrapText(true);
        cartAsListView.setFocusTraversable( false );

        /**
         * Update new customer discount
         */
        if(((Customer)Client.userController.getLoggedInUser()).getNewCustomer()){
            lblDiscount.setVisible(true);
            discountPrice = (float) (Client.orderController.getOrderPrice(true) * 0.8);
        }else {
            discountPrice = Client.orderController.getOrderPrice(true);
        }
        price = Client.orderController.getOrderPrice(false);
        if(Client.orderController.getCurrentOrder() != null && Client.orderController.getCurrentOrder().getBranch() != null) {
            restoreDeliveryData();
        }
        else{
            lblTotal.setText("Total: " + discountPrice + " \u20AA");
        }

        Client.orderController.getBranches();

        /**
         * Add hours to Time ComboBox.
         */
        timeList = new ArrayList<String>();
        for (int i = 8; i <= 18; i++) {
            timeList.add(i + ":00");
            if (i != 18)
                timeList.add(i + ":30");
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


        times = FXCollections.observableArrayList(timeList);
        cbTime.getItems().addAll(times);

        /**
         * Initializes Branches and City comboBox.
         */
        ObservableList<String> branches = FXCollections.observableArrayList((ArrayList<String>)Client.orderController.getService().getResponse().getData());
        cbBranch.getItems().addAll(branches);
        cbCity.getItems().addAll(branches);
        ArrayList<OrderProduct> cart = Client.orderController.getCart();

        /**
         * For each product order product in the cart - presented in the list
         */
        for (OrderProduct op : cart) {
            ImageView iv = new ImageView();
            iv.setFitHeight(130);
            iv.setFitWidth(110);
            Client.productController.createProductImage(op.getProduct());
            iv.setImage(Client.productController.getProductImages().get(op.getProduct().getProductId()));
            Label priceLabel;
            Label imageLabel = new Label(null,iv);
            Label nameLabel = new Label(op.getProduct().getName() + "\n"  + op.getProduct().getDominantColor() + "\n" + op.getProduct().customMadeToString(), null);
            priceLabel = new Label(op.getQuantity() + "X " + op.getProduct().discountPriceToString(), null);
            if(op.getProduct().getPrice() == op.getProduct().getDiscountPrice()) {
                priceLabel.setStyle("-fx-text-fill: #77385a");
            }
            else{
                priceLabel.setStyle("-fx-text-fill: red");
            }

            nameLabel.setFont(new Font(14));
            nameLabel.setPrefWidth(150);
            nameLabel.setStyle("-fx-text-fill: #77385a");
            nameLabel.setWrapText(true);

            priceLabel.setFont(new Font(14));
            priceLabel.setPrefWidth(120);
            priceLabel.setWrapText(true);

            HBox h = new HBox(10,imageLabel,nameLabel, priceLabel);
            HBox.setHgrow(nameLabel, Priority.max(Priority.ALWAYS, Priority.ALWAYS));
            h.setAlignment(Pos.CENTER_LEFT);
            cartAsListView.getItems().addAll(h);
        }
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
        datePicker.setValue(LocalDate.of(year, month, day));      //set date
        cbTime.setValue(fullTime.substring(11, 16));             //set time

        cbBranch.setValue(Client.orderController.getCurrentOrder().getBranch());
        cbBranch.fireEvent(new ActionEvent());
        if ((fullAddress = Client.orderController.getCurrentOrder().getDeliveryAddress()) != null) {
            btnRadioDelivery.setSelected(true);
            addressField.setText(fullAddress.substring(Client.orderController.getCurrentOrder().getBranch().length()+1));
            setDeliveryDisable(false);
            lblDelivery.setText("Delivery: " + Client.orderController.DELIVERY_PRICE + " \u20AA");
            lblTotal.setText("Total: " + (discountPrice + Client.orderController.DELIVERY_PRICE) +" \u20AA");
        }
        else{
            btnRadioSelfPickup.setSelected(true);
            setDeliveryDisable(true);
            lblTotal.setText("Total: " + discountPrice  + " \u20AA");
        }
        if(Client.orderController.getCurrentOrder().getOrderStatus().equals(OrderStatus.EXPRESS_PENDING) ){
            btnRadioExpressYes.setSelected(true);
            setTimeDateDisable(true);
        }
        else {
            btnRadioExpressNo.setSelected(true);
            setTimeDateDisable(false);
        }
    }
    @FXML
    void chooseCbBranch(ActionEvent event) {
        cbCity.setValue(cbBranch.getSelectionModel().getSelectedItem());
        cbCity.setDisable(true);
    }
    /**
     * If the user clicked on the delivery button then he will need to entered delivery data.
     * and a delivery price will be added to his total order price.
     * @param event
     */
    @FXML
    void clickBtnRadioDelivery(ActionEvent event) {
        if(btnRadioDelivery.isSelected()) {
            btnRadioSelfPickup.setSelected(false);
            setDeliveryDisable(false);
            lblDelivery.setText("Delivery: " + Client.orderController.DELIVERY_PRICE + " \u20AA");
            lblTotal.setText("Total: " + (discountPrice + Client.orderController.DELIVERY_PRICE) + " \u20AA");
            addressField.requestFocus();
        }
        else {
            setDeliveryDisable(true);
            lblTotal.setText("Total: " + (discountPrice - Client.orderController.DELIVERY_PRICE) + " \u20AA");
        }
    }

    /**
     * If the user clicked on self pick-up button
     * then he will not need to enter delivery data or pay a delivery fee.
     * @param event
     */
    @FXML
    void clickBtnRadioSelfPickup(ActionEvent event) {
        if(btnRadioSelfPickup.isSelected()){
            btnRadioDelivery.setSelected(false);
            setDeliveryDisable(true);
            lblDelivery.setText("Delivery: " + 0.0 + " \u20AA");
            lblTotal.setText("Total: " + discountPrice + " \u20AA");
        }
    }

    /**
     * Disables\enables data in accordance to customer`s selection
     * @param toDisable
     */
    private void setDeliveryDisable(boolean toDisable){
        lblCity.setDisable(toDisable);
        lblAddress.setDisable(toDisable);
//        cbCity.setDisable(toDisable);
        addressField.setDisable(toDisable);
    }

    /**
     * The order is not express order
     * @param event
     */
    @FXML
    void clickBTNRadioExpressNo(ActionEvent event) {
        if(btnRadioExpressNo.isSelected()) {
            btnRadioExpressYes.setSelected(false);
            setTimeDateDisable(false);
            lblExpressOrderError.setVisible(false);
            datePicker.requestFocus();
        }
        else{
            datePicker.setValue(null);
            cbTime.setValue(null);
            setTimeDateDisable(true);
        }
    }

    /**
     * The order is express order - set the time and date to be the current
     * @param event
     */
    @FXML
    void clickBtnRadioYes(ActionEvent event) {
        if(btnRadioExpressYes.isSelected()) {
            lblExpressOrderError.setVisible(false);
            btnRadioExpressNo.setSelected(false);
            setTimeDateDisable(true);
            if (LocalTime.now().compareTo(LocalTime.of(18, 30)) <= 0){
                datePicker.setValue(LocalDate.now());
                cbTime.setValue(LocalTime.now().toString().substring(0, 5));
            }
            else {
                datePicker.setValue(LocalDate.now().plusDays(1));
                cbTime.setValue("08:00");
            }
        }
        else{
            datePicker.setValue(null);
            cbTime.setValue(null);
            setTimeDateDisable(false);
        }
    }

    /**
     * Disables\enables data in accordance to customer`s selection
     * @param toDisable
     */
    private void setTimeDateDisable(boolean toDisable){
        lblDateTime.setDisable(toDisable);
        datePicker.setDisable(toDisable);
        cbTime.setDisable(toDisable);
    }

    /**
     * This method checks if all the data that was entered for the order are correct.
     * if tes, it will proceed to the next page in the order process (RecipientPage).
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnProceed(ActionEvent event) throws IOException {
        if(validateInput()) {
            Client.orderController.getCurrentOrder().setBranch(cbBranch.getSelectionModel().getSelectedItem());
            if (btnRadioDelivery.isSelected()) {
                Client.orderController.getCurrentOrder().setDeliveryAddress(cbCity.getSelectionModel().getSelectedItem() + " " + addressField.getText());
                Client.orderController.getCurrentOrder().setPrice(price + Client.orderController.DELIVERY_PRICE);
                Client.orderController.getCurrentOrder().setDiscountPrice(discountPrice + Client.orderController.DELIVERY_PRICE);
            } else {
                Client.orderController.getCurrentOrder().setPrice(price);
                Client.orderController.getCurrentOrder().setDiscountPrice(discountPrice);
                Client.orderController.getCurrentOrder().setDeliveryAddress(null);
            }
            Timestamp deliveryDateTime = Timestamp.valueOf(datePicker.getValue().toString() + " " + cbTime.getSelectionModel().getSelectedItem() + ":00");
            Client.orderController.getCurrentOrder().setDeliveryDate(deliveryDateTime);
            if(btnRadioExpressYes.isSelected()){
                Client.orderController.getCurrentOrder().setOrderStatus(OrderStatus.EXPRESS_PENDING);
            }
            else {
                Client.orderController.getCurrentOrder().setOrderStatus(OrderStatus.NORMAL_PENDING);
            }
            MainDashboardController.setContentFromFXML("OrderRecipientPage.fxml");
        }

    }

    /**
     * This method checks if all the data that was entered for the order are correct.
     * @return true if yes, else false.
     */
    private boolean validateInput(){
        int invalidFields = 0;
        if(cbBranch.getValue() == null){
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
        if(btnRadioDelivery.isSelected() && addressField.getText().isEmpty()){
            lblCityAndAddressError.setVisible(true);
            invalidFields++;
        }
        if(!btnRadioExpressYes.isSelected() && !btnRadioExpressNo.isSelected()){
            lblExpressOrderError.setVisible(true);
            invalidFields++;
        }
        if(btnRadioExpressNo.isSelected()) {
            if (datePicker.getValue() == null) {
                lblDateTimeError.setVisible(true);
                invalidFields++;
            }
            if (cbTime.getValue() == null) {
                lblDateTimeError.setVisible(true);
                invalidFields++;
            }
        }
        return invalidFields == 0;
    }

    /**
     * Cancel order and return to cart (CartPage).
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnCancelOrder(ActionEvent event) throws IOException {
        initCurrentOrder();
        MainDashboardController.setContentFromFXML("MyCartPage.fxml");
    }

    /**
     * If the day that was chosen is today, the hours in cbTime changes accordingly to the current time
     * @param event
     */
    @FXML
    void pickDateHandler(ActionEvent event) {
        List<String> timeLeftOfDay = new ArrayList<>();
        if (LocalDate.now().equals(datePicker.getValue())){
            for (int i = LocalTime.now().getHour(); i <= 18; i++) {
                timeLeftOfDay.add(i + ":00");
                if (i != 18)
                    timeLeftOfDay.add(i + ":30");
                times = FXCollections.observableArrayList(timeLeftOfDay);
                cbTime.getItems().clear();
                cbTime.getItems().addAll(times);
            }
        }
        else {
            times = FXCollections.observableArrayList(timeList);
            cbTime.getItems().clear();
            cbTime.getItems().addAll(times);
        }
    }
    /**
     * Set order object in order controller to a new Order.
     */
    private void initCurrentOrder() {
        Client.orderController.setCurrentOrder(new Order());
    }

}
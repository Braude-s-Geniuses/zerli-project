package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import order.Order;
import order.OrderStatus;
import user.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentPageController implements Initializable {
    private float total, balance;
    @FXML
    private TextField CardField1;
    @FXML
    private TextField CardField2;
    @FXML
    private TextField CardField3;
    @FXML
    private TextField CardField4;
    @FXML
    private CheckBox btnUseAnotherCreditCard;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnBrowseCatalog;
    @FXML
    private Button btnBrowseOrders;
    @FXML
    private Button btnPayment;
    @FXML
    private Button btnPlaceOrder;
    @FXML
    private CheckBox btnSavedCreditCard;
    @FXML
    private CheckBox btnUseBalance;
    @FXML
    private Button btnViewCart;

    @FXML
    private ComboBox<String> comboBoxMonth;

    @FXML
    private ComboBox<String> comboBoxYear;

    @FXML
    private Label creditLabel;
    @FXML
    private TextField cvvField;

    @FXML
    private Label expLable;

    @FXML
    private TextField idField;

    @FXML
    private CheckBox btnUpdateCreditCard;
    @FXML
    private Label idLabel;

    @FXML
    private Label lblCVV;

    @FXML
    private Label lblCVVError;

    @FXML
    private Label lblCardNumberError;

    @FXML
    private Label lblExpDateError;

    @FXML
    private Label lblForSideLine;

    @FXML
    private Label lblIDError;

    @FXML
    private Label lblPaymentError;

    @FXML
    private Label lblTotal;

    @FXML
    private ProgressBar progressBar;

    private Customer customer;


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
        customer = (Customer) Client.clientController.getClient().getLoggedInUser();

        List<String> monthList = new ArrayList<String>();       //Set value of months.
        for (int i = 1; i <= 12; i++) {
            if (i<10){
                monthList.add("0" + String.valueOf(i));
            }else {
                monthList.add(String.valueOf(i));
            }
        }
        ObservableList<String> months = FXCollections.observableArrayList(monthList);
        comboBoxMonth.getItems().addAll(months);

        List<String> yearList = new ArrayList<String>();       //Set value of years.
        for (int i = 2022; i <= 2035; i++) {
            yearList.add(String.valueOf(i));
        }
        ObservableList<String> years = FXCollections.observableArrayList(yearList);
        comboBoxYear.getItems().addAll(years);

        lblTotal.setText("Total: " + Client.orderController.sumOfCart() + " \u20AA");



        if(customer.getCreditCard() == null){         //How to get a costomer.
            btnSavedCreditCard.setDisable(true);
        }
        else{
            btnSavedCreditCard.setText(btnSavedCreditCard.getText() + "  (***" + customer.getCreditCard().substring(15) + ")");
        }
        if(customer.getBalance() == 0){
            btnUseBalance.setDisable(true);     //what if there is a bit of balance?
        }
        else{
            btnUseBalance.setText(btnUseBalance.getText() + "  (" + customer.getBalance() + " \u20AA" + ")");
        }
        if (customer.getCreditCard() == null && customer.getBalance() == 0){
            btnUseAnotherCreditCard.setSelected(true);
            btnUseAnotherCreditCard.setDisable(true);
        }
    }

    /**
     * Enables the customer to use his credit card
     * @param event
     */
    @FXML
    void clickBtnSavedCreditCard(ActionEvent event) {
        if(btnSavedCreditCard.isPressed()) {
            lblPaymentError.setVisible(false);
            lblCardNumberError.setVisible(false);
            lblCVVError.setVisible(false);
            lblIDError.setVisible(false);
            lblExpDateError.setVisible(false);
            btnUseAnotherCreditCard.setSelected(false);
            setCreditCardLabels(true);
        }
        else{
            setCreditCardLabels(false);
        }
    }

    /**
     * Enables the customer to use new credit card
     * @param event
     */
    @FXML
    void clickBtnUseAnotherCreditCard(ActionEvent event) {
        if(btnUseAnotherCreditCard.isPressed()) {
            lblPaymentError.setVisible(false);
            btnSavedCreditCard.setSelected(false);
            setCreditCardLabels(false);
        }
        else{
            lblPaymentError.setVisible(true);
            btnSavedCreditCard.setSelected(true);
            setCreditCardLabels(true);
        }
    }
    /**
     * Finishes order process, update order in DB
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    @FXML
    void clickBtnPlaceOrder(ActionEvent event) throws IOException, InterruptedException {
        if(validateInput()){ //TODO- update in DB after combine
            if (btnUpdateCreditCard.isPressed()) {
                customer.setCreditCard(CardField1.getText() + CardField2.getText() + CardField3.getText() + CardField4.getText());
                customer.setId(idField.getText());
                customer.setExpDate(comboBoxMonth.getSelectionModel().getSelectedItem() +"/"+ comboBoxYear.getSelectionModel().getSelectedItem());
                customer.setCvv(cvvField.getText());
            }
            if(btnUseBalance.isPressed()){ //TODO- update in DB after combine
                customer.setBalance(balance);
            }
            Client.orderController.getCurrentOrder().setPrice(total);
            Client.orderController.getCurrentOrder().setOrderStatus(OrderStatus.NORMAL_PENDING);
            Client.orderController.getCurrentOrder().setCustomerId(customer.getUserId());
            Client.orderController.getCurrentOrder().setOrderDate(Timestamp.valueOf(LocalDateTime.now()));

            if(Client.orderController.sendNewOrder()) {
//                new Thread(() -> {
//                    btnPayment.setStyle("-fx-text-fill: #309639");
//                    for (double i = 0.9; i < 1.0; i+=0.01){
//                        progressBar.setProgress(i);
//                        try {
//                            sleep(1500);
//                        } catch(InterruptedException ex) {
//                            Thread.currentThread().interrupt();
//                        }
//                    }
//                }).start();


                Client.setScene(event, getClass().getResource("CompletedOrderPage.fxml"));

            }else{
                System.out.println("not succeed"); //TODO
            }
        }
    }

    /**
     * Validates all values inserted in payment page before finishes order process
     * @return true if all details are verified
     */
    private boolean validateInput() {
        if ((Client.orderController.sumOfCart() - customer.getBalance()) <= 0 && btnUseBalance.isSelected()){
            return true; // If balance is enough and was also selected.
        }
        int invalidFields = 0;
        if (!btnSavedCreditCard.isSelected() && !btnUseAnotherCreditCard.isSelected()){
            lblPaymentError.setVisible(true);
            invalidFields++;
        }
        if (btnUseBalance.isSelected() && (Client.orderController.sumOfCart() - customer.getBalance()) > 0 && !btnSavedCreditCard.isSelected() && !btnUseAnotherCreditCard.isSelected()) {
            lblPaymentError.setVisible(true);                       //If balance was selected but not enough
            invalidFields++;                                      // and no other payment method was selected
        }
        if (btnUseAnotherCreditCard.isSelected() && !validCardNumber()){
            lblCardNumberError.setVisible(true);
            invalidFields++;
        }
        if (btnUseAnotherCreditCard.isSelected() && idField.getText().trim().isEmpty() && !idField.getText().contains("[0-9]+")){
            lblIDError.setVisible(true);
            invalidFields++;
        }

        if (btnUseAnotherCreditCard.isSelected() && comboBoxYear.getSelectionModel().isEmpty()){ //add check
            lblExpDateError.setVisible(true);
            invalidFields++;
        }
        if (btnUseAnotherCreditCard.isSelected() && comboBoxMonth.getSelectionModel().isEmpty()){
            lblExpDateError.setVisible(true);
            invalidFields++;
        }
        if (btnUseAnotherCreditCard.isSelected() && cvvField.getText().trim().isEmpty()){
            lblCVVError.setVisible(true);
            invalidFields++;
        }
        return invalidFields == 0 ? true: false;
    }

    /**
     * Verifies whether the credit card inserted contains only numbers and in the correct length
     * @return true if the number card inserted is valid
     */
    private boolean validCardNumber() {
        if (CardField1.getText().length() != 4 && !CardField1.getText().contains("[0-9]+")){
            return false;
        }
        if (CardField2.getText().length() != 4 && !CardField2.getText().contains("[0-9]+")){
            return false;
        }
        if (CardField3.getText().length() != 4 && !CardField3.getText().contains("[0-9]+")){
            return false;
        }
        if (CardField4.getText().length() != 4 && !CardField4.getText().contains("[0-9]+")){
            return false;
        }
        return true;
    }

    /**
     * Enables the customer to use his balance and calculates total price
     * @param event
     */
    @FXML
    void clickBtnUseBalance(ActionEvent event) {
        if(btnUseBalance.isSelected()){
            if (( Client.orderController.getCurrentOrder().getPrice() - customer.getBalance()) <= 0){ //If balance is more than total price.
                lblTotal.setText("Total: 0" + " \u20AA");
                setCreditCardLabels(true);
                btnUseAnotherCreditCard.setDisable(true);
                total = 0;
                balance = customer.getBalance() - Client.orderController.getCurrentOrder().getPrice();
            }
            else{
                lblTotal.setText("Total: " + ( Client.orderController.getCurrentOrder().getPrice() - customer.getBalance()) + " \u20AA");
                total = ( Client.orderController.getCurrentOrder().getPrice() - customer.getBalance());
                balance = 0;
            }
        }
        else {
            lblTotal.setText("Total: " +  Client.orderController.getCurrentOrder().getPrice() + " \u20AA");
            setCreditCardLabels(false);
            btnUseAnotherCreditCard.setDisable(false);
            total =  Client.orderController.getCurrentOrder().getPrice();
        }
    }

    /**
     * Set credit card fields disable/ enable
     * @param isDisabled
     */
    private void setCreditCardLabels(boolean isDisabled) {
        creditLabel.setDisable(isDisabled);
        CardField1.setDisable(isDisabled);
        CardField2.setDisable(isDisabled);
        CardField3.setDisable(isDisabled);
        CardField4.setDisable(isDisabled);
        idLabel.setDisable(isDisabled);
        idField.setDisable(isDisabled);
        expLable.setDisable(isDisabled);
        comboBoxMonth.setDisable(isDisabled);
        comboBoxYear.setDisable(isDisabled);
        lblForSideLine.setDisable(isDisabled);
        lblCVV.setDisable(isDisabled);
        cvvField.setDisable(isDisabled);
        btnUpdateCreditCard.setDisable(isDisabled);
    }

    /**
     * Returns to recipient info page
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnBack(ActionEvent event) throws IOException {
        Client.setScene(event, getClass().getResource("RecipientPage.fxml"));
    }
    /**
     * View Catalog - cancels current Order
     * @param event
     */
    @FXML
    void clickBtnBrowseCatalog(ActionEvent event) {
        initCurrentOrder();
        //TODO Combine
    }

    /**
     * View Customer`s orders - cancels current Order
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnBrowseOrders(ActionEvent event) throws IOException {
        initCurrentOrder();
        Client.setScene(event, getClass().getResource("OrdersPage.fxml"));
    }

    /**
     *  Customer`s cart - cancels current Order
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnViewCart(ActionEvent event) throws IOException {
        initCurrentOrder();
        Client.setScene(event, getClass().getResource("CartPage.fxml"));
    }
    /**
     * Set order object in order controller to a new Order.
     */
    private void initCurrentOrder() {
        Client.orderController.setCurrentOrder(new Order());
    }


}

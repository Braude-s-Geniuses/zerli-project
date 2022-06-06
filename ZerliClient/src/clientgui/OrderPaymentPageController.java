package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import user.Customer;
import util.Alert;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderPaymentPageController implements Initializable {
    private float total, balance;
    @FXML
    private TextField cardField1;
    @FXML
    private TextField cardField2;
    @FXML
    private TextField cardField3;
    @FXML
    private TextField cardField4;
    @FXML
    private CheckBox btnUseAnotherCreditCard;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnPayment;
    @FXML
    private Button btnPlaceOrder;
    @FXML
    private CheckBox btnSavedCreditCard;
    @FXML
    private CheckBox btnUseBalance;
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
    private Label lblSaveCardWithDifferentIdError;

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
        customer = (Customer) Client.userController.getLoggedInUser();
        setCreditCardLabels(true);
        List<String> monthList = new ArrayList<String>();       //Set value of months.
        for (int i = 1; i <= 12; i++) {
            if (i<10){
                monthList.add("0" + i);
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

        lblTotal.setText("Total: " + Client.orderController.getCurrentOrder().getDiscountPrice() + " \u20AA");

        if(customer.getCreditCard() == null){         //How to get a costomer.
            btnSavedCreditCard.setDisable(true);
        }
        else{
            btnSavedCreditCard.setText(btnSavedCreditCard.getText() + "  (***" + customer.getCreditCard().substring(12) + ")");
        }
        if(customer.getBalance() == 0){
            btnUseBalance.setDisable(true);
        }
        else{
            btnUseBalance.setText(btnUseBalance.getText() + "  (" + customer.getBalance() + " \u20AA" + ")");
        }
        if (customer.getCreditCard() == null && customer.getBalance() == 0){
            btnUseAnotherCreditCard.setSelected(true);
            btnUseAnotherCreditCard.setDisable(true);
        }
        cardField1.textProperty().addListener((obs, oldText, newText)-> {
            if (oldText.length() < 4 && newText.length() >= 4)
                cardField2.requestFocus();
        });

        cardField2.textProperty().addListener((obs, oldText, newText)-> {
            if (oldText.length() <4  && newText.length() >= 4)
                cardField3.requestFocus();
        });

        cardField3.textProperty().addListener((obs, oldText, newText)-> {
            if (oldText.length() < 4 && newText.length() >= 4)
                cardField4.requestFocus();
        });

        cardField4.textProperty().addListener((obs, oldText, newText)-> {
            if (oldText.length() < 4 && newText.length() >= 4)
                idField.requestFocus();
        });

        idField.textProperty().addListener((obs, oldText, newText)-> {
            if (oldText.length() < 9 && newText.length() >= 9)
                expLable.requestFocus();
        });

        cvvField.textProperty().addListener((obs, oldText, newText)-> {
            if (oldText.length() < 3 && newText.length() >= 3)
                btnUpdateCreditCard.requestFocus();
        });

        btnUpdateCreditCard.setOnKeyPressed( evt ->{
            if(evt.getCode().equals(KeyCode.TAB)){
                btnPlaceOrder.requestFocus();
            }
        });

        btnPlaceOrder.setOnKeyPressed( evt ->{
            if(evt.getCode().equals(KeyCode.TAB)){
                btnPlaceOrder.fire();
            }
        });
    }

    /**
     * Enables the customer to use his credit card
     * @param event
     */
    @FXML
    void clickBtnSavedCreditCard(ActionEvent event) {
        btnSavedCreditCard.setSelected(true);
        lblPaymentError.setVisible(false);
        lblCardNumberError.setVisible(false);
        lblCVVError.setVisible(false);
        lblIDError.setVisible(false);
        lblExpDateError.setVisible(false);
        btnUseAnotherCreditCard.setSelected(false);
        setCreditCardLabels(true);
    }

    /**
     * Enables the customer to use new credit card
     * @param event
     */
    @FXML
    void clickBtnUseAnotherCreditCard(ActionEvent event) {
        btnUseAnotherCreditCard.setSelected(true);
        lblPaymentError.setVisible(false);
        btnSavedCreditCard.setSelected(false);
        setCreditCardLabels(false);
    }

    /**
     * Finishes order process, update order in DB
     * @param event
     * @throws IOException
     * @throws InterruptedException
     */
    @FXML
    void clickBtnPlaceOrder(ActionEvent event) {
        if(validateInput()){
            if (btnUpdateCreditCard.isSelected()) {
                customer.setCreditCard(cardField1.getText() + cardField2.getText() + cardField3.getText() + cardField4.getText());
                customer.setExpDate(comboBoxMonth.getSelectionModel().getSelectedItem() +"/"+ comboBoxYear.getSelectionModel().getSelectedItem());
                customer.setCvv(cvvField.getText());
                Client.orderController.updateCreditCard(customer.getUserId(),customer.getCardDetails());
            }
            if(btnUseBalance.isSelected()){
                customer.setBalance(balance);
                Client.orderController.updateBalance(customer.getUserId(),balance);
            }
            if(customer.getNewCustomer()){
                customer.setNewCustomer(false);
                Client.orderController.updateNewCustomer(customer.getUserId());
            }
            Client.orderController.getCurrentOrder().setOrderDate(Timestamp.valueOf(LocalDateTime.now()));

            if(Client.orderController.sendNewOrder()) {
                MainDashboardController.setContentFromFXML("OrderCompletePage.fxml");
                MainDashboardController.refreshCartCounter();
            }else{
                MainDashboardController.setContentFromFXML("MyCartPage.fxml");
                MainDashboardController.createAlert("Failed to place the Order. Please try again later", Alert.DANGER, Duration.seconds(5), 135, 67);
            }

        }
    }

    /**
     * Validates all values inserted in payment page before finishes order process
     * @return true if all details are verified
     */
    private boolean validateInput() {
        if ((Client.orderController.getOrderPrice(true) - customer.getBalance()) <= 0 && btnUseBalance.isSelected()){
            return true; // If balance is enough and was also selected.
        }
        int invalidFields = 0;
        if (!btnSavedCreditCard.isSelected() && !btnUseAnotherCreditCard.isSelected()){
            lblPaymentError.setVisible(true);
            invalidFields++;
        }
        if (btnUseBalance.isSelected() && (Client.orderController.getOrderPrice(true) - customer.getBalance()) > 0 && !btnSavedCreditCard.isSelected() && !btnUseAnotherCreditCard.isSelected()) {
            lblPaymentError.setVisible(true);                       //If balance was selected but not enough
            invalidFields++;                                      // and no other payment method was selected
        }
        if (btnUseAnotherCreditCard.isSelected() && !validCardNumber()){
            lblCardNumberError.setVisible(true);
            invalidFields++;
        }
        if (btnUseAnotherCreditCard.isSelected() && idField.getText().trim().isEmpty() && !idField.getText().contains("[0-9]+") && idField.getText().length() != 9 ){
            lblIDError.setVisible(true);
            invalidFields++;
        }

        if (btnUseAnotherCreditCard.isSelected() && comboBoxYear.getSelectionModel().isEmpty()){
            lblExpDateError.setVisible(true);
            invalidFields++;
        }
        if (btnUseAnotherCreditCard.isSelected() && btnUpdateCreditCard.isSelected()){
            if(!customer.getId().equals(idField.getText())) {
                lblSaveCardWithDifferentIdError.setVisible(true);
                invalidFields++;
            }
        }
        if (btnUseAnotherCreditCard.isSelected() && comboBoxMonth.getSelectionModel().isEmpty()){
            lblExpDateError.setVisible(true);
            invalidFields++;
        }
        if (btnUseAnotherCreditCard.isSelected() && cvvField.getText().trim().isEmpty()  && cvvField.getText().length() != 3){
            lblCVVError.setVisible(true);
            invalidFields++;
        }
        return invalidFields == 0;
    }

    /**
     * Verifies whether the credit card inserted contains only numbers and in the correct length
     * @return true if the number card inserted is valid
     */
    private boolean validCardNumber() {
        if (cardField1.getText().length() != 4 && !cardField1.getText().contains("[0-9]+")){
            return false;
        }
        if (cardField2.getText().length() != 4 && !cardField2.getText().contains("[0-9]+")){
            return false;
        }
        if (cardField3.getText().length() != 4 && !cardField3.getText().contains("[0-9]+")){
            return false;
        }
        return cardField4.getText().length() == 4 || cardField4.getText().contains("[0-9]+");
    }

    /**
     * Enables the customer to use his balance and calculates total price
     * @param event
     */
    @FXML
    void clickBtnUseBalance(ActionEvent event) {
        if(btnUseBalance.isSelected()){
            if (( Client.orderController.getCurrentOrder().getDiscountPrice() - customer.getBalance()) <= 0){ //balance is more than total price.
                lblTotal.setText("Total: 0" + " \u20AA");
                setCreditCardLabels(true);
                btnUseAnotherCreditCard.setDisable(true);
                btnSavedCreditCard.setDisable(true);
                total = 0;
                balance = customer.getBalance() - Client.orderController.getCurrentOrder().getDiscountPrice();
            }
            else{
                lblTotal.setText("Total: " + ( Client.orderController.getCurrentOrder().getDiscountPrice() - customer.getBalance()) + " \u20AA");
                total = ( Client.orderController.getCurrentOrder().getDiscountPrice() - customer.getBalance());
                balance = 0;
            }
        }
        else {
            lblTotal.setText("Total: " +  Client.orderController.getCurrentOrder().getDiscountPrice() + " \u20AA");
            setCreditCardLabels(false);
            balance = customer.getBalance();
            total =  Client.orderController.getCurrentOrder().getDiscountPrice();
            btnUseAnotherCreditCard.setDisable(false);
            btnSavedCreditCard.setDisable(false);
        }
    }

    /**
     * Customer can update his credit card
     * @param event
     */
    @FXML
    void checkButtonUpdateCard(ActionEvent event) {
        if(btnSavedCreditCard.isSelected()){
            lblSaveCardWithDifferentIdError.setVisible(!customer.getId().equals(idField.getText()));
            btnPlaceOrder.requestFocus();
        }
        else{
            lblSaveCardWithDifferentIdError.setVisible(false);
        }
    }

    @FXML
    void cvvFieldEvent(KeyEvent event) {
        lblCVVError.setVisible(false);
    }
    @FXML
    void idFieldEvent(KeyEvent event) {
        lblIDError.setVisible(false);
    }
    @FXML
    void selectCbMonth(ActionEvent event) {
        lblExpDateError.setVisible(false);
    }

    @FXML
    void selectCbYear(ActionEvent event) {
        if(comboBoxYear.getValue() != null)
            cvvField.requestFocus();
    }
    @FXML
    void cardField1Event(KeyEvent event) {
        lblCardNumberError.setVisible(false);
    }

    @FXML
    void cardField2Event(KeyEvent event) {
        lblCardNumberError.setVisible(false);
    }

    @FXML
    void cardField3Event(KeyEvent event) {
        lblCardNumberError.setVisible(false);
    }

    @FXML
    void cardField4Event(KeyEvent event) {
        lblCardNumberError.setVisible(false);
    }

    /**
     * Set credit card fields disable/ enable
     * <b>Note:</b> this feature is invisible and can be used in future updates to allow the customer to enter different credit card!!!
     * @param isDisabled
     */
    private void setCreditCardLabels(boolean isDisabled) {
        isDisabled = false;
        btnUseAnotherCreditCard.setVisible(isDisabled);
        creditLabel.setVisible(isDisabled);
        cardField1.setVisible(isDisabled);
        cardField2.setVisible(isDisabled);
        cardField3.setVisible(isDisabled);
        cardField4.setVisible(isDisabled);
        idLabel.setVisible(isDisabled);
        idField.setVisible(isDisabled);
        expLable.setVisible(isDisabled);
        comboBoxMonth.setVisible(isDisabled);
        comboBoxYear.setVisible(isDisabled);
        lblForSideLine.setVisible(isDisabled);
        lblCVV.setVisible(isDisabled);
        cvvField.setVisible(isDisabled);
        btnUpdateCreditCard.setVisible(isDisabled);
    }

    /**
     * Returns to recipient info page
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnBack(ActionEvent event) throws IOException {
        MainDashboardController.setContentFromFXML("OrderRecipientPage.fxml");
    }
}
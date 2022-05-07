package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
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
    private float total;
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
    @FXML
    void clickBtnSavedCreditCard(ActionEvent event) {
        lblPaymentError.setVisible(false);
        lblCardNumberError.setVisible(false);
        lblCVVError.setVisible(false);
        lblIDError.setVisible(false);
        lblExpDateError.setVisible(false);
        btnUseAnotherCreditCard.setSelected(false);
        setCreditCardLabels(true);
    }
    @FXML
    void clickBtnUseAnotherCreditCard(ActionEvent event) {
        lblPaymentError.setVisible(false);
        btnSavedCreditCard.setSelected(false);
        setCreditCardLabels(false);


    }

    @FXML
    void clickBtnBack(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("RecipientPage.fxml"));
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
    void clickBtnPlaceOrder(ActionEvent event) throws IOException {
        if(validateInput()){
            if (btnUpdateCreditCard.isPressed()) {
                customer.setCreditCard(CardField1.getText() + CardField2.getText() + CardField3.getText() + CardField4.getText());
                customer.setId(idField.getText());
                customer.setExpDate(comboBoxMonth.getSelectionModel().getSelectedItem() +"/"+ comboBoxYear.getSelectionModel().getSelectedItem());
                customer.setCvv(cvvField.getText());
            }
            Client.orderController.getCurrentOrder().setPrice(total);
            Client.orderController.getCurrentOrder().setOrderStatus(OrderStatus.NORMAL_PENDING);
            Client.orderController.getCurrentOrder().setCustomerId(customer.getUserId());
            Client.orderController.getCurrentOrder().setOrderDate(Timestamp.valueOf(LocalDateTime.now()));
            if(Client.orderController.sendNewOrder()) {
                ((Node) event.getSource()).getScene().getWindow().hide();
                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("CompletedOrderPage.fxml"));
                Scene scene = new Scene(root);

                primaryStage.setTitle("Zerli Client");
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.show();
            }else{
                System.out.println("not succeed");
            }

            System.out.println(Client.orderController.getCurrentOrder().toString());
        }
    }

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




    @FXML
    void clickBtnRecipientInfo(ActionEvent event) {

    }



    @FXML
    void clickBtnUseBalance(ActionEvent event) {
        if(btnUseBalance.isSelected()){
            if (( Client.orderController.getCurrentOrder().getPrice() - customer.getBalance()) <= 0){ //If balance is more than total price.
                lblTotal.setText("Total: 0" + " \u20AA");
                setCreditCardLabels(true);
                btnUseAnotherCreditCard.setDisable(true);
                total = 0;
            }
            else{
                lblTotal.setText("Total: " + ( Client.orderController.getCurrentOrder().getPrice() - customer.getBalance()) + " \u20AA");
                total = ( Client.orderController.getCurrentOrder().getPrice() - customer.getBalance());
            }
        }
        else {
            lblTotal.setText("Total: " +  Client.orderController.getCurrentOrder().getPrice() + " \u20AA");
            setCreditCardLabels(false);
            btnUseAnotherCreditCard.setDisable(false);
            total =  Client.orderController.getCurrentOrder().getPrice();
        }

    }

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
            btnSavedCreditCard.setText(btnSavedCreditCard.getText() + "  (***" + customer.getCreditCard().substring(15) + " \u20AA" + ")");
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
}

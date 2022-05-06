package clientgui;

import client.Client;
import client.ZerliClient;
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
import user.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PaymentPageController implements Initializable {

    @FXML
    private TextField CardField1;

    @FXML
    private TextField CardField2;

    @FXML
    private TextField CardField3;

    @FXML
    private TextField CardField4;

    @FXML
    private CheckBox btnAddCraditCard;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnBrowseCatalog;

    @FXML
    private Button btnBrowseOrders;

    @FXML
    private Button btnDeliveryData;

    @FXML
    private Button btnPayment;

    @FXML
    private Button btnPlaceOrder;

    @FXML
    private Button btnRecipientInfo;

    @FXML
    private CheckBox btnSavedCraditCard;

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
    void clickBtnAddCreditCard(ActionEvent event) {
        creditLabel.setDisable(false);
        CardField1.setDisable(false);
        CardField2.setDisable(false);
        CardField3.setDisable(false);
        CardField4.setDisable(false);
        idLabel.setDisable(false);
        idField.setDisable(false);
        expLable.setDisable(false);
        comboBoxMonth.setDisable(false);
        comboBoxYear.setDisable(false);
        lblForSideLine.setDisable(false);
        lblCVV.setDisable(false);
        cvvField.setDisable(false);


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
    void clickBtnDeliveryData(ActionEvent event) {

    }

    @FXML
    void clickBtnPayment(ActionEvent event) {

    }

    @FXML
    void clickBtnPlaceOrder(ActionEvent event) {
        if(validateInput()){
           customer.setCreditCard(CardField1.getText() + CardField2.getText() + CardField3.getText() + CardField4.getText());
           customer.setId(idField.getText());
           customer.setExpDate(comboBoxMonth.getSelectionModel().getSelectedItem() +"/"+ comboBoxYear.getSelectionModel().getSelectedItem());
           customer.setCvv(cvvField.getText());
           System.out.println(customer.toString());
        }
    }

    private boolean validateInput() {
        if ((Client.orderController.sumOfCart() - customer.getBalance()) <= 0 && btnUseBalance.isSelected()){
            return true; // If balance is enough and was also selected.
        }
        int invalidFields = 0;
        if (!btnSavedCraditCard.isSelected() && !btnAddCraditCard.isSelected()){
            lblPaymentError.setVisible(true);
            invalidFields++;
        }
        if (btnUseBalance.isSelected() && (Client.orderController.sumOfCart() - customer.getBalance()) > 0 && !btnSavedCraditCard.isSelected() && !btnAddCraditCard.isSelected()) {
            lblPaymentError.setVisible(true);                       //If balance was selected but not enough
            invalidFields++;                                      // and no other payment method was selected
        }
        if (btnAddCraditCard.isSelected() && !validCardNumber()){
            lblCardNumberError.setVisible(true);
            invalidFields++;
        }
        if (btnAddCraditCard.isSelected() && idField.getText().trim().isEmpty()){
            lblIDError.setVisible(true);
            invalidFields++;
        }
        if (btnAddCraditCard.isSelected() && comboBoxYear.getSelectionModel().isEmpty()){ //add check
            lblExpDateError.setVisible(true);
            invalidFields++;
        }
        if (btnAddCraditCard.isSelected() && comboBoxMonth.getSelectionModel().isEmpty()){
            lblExpDateError.setVisible(true);
            invalidFields++;
        }
        if (btnAddCraditCard.isSelected() && cvvField.getText().trim().isEmpty()){
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
    void clickBtnSaveCraditCard(ActionEvent event) {
        creditLabel.setDisable(true);
        CardField1.setDisable(true);
        CardField2.setDisable(true);
        CardField3.setDisable(true);
        CardField4.setDisable(true);
        idLabel.setDisable(true);
        idField.setDisable(true);
        expLable.setDisable(true);
        comboBoxMonth.setDisable(true);
        comboBoxYear.setDisable(true);
        lblForSideLine.setDisable(true);
        lblCVV.setDisable(true);
        cvvField.setDisable(true);



    }

    @FXML
    void clickBtnUseBalabce(ActionEvent event) {
        if(btnUseBalance.isSelected()){
            if ((Client.orderController.sumOfCart() - customer.getBalance()) <= 0){ //If balance is more than total price.
                lblTotal.setText("Total: 0" + " \u20AA");
                creditLabel.setDisable(true);
                CardField1.setDisable(true);
                CardField2.setDisable(true);
                CardField3.setDisable(true);
                CardField4.setDisable(true);
                idLabel.setDisable(true);
                idField.setDisable(true);
                expLable.setDisable(true);
                comboBoxMonth.setDisable(true);
                comboBoxYear.setDisable(true);
                lblForSideLine.setDisable(true);
                lblCVV.setDisable(true);
                cvvField.setDisable(true);
                btnAddCraditCard.setDisable(true);
            }
            else{
                lblTotal.setText("Total: " + (Client.orderController.sumOfCart() - customer.getBalance()) + " \u20AA");
            }
        }
        else {
            lblTotal.setText("Total: " + Client.orderController.sumOfCart() + " \u20AA");
            creditLabel.setDisable(false);
            CardField1.setDisable(false);
            CardField2.setDisable(false);
            CardField3.setDisable(false);
            CardField4.setDisable(false);
            idLabel.setDisable(false);
            idField.setDisable(false);
            expLable.setDisable(false);
            comboBoxMonth.setDisable(false);
            comboBoxYear.setDisable(false);
            lblForSideLine.setDisable(false);
            lblCVV.setDisable(false);
            cvvField.setDisable(false);
            btnAddCraditCard.setDisable(false);

        }

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
            btnSavedCraditCard.setDisable(true);
        }
        if(customer.getBalance() == 0){
            btnUseBalance.setDisable(true);     //what if there is a bit of balance?
        }
        if (customer.getCreditCard() == null && customer.getBalance() == 0){
            btnAddCraditCard.setSelected(true);
            btnAddCraditCard.setDisable(true);
        }



    }
}

package clientgui;

import client.Client;
import client.ZerliClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import user.Customer;

import java.net.URL;
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
    private Label lblForMonth;

    @FXML
    private Label lblForSideLine;

    @FXML
    private Label lblForYear;

    @FXML
    private Label lblGreetingError;

    @FXML
    private Label lblNoGreetingChooseError;

    @FXML
    private Label lblNoNameError;

    @FXML
    private Label lblNoPhoneError;

    @FXML
    private Label lblNoRecipientError;

    @FXML
    private Label lblTotal;

    @FXML
    private TextField monthField;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField yearField;

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
        lblForMonth.setDisable(false);
        lblForYear.setDisable(false);
        monthField.setDisable(false);
        yearField.setDisable(false);
        lblForSideLine.setDisable(false);
        lblCVV.setDisable(false);
        cvvField.setDisable(false);


    }

    @FXML
    void clickBtnBack(ActionEvent event) {

    }

    @FXML
    void clickBtnBrowseCatalog(ActionEvent event) {

    }

    @FXML
    void clickBtnBrowseOrders(ActionEvent event) {

    }

    @FXML
    void clickBtnDeliveryData(ActionEvent event) {

    }

    @FXML
    void clickBtnPayment(ActionEvent event) {

    }

    @FXML
    void clickBtnPlaceOrder(ActionEvent event) {

    }

    @FXML
    void clickBtnRecipientInfo(ActionEvent event) {

    }

    @FXML
    void clickBtnSaveCraditCard(ActionEvent event) {

    }

    @FXML
    void clickBtnUseBalabce(ActionEvent event) {

    }

    @FXML
    void clickBtnViewCart(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       /* Customer customer = (Customer) Client.clientController.getClient().getLoggedInUser();
        if(customer.getCreditCard().isEmpty()){         //How to get a costomer.
            btnSavedCraditCard.setDisable(true);
        }
        if(customer.getBalance() == 0){
            btnUseBalance.setDisable(true);     //what if there is a bit of balance?
        }*/



    }
}

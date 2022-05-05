package clientgui;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RecipientPageController implements Initializable {


    @FXML
    private TextArea GreetingField;

    @FXML
    private Label GreetingLable;

    @FXML
    private Label GreetingLableIfYes;

    @FXML
    private Label PhoneLabel;

    @FXML
    private Label RecipientLable;

    @FXML
    private RadioButton btnAddGreeting;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnBrowseCatalog;

    @FXML
    private Button btnBrowseOrders;

    @FXML
    private Button btnDeliveryData;

    @FXML
    private RadioButton btnIamNotTheRecipient;

    @FXML
    private RadioButton btnIamTheRecipient;

    @FXML
    private RadioButton btnNoAddGreeting;

    @FXML
    private Button btnPayment;

    @FXML
    private Button btnProceed;

    @FXML
    private Button btnRecipientInfo;

    @FXML
    private Button btnViewCart;

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
    private TextField nameField;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField phoneField;

    @FXML
    private ProgressBar progressBar;

    @FXML
    void clickBtnBack(ActionEvent event) throws IOException {
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("DeliveryPage.fxml"));
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
    void clickBtnDeliveryData(ActionEvent event) throws IOException {

    }

    @FXML
    void clickBtnIamNotTheRecipient(ActionEvent event) {
        btnIamTheRecipient.setSelected(false);
        nameField.setDisable(false);
        phoneField.setDisable(false);
        nameLabel.setDisable(false);
        PhoneLabel.setDisable(false);
    }

    @FXML
    void clickBtnPayment(ActionEvent event) {

    }

    @FXML
    void clickBtnProceed(ActionEvent event) throws IOException {
        if(validateInput()) {
            if (btnIamNotTheRecipient.isSelected()){
                Client.orderController.getCurrentOrder().setRecipientName(nameField.getText());
                Client.orderController.getCurrentOrder().setRecipientPhone(phoneField.getText());
            }
            if (btnAddGreeting.isSelected()){
                Client.orderController.getCurrentOrder().setGreetingCard(GreetingField.getText());
            }
            //System.out.println(Client.orderController.getCurrentOrder());
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("PaymentPage.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("Zerli Client");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }


    }

    private boolean validateInput() {
        int invalidFields = 0;
        if(!btnIamTheRecipient.isSelected() && !btnIamNotTheRecipient.isSelected()){
            lblNoRecipientError.setVisible(true);
            invalidFields++;
        }
        if(btnIamNotTheRecipient.isSelected() && nameField.getText().trim().isEmpty()){
            lblNoNameError.setVisible(true);
            invalidFields++;
        }
        if(btnIamNotTheRecipient.isSelected() && phoneField.getText().trim().isEmpty()){
            lblNoPhoneError.setVisible(true);
            invalidFields++;
        }
        if(!btnAddGreeting.isSelected() && !btnNoAddGreeting.isSelected()){
            lblNoGreetingChooseError.setVisible(true);
            invalidFields++;
        }
        if(btnAddGreeting.isSelected() && GreetingField.getText().trim().isEmpty()){
            lblGreetingError.setVisible(true);
            invalidFields++;
        }
        if(invalidFields > 0){
            return false;
        }
        return true;
    }

    @FXML
    void clickBtnRadioAddGreeting(ActionEvent event) {
        btnNoAddGreeting.setSelected(false);
        GreetingField.setDisable(false);
        GreetingLableIfYes.setDisable(false);
    }

    @FXML
    void clickBtnRadioNoAddGreeting(ActionEvent event) {
        btnAddGreeting.setSelected(false);
        GreetingField.setDisable(true);
        GreetingLableIfYes.setDisable(true);
    }

    @FXML
    void clickBtnRadioIAmTheRecipient(ActionEvent event) {
        btnIamNotTheRecipient.setSelected(false);
        nameField.setDisable(true);
        phoneField.setDisable(true);
        nameLabel.setDisable(true);
        PhoneLabel.setDisable(true);

    }

    @FXML
    void clickBtnRecipientInfo(ActionEvent event) {

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
        GreetingField.setDisable(true);
        GreetingLableIfYes.setDisable(true);
    }

}

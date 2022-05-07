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
import order.Order;
import user.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RecipientPageController implements Initializable {
    @FXML
    private TextArea greetingField;
    @FXML
    private Label GreetingLabelIfYes;
    @FXML
    private Label PhoneLabel;
    @FXML
    private RadioButton btnAddGreeting;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnBrowseCatalog;
    @FXML
    private Button btnBrowseOrders;
    @FXML
    private RadioButton btnIamNotTheRecipient;
    @FXML
    private RadioButton btnIamTheRecipient;
    @FXML
    private RadioButton btnNoAddGreeting;
    @FXML
    private Button btnProceed;
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


    /**
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        restoreRecipientData();
    }
    private void restoreRecipientData() {
        if(Client.orderController.getCurrentOrder().getRecipientName() != null){
            btnIamNotTheRecipient.setSelected(true);
            phoneField.setText(Client.orderController.getCurrentOrder().getRecipientPhone());
            nameField.setText(Client.orderController.getCurrentOrder().getRecipientName());
        }
        else {
            btnIamTheRecipient.setSelected(true);
            nameField.setDisable(true);
            phoneField.setDisable(true);
            nameLabel.setDisable(true);
            PhoneLabel.setDisable(true);
        }

        if(Client.orderController.getCurrentOrder().getGreetingCard() != null){
            btnAddGreeting.setSelected(true);
            greetingField.setText(Client.orderController.getCurrentOrder().getGreetingCard());
            greetingField.setDisable(false);
            GreetingLabelIfYes.setDisable(false);
        }
        else{
            btnNoAddGreeting.setSelected(true);
            greetingField.setDisable(true);
            GreetingLabelIfYes.setDisable(true);
        }
    }
    /**
     * If selected, there is a need to fill recipient information.
     * @param event
     */
    @FXML
    void clickBtnIamNotTheRecipient(ActionEvent event) {
        btnIamTheRecipient.setSelected(false);
        nameField.setDisable(false);
        phoneField.setDisable(false);
        nameLabel.setDisable(false);
        PhoneLabel.setDisable(false);
    }

    /**
     * If selected, there is no need to fill recipient information.
     * @param event
     */
    @FXML
    void clickBtnRadioIAmTheRecipient(ActionEvent event) {
        btnIamNotTheRecipient.setSelected(false);
        nameField.setDisable(true);
        phoneField.setDisable(true);
        nameLabel.setDisable(true);
        PhoneLabel.setDisable(true);

    }

    /**
     * If selected, there is an option to add a greeting card
     * @param event
     */
    @FXML
    void clickBtnRadioAddGreeting(ActionEvent event) {
        btnNoAddGreeting.setSelected(false);
        greetingField.setDisable(false);
        GreetingLabelIfYes.setDisable(false);
    }

    /**
     * If selected, there is no option to add a greeting card
     * @param event
     */
    @FXML
    void clickBtnRadioNoAddGreeting(ActionEvent event) {
        btnAddGreeting.setSelected(false);
        greetingField.setDisable(true);
        GreetingLabelIfYes.setDisable(true);
    }

    /**
     * Returns to payment page that contains all filled data.
     * @param event
     * @throws IOException
     */
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

    /**
     * Validate all data in current page and proceeds to payment page.
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnProceed(ActionEvent event) throws IOException {
        if(validateInput()) {
            if (btnIamNotTheRecipient.isSelected()){
                Client.orderController.getCurrentOrder().setRecipientName(nameField.getText());
                Client.orderController.getCurrentOrder().setRecipientPhone(phoneField.getText());
            }
            else{
                Client.orderController.getCurrentOrder().setRecipientName(null);
                Client.orderController.getCurrentOrder().setRecipientPhone(null);
            }
            if (btnAddGreeting.isSelected()){
                Client.orderController.getCurrentOrder().setGreetingCard(greetingField.getText());
            }
            else{
                Client.orderController.getCurrentOrder().setGreetingCard(null);
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

    /**
     * Checks if all the data in the field entered correctly.
     * @return
     */
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
        if(btnAddGreeting.isSelected() && greetingField.getText().trim().isEmpty()){
            lblGreetingError.setVisible(true);
            invalidFields++;
        }
        if(invalidFields > 0){
            return false;
        }
        return true;
    }

    /**
     * View customer's cart.
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnViewCart(ActionEvent event) throws IOException {
        initCurrentOrder();
        Client.setScene(event, getClass().getResource("CartPage.fxml"));
    }
    /**
     * View order list
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnBrowseOrders(ActionEvent event) throws IOException {
        initCurrentOrder();
        Client.setScene(event, getClass().getResource("OrdersPage.fxml"));
    }

    /**
     * View catalog page.
     * @param event
     */
    @FXML
    void clickBtnBrowseCatalog(ActionEvent event) {
        initCurrentOrder();
        //TODO Combine
    }

    /**
     * Set order object in order controller to a new Order.
     */
    private void initCurrentOrder() {//TODO
        Client.orderController.setCurrentOrder(new Order());
    }
}

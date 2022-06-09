

package clientgui;

import client.Client;
import client.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import user.BranchEmployee;
import user.Customer;
import user.User;
import user.UserType;
import util.Alert;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BranchManagerCustomerManagementPageController implements Initializable {

    private User user;
    private Customer customer;
    private BranchEmployee branchEmployee;

    private final int creditCardCharsCount = 0;

    private final int cvvCharsCount = 0;

    @FXML // fx:id="newCustomerAnchorPane"
    private AnchorPane newCustomerAnchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="firstNameNewCustomerTextField"
    private TextField firstNameNewCustomerTextField; // Value injected by FXMLLoader

    @FXML // fx:id="lastNameNewCustomerTextField"
    private TextField lastNameNewCustomerTextField; // Value injected by FXMLLoader


    @FXML // fx:id="emailNewCustomerTextField"
    private TextField emailNewCustomerTextField; // Value injected by FXMLLoader


    @FXML // fx:id="idNewCustomerTextField"
    private TextField idNewCustomerTextField; // Value injected by FXMLLoader

    @FXML // fx:id="cardNewCustomerTextField4"
    private TextField cardNewCustomerTextField4; // Value injected by FXMLLoader

    @FXML // fx:id="cardNewCustomerTextField3"
    private TextField cardNewCustomerTextField3; // Value injected by FXMLLoader

    @FXML // fx:id="cardNewCustomerTextField2"
    private TextField cardNewCustomerTextField2; // Value injected by FXMLLoader

    @FXML // fx:id="cardNewCustomerTextField1"
    private TextField cardNewCustomerTextField1; // Value injected by FXMLLoader

    @FXML // fx:id="cvvNewCustomerTextField"
    private TextField cvvNewCustomerTextField; // Value injected by FXMLLoader

    @FXML // fx:id="monthNewCustomerComboBox"
    private ComboBox<String> monthNewCustomerComboBox; // Value injected by FXMLLoader

    @FXML // fx:id="yearNewCustomerComboBox"
    private ComboBox<String> yearNewCustomerComboBox; // Value injected by FXMLLoader


    @FXML // fx:id="phoneNewCustomerTextField"
    private TextField phoneNewCustomerTextField; // Value injected by FXMLLoader


    @FXML // fx:id="editCustomerAnchorPane"
    private AnchorPane editCustomerAnchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="actualBalanceLabel"
    private Label actualBalanceLabel; // Value injected by FXMLLoader

    @FXML // fx:id="editPermissionAnchorPane"
    private AnchorPane editPermissionAnchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="editCatalogueCheckBox"
    private CheckBox editCatalogueCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="activateDiscountCheckBox"
    private CheckBox activateDiscountCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="insertSurveyAnswersCheckBox"
    private CheckBox insertSurveyAnswersCheckBox; // Value injected by FXMLLoader


    @FXML // fx:id="enterIDTextField"
    private TextField enterIDTextField; // Value injected by FXMLLoader


    @FXML // fx:id="freezeCustomerCheckBox"
    private CheckBox freezeCustomerCheckBox; // Value injected by FXMLLoader

    @FXML // fx:id="firstErrorLabel"
    private Label firstErrorLabel; // Value injected by FXMLLoader

    @FXML // fx:id="messageEditEmployeeLabel"
    private Label messageEditEmployeeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="messageEditCustomerLabel"
    private Label messageEditCustomerLabel; // Value injected by FXMLLoader

    @FXML // fx:id="messageNewCustomerLabel"
    private Label messageNewCustomerLabel; // Value injected by FXMLLoader

    @FXML // fx:id="updateNewCustomerButton"
    private Button updateNewCustomerButton; // Value injected by FXMLLoader

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cardNewCustomerTextField1.textProperty().addListener((obs, oldText, newText)-> {
            if (oldText.length() < 4 && newText.length() >= 4)
                cardNewCustomerTextField2.requestFocus();
        });

        cardNewCustomerTextField2.textProperty().addListener((obs, oldText, newText)-> {
            if (oldText.length() <4  && newText.length() >= 4)
                cardNewCustomerTextField3.requestFocus();
        });

        cardNewCustomerTextField3.textProperty().addListener((obs, oldText, newText)-> {
            if (oldText.length() < 4 && newText.length() >= 4)
                cardNewCustomerTextField4.requestFocus();
        });

        cardNewCustomerTextField4.textProperty().addListener((obs, oldText, newText)-> {
            if (oldText.length() < 4 && newText.length() >= 4)
                cvvNewCustomerTextField.requestFocus();
        });

        clearPanes();
        clearMessages();
    }

    @FXML
    void clickBtnEnterID(ActionEvent event) {
        clearPanes();
        clearMessages();
        String userId = enterIDTextField.getText();
        List<String> userIdAndManagerId = new ArrayList<>();
        userIdAndManagerId.add(userId);
        userIdAndManagerId.add(Client.userController.getLoggedInUser().getId());
        user = Client.userController.getUserInformation(userIdAndManagerId);
        if(user == null) {
            clearPanes();
            showFirstErrorMessage("Wrong ID or not authorized user to edit");
        }
        else {
            switch (user.getUserType()) {
                case UNREGISTERED:
                    updateNewCustomerButton.setDisable(false);
                    newCustomerAnchorPane.setVisible(true);
                    setNewCustomerDetails(user);
                    break;
                case CUSTOMER:
                    Customer customer = Client.userController.getCustomerForInformation();
                    showCustomerBalance(customer);
                    cardNewCustomerTextField1.requestFocus();
                    break;
                case BRANCH_EMPLOYEE:
                    BranchEmployee branchEmployee = Client.userController.getBranchEmployeeForInformation();
                    showBranchEmployeePermissions(branchEmployee);
                    break;
                default:
                    showFirstErrorMessage("Wrong ID or not authorized user to edit");
                    break;
            }
        }

    }

    @FXML
    void clickBtnUpdateDetails(ActionEvent event) {
        clearMessages();
        if(validateFields()) {
            user.setFirstName(firstNameNewCustomerTextField.getText());
            user.setLastName(lastNameNewCustomerTextField.getText());
            user.setPhone(phoneNewCustomerTextField.getText());
            user.setEmail(emailNewCustomerTextField.getText());
            Customer newCustomer = new Customer(user);
            newCustomer.setBalance(0);
            String creditCard = cardNewCustomerTextField1.getText() + cardNewCustomerTextField2.getText()
                    + cardNewCustomerTextField3.getText() + cardNewCustomerTextField4.getText();
            newCustomer.setCreditCard(creditCard);
            newCustomer.setExpDate(monthNewCustomerComboBox.getValue() + "/" + yearNewCustomerComboBox.getValue());
            newCustomer.setCvv(cvvNewCustomerTextField.getText());

            boolean response = Client.userController.createNewCustomer(newCustomer);
            if(response) {
                showMessage("Create new customer succeed", false);
                updateNewCustomerButton.setDisable(true);
            }
            else showMessage("Create new customer failed",true);
        }
    }

    @FXML
    void clickBtnEditCustomer(ActionEvent event) {
        clearMessages();
        customer = new Customer(user);
        customer.setBlocked(freezeCustomerCheckBox.isSelected());
        boolean response =Client.userController.FreezeCustomer(customer);
        if(response)
            showMessage("Un/Freeze customer succeed",false);
        else showMessage("Un/Freeze customer failed",true);
    }



    @FXML
    void clickBtnEditBranchEmployee(ActionEvent event) {
        clearMessages();
        branchEmployee = new BranchEmployee(user);
        branchEmployee.setDiscount(activateDiscountCheckBox.isSelected());
        branchEmployee.setCatalogue(editCatalogueCheckBox.isSelected());
        branchEmployee.setSurvey(insertSurveyAnswersCheckBox.isSelected());
        boolean response =Client.userController.changeBranchEmployeePermissions(branchEmployee);
        if(response)
            showMessage("Branch employee change permission succeed",false);
        else showMessage("Branch employee change permission failed",true);
    }

    private void showBranchEmployeePermissions(BranchEmployee branchEmployee) {
        activateDiscountCheckBox.setSelected(branchEmployee.isDiscount());
        editCatalogueCheckBox.setSelected(branchEmployee.isCatalogue());
        insertSurveyAnswersCheckBox.setSelected(branchEmployee.isSurvey());
        editPermissionAnchorPane.setVisible(true);
    }

    private void showCustomerBalance(Customer customer) {
        float balance = customer.getBalance();
        actualBalanceLabel.setText(String.valueOf(balance));
        if(balance<0)
            actualBalanceLabel.setTextFill(Color.RED);
        else actualBalanceLabel.setTextFill(Color.GREEN);
        freezeCustomerCheckBox.setSelected(customer.isBlocked());
        editCustomerAnchorPane.setVisible(true);
    }

    private void setNewCustomerDetails(User user) {
        clearFields();
        firstNameNewCustomerTextField.setText(user.getFirstName());
        lastNameNewCustomerTextField.setText(user.getLastName());
        idNewCustomerTextField.setText(user.getId());
        emailNewCustomerTextField.setText(user.getEmail());
        phoneNewCustomerTextField.setText(user.getPhone());
        setComboBoxes();
        idNewCustomerTextField.setDisable(true);
    }

    private void setComboBoxes() {
        List<String> monthList = new ArrayList<String>();       //Set value of months.
        for (int i = 1; i <= 12; i++) {
            if (i<10){
                monthList.add("0" + i);
            }else {
                monthList.add(String.valueOf(i));
            }
        }
        ObservableList<String> months = FXCollections.observableArrayList(monthList);
        monthNewCustomerComboBox.getItems().addAll(months);

        List<String> yearList = new ArrayList<String>();       //Set value of years.
        for (int i = 2022; i <= 2035; i++) {
            yearList.add(String.valueOf(i));
        }
        ObservableList<String> years = FXCollections.observableArrayList(yearList);
        yearNewCustomerComboBox.getItems().addAll(years);
    }

    private boolean validateFields() {
        Boolean validate = true;
        String missingDetailsMsg = "Missing details, Please fill all fields";
        validate = validateDetailsTextField(firstNameNewCustomerTextField) && validateDetailsTextField(lastNameNewCustomerTextField)
                && validateDetailsTextField(phoneNewCustomerTextField) &&validateDetailsTextField(emailNewCustomerTextField)
                && validateDetailsComboBox(monthNewCustomerComboBox)&& validateDetailsComboBox(yearNewCustomerComboBox)
                && validCardNumber();
        if(!validate)
            showMessage(missingDetailsMsg,true);
        return validate;
    }

    /**
     * Verifies whether the credit card inserted contains only numbers and in the correct length
     * @return true if the number card inserted is valid
     */
    private boolean validCardNumber() {
        Border border = new Border(new BorderStroke(Color.INDIANRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        boolean res = true;
        if (cardNewCustomerTextField1.getText().length() != 4 && !cardNewCustomerTextField1.getText().contains("[0-9]+")){
            cardNewCustomerTextField1.setBorder(border);
            res= false;
        }
        if (cardNewCustomerTextField2.getText().length() != 4 && !cardNewCustomerTextField2.getText().contains("[0-9]+")){
            cardNewCustomerTextField2.setBorder(border);
            res= false;
        }
        if (cardNewCustomerTextField3.getText().length() != 4 && !cardNewCustomerTextField3.getText().contains("[0-9]+")){
            cardNewCustomerTextField3.setBorder(border);
            res= false;
        }
        if (cardNewCustomerTextField4.getText().length() != 4 && !cardNewCustomerTextField4.getText().contains("[0-9]+")){
            cardNewCustomerTextField4.setBorder(border);
            res= false;
        }
        if (cvvNewCustomerTextField.getText().length() != 3 && !cvvNewCustomerTextField.getText().contains("[0-9]+")){
            cvvNewCustomerTextField.setBorder(border);
            res= false;
        }
        return res;
    }

    private boolean validateDetailsTextField(TextField text)
    {
        Border border = new Border(new BorderStroke(Color.INDIANRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        if(text.getText() == null)
        {
            text.setBorder(border);
            return false;
        }
        return true;
    }

    private boolean validateDetailsComboBox(ComboBox comboBox)
    {
        Border border = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        if(comboBox.getValue() == null)
        {
            comboBox.setBorder(border);
            return false;
        }
        return true;
    }


    private void showMessage(String s,boolean error) {
        if(newCustomerAnchorPane.isVisible()) {
            messageNewCustomerLabel.setText(s);
            if(error)
                messageNewCustomerLabel.setTextFill(Color.RED);
            else messageNewCustomerLabel.setTextFill(Color.BLACK);
            messageNewCustomerLabel.setVisible(true);
        }
        else if (editCustomerAnchorPane.isVisible()) {
            messageEditCustomerLabel.setText(s);
            if(error)
                messageEditCustomerLabel.setTextFill(Color.RED);
            else messageEditCustomerLabel.setTextFill(Color.BLACK);
            messageEditCustomerLabel.setVisible(true);
        }
        else if(editPermissionAnchorPane.isVisible())
        {
            messageEditEmployeeLabel.setText(s);
            if(error)
                messageEditEmployeeLabel.setTextFill(Color.RED);
            else messageEditEmployeeLabel.setTextFill(Color.BLACK);
            messageEditEmployeeLabel.setVisible(true);
        }
    }

    private void showFirstErrorMessage(String s)
    {
        firstErrorLabel.setText(s);
        firstErrorLabel.setTextFill(Color.RED);
        firstErrorLabel.setVisible(true);
    }

    private void clearMessages()
    {
        firstErrorLabel.setVisible(false);
        messageEditEmployeeLabel.setVisible(false);
        messageEditEmployeeLabel.setVisible(false);
        messageNewCustomerLabel.setVisible(false);
    }

    private void clearPanes() {
        newCustomerAnchorPane.setVisible(false);
        editCustomerAnchorPane.setVisible(false);
        editPermissionAnchorPane.setVisible(false);
    }

    private void clearFields()
    {
        firstNameNewCustomerTextField.clear();
        lastNameNewCustomerTextField.clear();
        phoneNewCustomerTextField.clear();
        emailNewCustomerTextField.clear();
        idNewCustomerTextField.clear();
        cardNewCustomerTextField1.clear();
        cardNewCustomerTextField2.clear();
        cardNewCustomerTextField3.clear();
        cardNewCustomerTextField4.clear();
        cvvNewCustomerTextField.clear();
        monthNewCustomerComboBox.setValue("Month");
        yearNewCustomerComboBox.setValue("Year");
    }
}
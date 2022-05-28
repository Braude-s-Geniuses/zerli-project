

package clientgui;

import client.Client;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BranchManagerCustomerManagementPageController implements Initializable {

    private User user;
    private Customer customer;
    private BranchEmployee branchEmployee;

    private int creditCardCharsCount = 0;

    private int cvvCharsCount = 0;

    @FXML
    private AnchorPane newCustomerAnchorPane;

    @FXML
    private TextField firstNameNewCustomerTextField;

    @FXML
    private TextField lastNameNewCustomerTextField;


    @FXML
    private TextField emailNewCustomerTextField;


    @FXML
    private TextField idNewCustomerTextField;

    @FXML
    private TextField cardNewCustomerTextField4;

    @FXML
    private TextField cardNewCustomerTextField3;

    @FXML
    private TextField cardNewCustomerTextField2;

    @FXML
    private TextField cardNewCustomerTextField1;

    @FXML
    private TextField cvvNewCustomerTextField;

    @FXML
    private ComboBox<String> monthNewCustomerComboBox;

    @FXML
    private ComboBox<String> yearNewCustomerComboBox;


    @FXML
    private TextField phoneNewCustomerTextField;


    @FXML
    private AnchorPane editCustomerAnchorPane;

    @FXML // fx:id="actualBalanceLabel"
    private Label actualBalanceLabel;

    @FXML // fx:id="editPermissionAnchorPane"
    private AnchorPane editPermissionAnchorPane;

    @FXML // fx:id="editCatalogueCheckBox"
    private CheckBox editCatalogueCheckBox;

    @FXML
    private CheckBox activateDiscountCheckBox;

    @FXML
    private CheckBox insertSurveyAnswersCheckBox;


    @FXML
    private TextField enterIDTextField;


    @FXML
    private CheckBox freezeCustomerCheckBox;

    @FXML
    private Label firstErrorLabel;

    @FXML
    private Label messageEditEmployeeLabel;

    @FXML
    private Label messageEditCustomerLabel;

    @FXML
    private Label messageNewCustomerLabel;

    @FXML
    private Button updateNewCustomerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
            String creditCard = cardNewCustomerTextField1.getText()+"-" + cardNewCustomerTextField2.getText()+"-"
                    + cardNewCustomerTextField3.getText()+"-" + cardNewCustomerTextField4.getText();
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

    @FXML
    void creditCardKeyTyped(KeyEvent event) {
        creditCardCharsCount++;
        switch (creditCardCharsCount){
            case 4:
                cardNewCustomerTextField2.requestFocus();
                break;
            case 8:
                cardNewCustomerTextField3.requestFocus();
                break;
            case 12:
                cardNewCustomerTextField4.requestFocus();
                break;
            case 16:
                cvvNewCustomerTextField.requestFocus();
                break;
        }
    }

    @FXML
    void cvvKeyTyped(KeyEvent event) {
        cvvCharsCount++;
        if(cvvCharsCount == 3)
            monthNewCustomerComboBox.requestFocus();
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
                monthList.add("0" + String.valueOf(i));
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
}
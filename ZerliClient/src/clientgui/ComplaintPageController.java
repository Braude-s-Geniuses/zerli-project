package clientgui;

import branch.Complaint;
import branch.ComplaintStatus;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.String.valueOf;

public class ComplaintPageController implements Initializable {

    @FXML
    private TextField orderIDText;

    @FXML
    private TextArea complaintText;

    @FXML
    private Button submitComplaintBtn;
    @FXML
    private TextField customerUsername;
    @FXML
    private Label servicerID;
    @FXML
    private Label emptyLabel;
    @FXML
    private Button backBtn;

    /**
     * Initializes the controller
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        orderIDText.clear();
        customerUsername.clear();
        complaintText.clear();
        backBtn.getStyleClass().add("btn-red");

        if (Client.userController.getLoggedInUser() != null)
            servicerID.setText(valueOf(Client.userController.getLoggedInUser().getUserId()));
    }

    /**
     * This function will create new complaint in the system with few constraints
     *
     * @param actionEvent
     */
    public void submitComplaintClick(ActionEvent actionEvent) {
        if (customerUsername.getText().isEmpty() || orderIDText.getText().isEmpty()) {
            emptyLabel.setText("You must enter Customer's Username and Order ID");
            emptyLabel.setTextFill(Color.RED);
            emptyLabel.setVisible(true);
        } else
            emptyLabel.setVisible(false);

        if (!customerUsername.getText().isEmpty() && !orderIDText.getText().isEmpty()) {

            String customerUsernameCheck = customerUsername.getText();
            int orderIDCheck = Integer.parseInt(orderIDText.getText());

            //validating that there is such username and order connected to him in database and response accordingly
            Client.complaintController.validateCustomerAndOrder(customerUsernameCheck, orderIDCheck);
            ArrayList<Object> resultValidation = (ArrayList<Object>) Client.complaintController.getValidationResult().getData();

            if (resultValidation.get(0).equals("No such order connected to this username in database")) {
                emptyLabel.setText("No such order connected to this username");
                emptyLabel.setTextFill(Color.RED);
                emptyLabel.setVisible(true);

            } else if (resultValidation.get(0).equals("No such username in database")) {
                emptyLabel.setText("No such username , provide valid one!");
                emptyLabel.setTextFill(Color.RED);
                emptyLabel.setVisible(true);

            } else if (complaintText.getText().isEmpty()) {
                emptyLabel.setText("Please provide a complaint!");
                emptyLabel.setTextFill(Color.RED);
                emptyLabel.setVisible(true);
            } else { // After validation if all checks passed the complaint will be added to database
                int myID = Client.userController.getLoggedInUser().getUserId();
                String complaintTextToSend = complaintText.getText();
                int customerIDtoSend = (int) resultValidation.get(1);
                Client.complaintController.setComplaint(new Complaint(0, customerIDtoSend, myID, orderIDCheck, ComplaintStatus.OPENED, new Timestamp(System.currentTimeMillis()), complaintTextToSend));
                emptyLabel.setText("Complaint saved successfully!");
                emptyLabel.setTextFill(Color.GREEN);
                emptyLabel.setVisible(true);
                orderIDText.setDisable(true);
                complaintText.setDisable(true);
                customerUsername.setDisable(true);
                submitComplaintBtn.setDisable(true);
            }
        }
    }

    /**
     * Returns to the complaints page
     * @param actionEvent
     */
    public void backBtnClick(ActionEvent actionEvent) {
        MainDashboardController.setContentFromFXML("MyComplaintsPage.fxml");
    }
}
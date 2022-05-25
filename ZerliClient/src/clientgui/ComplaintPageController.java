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
    private TextField orderIDtext;

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        orderIDtext.clear();
        customerUsername.clear();
        complaintText.clear();
        backBtn.getStyleClass().add("btn-red");

        if (Client.userController.getLoggedInUser() != null)
            servicerID.setText(valueOf(Client.userController.getLoggedInUser().getUserId()));
        else {
            servicerID.setText("No Service person logged in! , Please log in");//Maybe will be deleted because no one can access this page unless servicer logged in
            servicerID.setTextFill(Color.RED);
            orderIDtext.setDisable(true);
            customerUsername.setDisable(true);
            complaintText.setDisable(true);
            submitComplaintBtn.setDisable(true);
        }


    }


    public void submitComplaintClick(ActionEvent actionEvent) {


        if (customerUsername.getText().isEmpty() || orderIDtext.getText().isEmpty()) {
            emptyLabel.setText("You must enter Customer's Username and Order ID");
            emptyLabel.setTextFill(Color.RED);
            emptyLabel.setVisible(true);
        } else
            emptyLabel.setVisible(false);


        if (!customerUsername.getText().isEmpty() && !orderIDtext.getText().isEmpty()) {

            String customerUsernameCheck = customerUsername.getText();
            int orderIDCheck = Integer.parseInt(orderIDtext.getText());

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
            } else {
                int myID = Client.userController.getLoggedInUser().getUserId();
                String complaintTextToSend = complaintText.getText();
                int customerIDtoSend = (int) resultValidation.get(1);
                Client.complaintController.setComplaint(new Complaint(0, customerIDtoSend, myID, orderIDCheck, ComplaintStatus.OPENED, new Timestamp(System.currentTimeMillis()), complaintTextToSend));
                emptyLabel.setText("Complaint saved successfully!");
                emptyLabel.setTextFill(Color.GREEN);
                emptyLabel.setVisible(true);
                orderIDtext.setDisable(true);
                complaintText.setDisable(true);
                customerUsername.setDisable(true);
                submitComplaintBtn.setDisable(true);

            }


        }


    }


    public void backBtnClick(ActionEvent actionEvent) {
        MainDashboardController.setContentFromFXML("MyComplaintsPage.fxml");
    }
}
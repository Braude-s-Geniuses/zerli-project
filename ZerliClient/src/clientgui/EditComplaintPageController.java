package clientgui;

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
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.String.valueOf;

public class EditComplaintPageController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea complaintText;

    @FXML
    private Label servicerID;

    @FXML
    private Label orderID;

    @FXML
    private Label customerID;
    @FXML
    private Button backBtn;
    @FXML
    private Button closeBtn;
    @FXML
    private TextField amount;
    @FXML
    private Label closeIssue;

    /**
     * Initializes the controller with the selected complaint from previous page
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

        backBtn.getStyleClass().add("btn-red");
        closeBtn.getStyleClass().add("btn");
        if (MyComplaintsPageController.currentComplaint != null) {
            servicerID.setText(valueOf(MyComplaintsPageController.currentComplaint.getServiceId()));
            orderID.setText(valueOf(MyComplaintsPageController.currentComplaint.getOrderId()));
            customerID.setText(valueOf(MyComplaintsPageController.currentComplaint.getCustomerId()));
            complaintText.setText(MyComplaintsPageController.currentComplaint.getDescription());
        }


    }

    /**
     * Function to close the selected complaint. The service-employee can choose whenever to give
     * compensation or not and will be added to the customers balance
     *
     * @param actionEvent
     */
    public void closeBtnClick(ActionEvent actionEvent) {
        ArrayList<Object> msgToSend = new ArrayList<>();
        msgToSend.add(MyComplaintsPageController.currentComplaint.getComplaintId());
        msgToSend.add(MyComplaintsPageController.currentComplaint.getCustomerId());
        try {
            if (!amount.getText().isEmpty() && Double.parseDouble(amount.getText()) >= 0) {
                msgToSend.add(Double.parseDouble(amount.getText()));
                Client.complaintController.closeStatus(msgToSend);
            } else {
                closeIssue.setText("Please input valid value");
                closeIssue.setTextFill(Color.RED);
                return;
            }
        } catch (Exception e) {
            closeIssue.setText("Please input valid value");
            closeIssue.setTextFill(Color.RED);
            return;
        }
        Client.complaintController.closeStatus(msgToSend);

        if (Client.complaintController.getStatusClosed().equals("Added Successfully")) {
            closeIssue.setText("Close issue done successfully!");
            closeIssue.setTextFill(Color.GREEN);
            closeBtn.setDisable(true);
            complaintText.setDisable(true);
            amount.setDisable(true);
        } else {
            closeIssue.setText("Something gone wrong!");
            closeIssue.setTextFill(Color.RED);
        }
    }

    /**
     * Returns to complaint page
     * @param actionEvent
     */
    public void backBtnClick(ActionEvent actionEvent) {
        MainDashboardController.setContentFromFXML("MyComplaintsPage.fxml");
    }
}
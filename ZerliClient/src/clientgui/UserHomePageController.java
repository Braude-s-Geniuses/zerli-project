package clientgui;

import complaint.Complaint;
import complaint.ComplaintStatus;
import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import user.Customer;
import user.UserType;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserHomePageController implements Initializable {

    @FXML
    private AnchorPane contentPane;
    @FXML
    private Label lblUserRole;

    @FXML
    private Label lbluserFullname;

    @FXML
    private AnchorPane picturePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUserRole.setText(Client.userController.getLoggedInUser().getUserType().toString());
        lbluserFullname.setText(Client.userController.getLoggedInUser().getFirstName() + " " + Client.userController.getLoggedInUser().getLastName());

        if(Client.userController.getLoggedInUser().getUserType() == UserType.CUSTOMER && ((Customer) Client.userController.getLoggedInUser()).isBlocked())
            lblUserRole.setText(lblUserRole.getText() + " (Frozen Account)");

        if(Client.userController.getLoggedInUser().getUserType() == UserType.SERVICE_EMPLOYEE) {
            Client.complaintController.getAllComplaints();
            ListView listView = new ListView();
            listView.setPrefWidth(250);
            Label lblMsg = new Label("Overdue Complaints: ");
            lblMsg.getStyleClass().add("details-label");
            lblMsg.setLayoutX(25);
            lblMsg.setLayoutY(150);
            listView.getStyleClass().add("list-view-msg");
            listView.setPlaceholder(new Label("No complaints are overdue"));
            listView.setLayoutX(20);
            listView.setLayoutY(180);
            listView.setMaxHeight(150);
            listView.setMinWidth(350);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ArrayList<Complaint> result = Client.complaintController.getListOfComplaints();

            for (Complaint p : result) {
                if (MyComplaintsPageController.calculateDifference(now, p.getCreatedAt(), "hours") > 24 && p.getServiceId() == Client.userController.getLoggedInUser().getUserId() && p.getComplaintStatus() == ComplaintStatus.OPENED) {
                    Label message = new Label("Please respond to the customer ASAP with id: " +p.getCustomerId());
                    listView.getItems().add(message);
                }
            }

            contentPane.getChildren().add(listView);
            contentPane.getChildren().add(lblMsg);
        }
    }
}
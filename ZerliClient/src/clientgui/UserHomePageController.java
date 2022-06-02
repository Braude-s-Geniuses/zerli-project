package clientgui;

import branch.Complaint;
import branch.ComplaintStatus;
import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
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

        if(Client.userController.getLoggedInUser().getUserType() == UserType.SERVICE_EMPLOYEE) {
            Client.complaintController.getAllComplaints();
            ListView listView = new ListView();
            Label lblMsg = new Label("Messages: ");
            lblMsg.getStyleClass().add("details-label");
            lblMsg.setLayoutX(25);
            lblMsg.setLayoutY(150);
            listView.getStyleClass().add("list-view-msg");
            listView.setPlaceholder(new Label("No complaints in this list"));
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
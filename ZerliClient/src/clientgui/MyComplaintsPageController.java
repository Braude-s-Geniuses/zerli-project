package clientgui;

import complaint.Complaint;
import complaint.ComplaintStatus;
import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyComplaintsPageController implements Initializable {

    public static Complaint currentComplaint;
    @FXML
    private TableView<Complaint> complaintsTable;

    @FXML
    private TableColumn<Complaint, Integer> complaintIdColumn;

    @FXML
    private TableColumn<Complaint, String> customerIdColumn;

    @FXML
    private TableColumn<Complaint, Integer> servicerIdColumn;
    @FXML
    private TableColumn<Complaint, Integer> orderIdColumn;
    @FXML
    private TableColumn<Complaint, ComplaintStatus> statusColumn;

    @FXML
    private TableColumn<Complaint, Timestamp> dateTimeColumn;
    @FXML
    private TableColumn<Complaint, String> descriptionColumn;

    @FXML
    private Label errLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        complaintIdColumn.setCellValueFactory(new PropertyValueFactory<Complaint, Integer>("complaintId"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Complaint, String>("customerId"));
        servicerIdColumn.setCellValueFactory(new PropertyValueFactory<Complaint, Integer>("serviceId"));
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<Complaint, Integer>("orderId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Complaint, ComplaintStatus>("complaintStatus"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<Complaint, Timestamp>("createdAt"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Complaint, String>("description"));

        Client.complaintController.getAllComplaints();

        ArrayList<Complaint> result = Client.complaintController.getListOfComplaints();

        if (result != null) {
            ObservableList<Complaint> complaints = FXCollections.observableArrayList(result);
            complaintsTable.setItems(complaints);

            complaintsTable.setRowFactory(tv -> new TableRow<Complaint>() {
                @Override
                protected void updateItem(Complaint item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null)
                        setStyle("");
                    else if(item.getComplaintStatus() == ComplaintStatus.CLOSED){
                        setDisable(true);
                        getStyleClass().add("table-row-cell");
                    }
                }
            });
        }

    }

    public void showDetailsClick(ActionEvent actionEvent) {
        if(complaintsTable.getSelectionModel().getSelectedItem() !=null && complaintsTable.getSelectionModel().getSelectedItem().getServiceId() == Client.userController.getLoggedInUser().getUserId()) {
            currentComplaint = complaintsTable.getSelectionModel().getSelectedItem();
            errLabel.setVisible(false);
            MainDashboardController.setContentFromFXML("EditComplaintPage.fxml");
        }
        else{
            errLabel.setVisible(true);
            errLabel.setTextFill(Color.RED);
            errLabel.setText("Please choose complaint connected to your ID");
        }


    }

    public void addComplaintClick(ActionEvent actionEvent) {
        MainDashboardController.setContentFromFXML("ComplaintPage.fxml");
    }

    static Long calculateDifference(Timestamp date1, Timestamp date2, String value) {

        long milliseconds = date1.getTime() - date2.getTime();
        if (value.equals("second"))
            return milliseconds / 1000;
        if (value.equals("minute"))
            return milliseconds / 1000 / 60;
        if (value.equals("hours"))
            return milliseconds / 1000 / 3600;
        else
            return 999999999L;
    }

}
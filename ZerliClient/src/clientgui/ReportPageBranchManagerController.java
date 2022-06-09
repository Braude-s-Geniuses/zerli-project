package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class ReportPageBranchManagerController implements Initializable {
    ObservableList<String> monthList = FXCollections.observableArrayList("01", "02", "03", "04" , "05", "06", "07", "08", "09", "10", "11", "12");

    @FXML
    private ComboBox<String> cbReportType;

    @FXML
    private Label labelTime;

    @FXML
    private ComboBox<String> cbMonth;

    @FXML
    private ComboBox<String> cbYear;

    @FXML
    private Button btnShowReport;

    @FXML
    private Button btnBack;

    @FXML
    private Label lblErrorInDetails;

    @FXML
    private Label lblErrorInReportType;

    @FXML
    void cbMonthTimeBranchSelect(ActionEvent event) {
        if(!cbMonth.getSelectionModel().isEmpty()  && !cbYear.getSelectionModel().isEmpty() ){
            lblErrorInDetails.setVisible(false);
        }
    }

    @FXML
    void cbReportTypeSelect(ActionEvent event) {
        lblErrorInReportType.setVisible(false);
    }

    @FXML
    void cbYearSelect(ActionEvent event) {
        cbMonthTimeBranchSelect(new ActionEvent());
        limitCbMonth();
    }
    /**
     * Limits the month user can choose in current year (only the months that have passed)
     */
    void limitCbMonth(){
        if(String.valueOf(LocalDate.now().getYear()).equals(cbYear.getValue())){
            ObservableList<String> month = FXCollections.observableArrayList();
            for (int i = 1; i < LocalDate.now().getMonthValue() ; i++) {
                month.addAll(monthList.get(i-1));
            }
            cbMonth.getItems().clear();
            cbMonth.getItems().addAll(month);
        }
        else {
            String month;
            if(cbMonth.getValue() != null) {
                month = cbMonth.getValue();
                cbMonth.getItems().clear();
                cbMonth.getItems().addAll(monthList);
                cbMonth.setValue(month);
            }else {
                cbMonth.getItems().clear();
                cbMonth.getItems().addAll(monthList);
            }
        }
    }

    @FXML
    void clickBtnBack(ActionEvent event) {
        MainDashboardController.setContentFromFXML("BrowseCatalogPage.fxml");
    }

    /**
     * Prepares report details and opens a report page
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnViewReport(ActionEvent event) throws IOException {
        if(validData()) {
            ArrayList<String> data = new ArrayList<>();
            String date;
            data.add(cbReportType.getValue().substring(0, cbReportType.getValue().length() - 7));
            date = cbYear.getValue() + "-" + cbMonth.getValue();
            data.add(date);
            Client.reportController.getManagersBranch(Client.userController.getLoggedInUser().getUserId());
            data.add((String) Client.reportController.getService().getResponse().getData());

            ReportPageController.reportData.addAll(data);

            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("ReportPage.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("Zerli Client");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }

    /**
     * checks if all data has been filled by the user, if not shows an Error label
     * @return true if user filled everything, false if not
     */
    private boolean validData() {
        int flag = 0;
        if( cbMonth.getSelectionModel().isEmpty()  || cbYear.getSelectionModel().isEmpty() ){
            lblErrorInDetails.setVisible(true);
            flag++;
        }

        if(cbReportType.getSelectionModel().isEmpty() ){
            lblErrorInReportType.setVisible(true);
            flag++;
        }
        return flag <= 0;
    }

    /**
     *
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
        List<String> reportList = new ArrayList<String>();
        reportList.add("Order report");
        reportList.add("Revenue report");
        reportList.add("Complaints report");
        ObservableList<String> reports = FXCollections.observableArrayList(reportList);
        cbReportType.getItems().addAll(reports);

        List<String> yearList = new ArrayList<String>();
        for (int i = 2020; i <= LocalDate.now().getYear(); i++) {
            yearList.add(String.valueOf(i));
        }

        ObservableList<String> years = FXCollections.observableArrayList(yearList);
        cbYear.getItems().addAll(years);
        cbMonth.getItems().addAll(monthList);



    }
}
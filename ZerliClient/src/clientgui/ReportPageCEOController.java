package clientgui;

import client.Client;
import communication.Message;
import communication.MessageFromServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class ReportPageCEOController implements Initializable{
    ObservableList<String> monthList = FXCollections.observableArrayList("01", "02", "03", "04" , "05", "06", "07", "08", "09", "10", "11", "12");
    ObservableList<String> quarterList = FXCollections.observableArrayList("01", "02", "03", "04");
    @FXML
    private Label lblErrorInDetails;

    @FXML
    private Label lblErrorInDetailsCompare;

    @FXML
    private Label lblErrorInReportType;
    @FXML
    private Label labelTime;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnMonthlyReports;

    @FXML
    private ToggleButton btnMonth;

    @FXML
    private ToggleButton btnMonthForCompare;

    @FXML
    private ToggleButton btnQuarter;

    @FXML
    private ToggleButton btnQuarterForCompare;

    @FXML
    private Button btnShowReport;

    @FXML
    private CheckBox cbCompare;

    @FXML
    private ComboBox<String> cbBranch;

    @FXML
    private ComboBox<String> cbBranchForCompare;

    @FXML
    private ComboBox<String> cbReportType;

    @FXML
    private ComboBox<String> cbTime;

    @FXML
    private ComboBox<String> cbTimeForCompare;

    @FXML
    private ComboBox<String> cbYear;

    @FXML
    private ComboBox<String> cbYearCompare;

    @FXML
    private Label labelBranchCompare;

    @FXML
    private Label labelTimeCompare;

    @FXML
    private Label labelYearCompare;

    @FXML
    void clickBtnBack(ActionEvent event) {
        MainDashboardController.setContentFromFXML("BrowseCatalogPage.fxml");
    }
    @FXML
    void cbReportTypeSelect(ActionEvent event) {
        lblErrorInReportType.setVisible(false);
    }

    /**
     * When person starts fill the missing data, Error label disappears
     * @param event
     */
    @FXML
    void cbTimeBranchCompareSelect(ActionEvent event) {
        if(!cbBranchForCompare.getSelectionModel().isEmpty() && !cbTimeForCompare.getSelectionModel().isEmpty() && !cbYearCompare.getSelectionModel().isEmpty()){
            lblErrorInDetailsCompare.setVisible(false);
        }
    }

    /**
     * When person starts fill the missing data, Error label disappears
     * @param event
     */
    @FXML
    void cbTimeBranchSelect(ActionEvent event) {
        if(!cbBranch.getSelectionModel().isEmpty()  && !cbTime.getSelectionModel().isEmpty()  && !cbYear.getSelectionModel().isEmpty() ){
            lblErrorInDetails.setVisible(false);
        }
    }

    @FXML
    void cbYearSelect(ActionEvent event) {
        cbTimeBranchSelect(new ActionEvent());
        if(btnMonth.isSelected())
            limitCbTimeMonthly();
        else limitCbTimeQuarterly(false);

    }

    @FXML
    void cbYearCompareSelect(ActionEvent event) {
        cbTimeBranchCompareSelect(new ActionEvent());
        limitCbTimeQuarterly(true);
    }

    /**
     * Limits the month user can choose in current year (only the months that have passed)
     */
    void limitCbTimeMonthly(){
        if(String.valueOf(LocalDate.now().getYear()).equals(cbYear.getValue())){
            ObservableList<String> month = FXCollections.observableArrayList();
            for (int i = 1; i < LocalDate.now().getMonthValue() ; i++) {
                month.addAll(monthList.get(i-1));
            }
            cbTime.getItems().clear();
            cbTime.getItems().addAll(month);
        }
        else {
            String month = null;
            if(cbTime.getValue() != null) {
                month = cbTime.getValue();
                cbTime.getItems().clear();
                cbTime.getItems().addAll(monthList);
                cbTime.setValue(month);
            }else {
                cbTime.getItems().clear();
                cbTime.getItems().addAll(monthList);
            }
        }
    }

    /**
     * Limits the quarter user can choose in current year (only the quarters that have passed)
     * @param compare
     */
    void limitCbTimeQuarterly(boolean compare){
        if(compare){
            if(String.valueOf(LocalDate.now().getYear()).equals(cbYearCompare.getValue())){
                ObservableList<String> quarter = FXCollections.observableArrayList();
                for (int i = 0; i < ((LocalDate.now().getMonthValue()-1)/3) ; i++) {
                    quarter.addAll(quarterList.get(i));
                }
                cbTimeForCompare.getItems().clear();
                cbTimeForCompare.getItems().addAll(quarter);
            }else{
                String quarter;
                if(cbTimeForCompare.getValue() != null) {
                    quarter = cbTimeForCompare.getValue();
                    cbTimeForCompare.getItems().clear();
                    cbTimeForCompare.getItems().addAll(quarterList);
                    cbTimeForCompare.setValue(quarter);
                }else {
                    cbTimeForCompare.getItems().clear();
                    cbTimeForCompare.getItems().addAll(quarterList);
                }
            }
        }else{
            if(String.valueOf(LocalDate.now().getYear()).equals(cbYear.getValue())){
                ObservableList<String> quarter = FXCollections.observableArrayList();
                for (int i = 0; i < ((LocalDate.now().getMonthValue()-1)/3) ; i++) {
                    quarter.addAll(quarterList.get(i));
                }
                cbTime.getItems().clear();
                cbTime.getItems().addAll(quarter);
            }
            else{
                String quarter;
                if(cbTime.getValue() != null) {
                    quarter = cbTime.getValue();
                    cbTime.getItems().clear();
                    cbTime.getItems().addAll(quarterList);
                    cbTime.setValue(quarter);
                }else {
                    cbTime.getItems().clear();
                    cbTime.getItems().addAll(quarterList);
                }
            }
        }

    }

    /**
     * Passes to the part in page that relates to month reports, initialized the month combo box
     * @param event
     */
    @FXML
    void clickBtnMonth(ActionEvent event) {
        btnMonth.setSelected(true);
        btnQuarter.setSelected(false);
        labelTime.setText("Select Month");
        cbTime.getItems().clear();
        cbTime.getItems().addAll(monthList);
        cbTime.setPromptText("month");
        setCompareInvisible(false);
        cbCompare.setSelected(false);
        if(cbYear.getValue() != null && cbYear.getValue().equals(String.valueOf(LocalDate.now().getYear())))
            limitCbTimeMonthly();

    }

    /**
     * The compare button is not selected so the user can choose to see report for compare
     * @param toVisible
     */
    private void setCompareInvisible(boolean toVisible) {
        cbCompare.setVisible(toVisible);
        labelYearCompare.setVisible(toVisible);
        labelTimeCompare.setVisible(toVisible);
        cbTimeForCompare.setVisible(toVisible);
        cbYearCompare.setVisible(toVisible);
        labelBranchCompare.setVisible(toVisible);
        cbBranchForCompare.setVisible(toVisible);
    }

    /**
     * Passes to the part in page that relates to quarter reports, initialized the quarter combo box
     * @param event
     */
    @FXML
    void clickBtnQuarter(ActionEvent event) {
        btnQuarter.setSelected(true);
        setCompareInvisible(true);
        setCompare(true);
        btnMonth.setSelected(false);
        labelTime.setText("Select Quarter ");
        cbTime.getItems().clear();
        cbTime.getItems().addAll(quarterList);
        cbTime.setPromptText("Quarter");
        if(cbYear.getValue() != null && cbYear.getValue().equals(String.valueOf(LocalDate.now().getYear()))) {
            limitCbTimeQuarterly(false);
        }
        if(cbYearCompare.getValue() != null && cbYear.getValue().equals(String.valueOf(LocalDate.now().getYear()))) {
            limitCbTimeQuarterly(true);
        }
    }

    /**
     * Enables the user to choose report for compare
     * @param event
     */
    @FXML
    void clickCheckBoxCompare(ActionEvent event) {
        setCompare(!cbCompare.isSelected());
    }

    /**
     * Hides/ shows the fields of compare reports
     * @param toDisable
     */
    private void setCompare(boolean toDisable) {
        labelYearCompare.setDisable(toDisable);
        labelTimeCompare.setDisable(toDisable);
        cbTimeForCompare.setDisable(toDisable);
        cbYearCompare.setDisable(toDisable);
        labelBranchCompare.setDisable(toDisable);
        cbBranchForCompare.setDisable(toDisable);
    }

    /**
     * Asking from controller the wanted report and prepares it for showing to the user
     * @param event
     * @throws IOException
     */
    @FXML
    void clickBtnViewReport(ActionEvent event) throws IOException {
        if(validData()) {
            if(!cbCompare.isSelected()) {
                ArrayList<String> data = new ArrayList<>();
                String timePeriod;
                data.add(cbReportType.getValue().substring(0, cbReportType.getValue().length() - 7));
                if (btnQuarter.isSelected()) {
                    timePeriod = cbYear.getValue() + "-" + "quarter_" + cbTime.getValue();
                } else {
                    timePeriod = cbYear.getValue() + "-" + cbTime.getValue();
                }
                data.add(timePeriod);
                data.add(cbBranch.getValue());

                ReportPageController.reportData.addAll(data);

                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("ReportPage.fxml"));
                Scene scene = new Scene(root);

                primaryStage.setTitle("Zerli Client");
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.show();
            }else{
                ArrayList<String> data = new ArrayList<>();
                String timePeriod;
                data.add(cbReportType.getValue().substring(0, cbReportType.getValue().length() - 7));
                timePeriod = cbYear.getValue() + "-" + "quarter_" + cbTime.getValue();
                data.add(timePeriod);
                data.add(cbBranch.getValue());
                ReportCompareQuarterlyPageController.firstReportData.addAll(data);

                data.clear();
                data.add(cbReportType.getValue().substring(0, cbReportType.getValue().length() - 7));
                timePeriod = cbYearCompare.getValue() + "-" + "quarter_" + cbTimeForCompare.getValue();
                data.add(timePeriod);
                data.add(cbBranchForCompare.getValue());
                ReportCompareQuarterlyPageController.secondReportData.addAll(data);
                data.clear();

                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("ReportCompareQuarterlyPage.fxml"));
                Scene scene = new Scene(root);

                primaryStage.setTitle("Zerli Client");
                primaryStage.setScene(scene);
                primaryStage.setResizable(false);
                primaryStage.show();

            }
        }
    }



    /**
     * checks if all data has been filled by the user, if not shows an Error label
     * @return true if user filled everything, false if not
     */
    private boolean validData() {
        int flag = 0;
        if(cbBranch.getSelectionModel().isEmpty()  || cbTime.getSelectionModel().isEmpty()  || cbYear.getSelectionModel().isEmpty() ){
            lblErrorInDetails.setVisible(true);
            flag++;
        }
        if(cbCompare.isSelected()) {
            if ((cbBranchForCompare.getSelectionModel().isEmpty() || cbTimeForCompare.getSelectionModel().isEmpty() || cbYearCompare.getSelectionModel().isEmpty())) {
                lblErrorInDetailsCompare.setVisible(true);
                flag++;
            }
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
        cbYearCompare.getItems().addAll(years);

        btnQuarter.setSelected(true);   //Quarter option as default
        btnQuarter.fire();

        cbTime.setPromptText("Quarter");
        cbTimeForCompare.getItems().addAll(quarterList);
        cbTimeForCompare.setPromptText("Quarter");

        Client.orderController.getBranches();   //set branches.
        ObservableList<String> branches = FXCollections.observableArrayList((ArrayList<String>)Client.orderController.getService().getResponse().getData());
        cbBranch.getItems().addAll(branches);
        cbBranchForCompare.getItems().addAll(branches);
    }
}
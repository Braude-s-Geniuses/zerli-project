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
import javafx.stage.FileChooser;
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
    ObservableList<String> monthPicker = FXCollections.observableArrayList("01", "02", "03", "04" , "05", "06", "07", "08", "09", "10", "11", "12");
    ObservableList<String> quarterPicker = FXCollections.observableArrayList("01", "02", "03", "04");
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
    private CheckBox checkBoxCompare;

    @FXML
    private ComboBox<String> comboBoxBranch;

    @FXML
    private ComboBox<String> comboBoxBranchForCompare;

    @FXML
    private ComboBox<String> comboBoxReportType;

    @FXML
    private ComboBox<String> comboBoxTime;

    @FXML
    private ComboBox<String> comboBoxTimeForCompare;

    @FXML
    private ComboBox<String> comboBoxYear;

    @FXML
    private ComboBox<String> comboBoxYearForCompare;

    @FXML
    private Label labelBranchForCompare;

    @FXML
    private Label labelTimeForCompare;

    @FXML
    private Label labelYearForCompare;

    @FXML
    void clickBtnBack(ActionEvent event) {
        MainDashboardController.setContentFromFXML("BrowseCatalogPage.fxml");
    }
    @FXML
    void cbReportTypeSelect(ActionEvent event) {
        lblErrorInReportType.setVisible(false);
    }

    @FXML
    void cbTimeBranchCompareSelect(ActionEvent event) {
        if(!comboBoxBranchForCompare.getSelectionModel().isEmpty() && !comboBoxTimeForCompare.getSelectionModel().isEmpty() && !comboBoxYearForCompare.getSelectionModel().isEmpty()){
            lblErrorInDetailsCompare.setVisible(false);
        }
    }

    @FXML
    void cbTimeBranchSelect(ActionEvent event) {
        if(!comboBoxBranch.getSelectionModel().isEmpty()  && !comboBoxTime.getSelectionModel().isEmpty()  && !comboBoxYear.getSelectionModel().isEmpty() ){
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

    void limitCbTimeMonthly(){

        if(String.valueOf(LocalDate.now().getYear()).equals(comboBoxYear.getValue())){
            ObservableList<String> month = FXCollections.observableArrayList();
            for (int i = 1; i < LocalDate.now().getMonthValue() ; i++) {
                month.addAll(monthPicker.get(i-1));
            }
            comboBoxTime.getItems().clear();
            comboBoxTime.getItems().addAll(month);
        }
        else {
            comboBoxTime.getItems().clear();
            comboBoxTime.getItems().addAll(monthPicker);
        }
    }
    void limitCbTimeQuarterly(boolean compare){
        if(compare){
            if(String.valueOf(LocalDate.now().getYear()).equals(comboBoxYearForCompare.getValue())){
                ObservableList<String> quarter = FXCollections.observableArrayList();
                for (int i = 0; i < (LocalDate.now().getMonthValue()/3) ; i++) {
                    quarter.addAll(quarterPicker.get(i));
                }
                comboBoxTimeForCompare.getItems().clear();
                comboBoxTimeForCompare.getItems().addAll(quarter);
            }else{
                comboBoxTimeForCompare.getItems().clear();
                comboBoxTimeForCompare.getItems().addAll(quarterPicker);
            }
        }else{
            if(String.valueOf(LocalDate.now().getYear()).equals(comboBoxYear.getValue())){
                ObservableList<String> quarter = FXCollections.observableArrayList();
                for (int i = 0; i < (LocalDate.now().getMonthValue()/3) ; i++) {
                    quarter.addAll(quarterPicker.get(i));
                }
                comboBoxTime.getItems().clear();
                comboBoxTime.getItems().addAll(quarter);
            }
            else{
                comboBoxTime.getItems().clear();
                comboBoxTime.getItems().addAll(quarterPicker);
            }
        }

    }
    @FXML
    void clickBtnMonth(ActionEvent event) {
        btnMonth.setSelected(true);
        btnQuarter.setSelected(false);
        labelTime.setText("Select Month");
        comboBoxTime.getItems().clear();
        comboBoxTime.getItems().addAll(monthPicker);
        comboBoxTime.setPromptText("month");
        setCompareInvisible(false);
        checkBoxCompare.setSelected(false);
        if(comboBoxYear.getValue() != null && comboBoxYear.getValue().equals(String.valueOf(LocalDate.now().getYear())))
            limitCbTimeMonthly();

    }

    private void setCompareInvisible(boolean toVisible) {
        checkBoxCompare.setVisible(toVisible);
        labelYearForCompare.setVisible(toVisible);
        labelTimeForCompare.setVisible(toVisible);
        comboBoxTimeForCompare.setVisible(toVisible);
        comboBoxYearForCompare.setVisible(toVisible);
        labelBranchForCompare.setVisible(toVisible);
        comboBoxBranchForCompare.setVisible(toVisible);
    }

    @FXML
    void clickBtnQuarter(ActionEvent event) {
        btnQuarter.setSelected(true);
        setCompareInvisible(true);
        btnMonth.setSelected(false);
        labelTime.setText("Select Quarter ");
        comboBoxTime.getItems().clear();
        comboBoxTime.getItems().addAll(quarterPicker);
        comboBoxTime.setPromptText("Quarter");
        if(comboBoxYear.getValue() != null && comboBoxYear.getValue().equals(String.valueOf(LocalDate.now().getYear()))) {
            limitCbTimeQuarterly(false);
        }
        if(comboBoxYearForCompare.getValue() != null && comboBoxYear.getValue().equals(String.valueOf(LocalDate.now().getYear()))) {
            limitCbTimeQuarterly(true);
        }
    }

    @FXML
    void clickCheckBoxCompare(ActionEvent event) {
        setCompare(!checkBoxCompare.isSelected());
    }

    private void setCompare(boolean toDisable) {
        labelYearForCompare.setDisable(toDisable);
        labelTimeForCompare.setDisable(toDisable);
        comboBoxTimeForCompare.setDisable(toDisable);
        comboBoxYearForCompare.setDisable(toDisable);
        labelBranchForCompare.setDisable(toDisable);
        comboBoxBranchForCompare.setDisable(toDisable);
    }

    @FXML
    void clickBtnMonthlyReports(ActionEvent event) {

    }

    @FXML
    void clickBtnViewReport(ActionEvent event) throws IOException {
        if(validData()) {
            if(!checkBoxCompare.isSelected()) {
                ArrayList<String> data = new ArrayList<>();
                String timePeriod;
                data.add(comboBoxReportType.getValue().substring(0, comboBoxReportType.getValue().length() - 7));
                if (btnQuarter.isSelected()) {
                    timePeriod = comboBoxYear.getValue() + "-" + "quarter_" + comboBoxTime.getValue();
                } else {
                    timePeriod = comboBoxYear.getValue() + "-" + comboBoxTime.getValue();
                }
                data.add(timePeriod);
                data.add(comboBoxBranch.getValue());

                prepareReport(data);

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
                data.add(comboBoxReportType.getValue().substring(0, comboBoxReportType.getValue().length() - 7));
                timePeriod = comboBoxYear.getValue() + "-" + "quarter_" + comboBoxTime.getValue();
                data.add(timePeriod);
                data.add(comboBoxBranch.getValue());
                prepareReport(data);
                ReportCompareQuarterlyPageController.imageViewsFromFirstReport = (ArrayList<ImageView>) ReportPageController.imageViews.clone();
                ReportPageController.imageViews.clear();
                data.clear();
                //Prepare the second report.
                data.add(comboBoxReportType.getValue().substring(0, comboBoxReportType.getValue().length() - 7));
                timePeriod = comboBoxYearForCompare.getValue() + "-" + "quarter_" + comboBoxTimeForCompare.getValue();
                data.add(timePeriod);
                data.add(comboBoxBranchForCompare.getValue());
                prepareReport(data);
                ReportCompareQuarterlyPageController.imageViewsFromSecondReport = (ArrayList<ImageView>) ReportPageController.imageViews.clone();
                ReportPageController.imageViews.clear();
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

    public void prepareReport(ArrayList<String> reportData) {
        Message message;
        File pdfFile = null;
        FileChooser fileChooser = new FileChooser();
        ImageView iv = new ImageView();

        Client.reportController.viewReport(reportData);
        message = Client.reportController.getResponse();
        SerialBlob blob = (SerialBlob) message.getData();
        if (message.getAnswer() == MessageFromServer.ORDER_REPORT_IMPORTED_NOT_SUCCESSFULLY) {
            //
        } else {
            try {
                pdfFile = File.createTempFile("zerli", "report.pdf");
                pdfFile.deleteOnExit();
                FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);
                byte[] b = blob.getBytes(1, (int) blob.length());
                fileOutputStream.write(b);
                fileOutputStream.close();

                PDDocument document = PDDocument.load(pdfFile.getAbsoluteFile());
                ArrayList<PDPage> pdPages = (ArrayList<PDPage>) document.getDocumentCatalog().getAllPages();
                int page = 0;
                for (PDPage pdPage : pdPages)
                {

                    ++page;
                    BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 200);
                    Image image = SwingFXUtils.toFXImage(bim, null );

                    iv.setImage(image);
                    ReportPageController.imageViews.add(iv);
                    PDResources resources = pdPage.getResources();
                    Map images = resources.getImages();
                    if( images != null )
                    {
                        Iterator imageIter = images.keySet().iterator();
                        while( imageIter.hasNext() )
                        {
                            String key = (String)imageIter.next();
                            PDXObjectImage image2 = (PDXObjectImage)images.get( key );
                            String name = "zerli.jpg";
                            image2.write2file( name );
                            iv.setImage(image);
                            ReportPageController.imageViews.add(iv);
                        }
                    }

                    //iv.setFitHeight(600);
                    //iv.setFitWidth(750);
                    //vboxReport.getChildren().add(iv);
                }
                document.close();


            } catch (IOException | SerialException e) {
                throw new RuntimeException(e);
            }

        }

    }

    private boolean validData() {
        int flag = 0;
        if(comboBoxBranch.getSelectionModel().isEmpty()  || comboBoxTime.getSelectionModel().isEmpty()  || comboBoxYear.getSelectionModel().isEmpty() ){
            lblErrorInDetails.setVisible(true);
            flag++;
        }
        if(checkBoxCompare.isSelected()) {
            if ((comboBoxBranchForCompare.getSelectionModel().isEmpty() || comboBoxTimeForCompare.getSelectionModel().isEmpty() || comboBoxYearForCompare.getSelectionModel().isEmpty())) {
                lblErrorInDetailsCompare.setVisible(true);
                flag++;
            }
        }
        if(comboBoxReportType.getSelectionModel().isEmpty() ){
            lblErrorInReportType.setVisible(true);
            flag++;
        }
        return flag <= 0;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<String> reportList = new ArrayList<String>();
        reportList.add("Order report");
        reportList.add("Revenue report");
        reportList.add("Complaints report");
        ObservableList<String> reports = FXCollections.observableArrayList(reportList);
        comboBoxReportType.getItems().addAll(reports);

        List<String> yearList = new ArrayList<String>();
        for (int i = 2017; i <= LocalDate.now().getYear(); i++) {
            yearList.add(String.valueOf(i));
        }

        ObservableList<String> years = FXCollections.observableArrayList(yearList);
        comboBoxYear.getItems().addAll(years);
        comboBoxYearForCompare.getItems().addAll(years);

        btnQuarter.setSelected(true);   //Quarter option as default
        btnQuarter.fire();

        comboBoxTime.getItems().addAll(quarterPicker);
        comboBoxTime.setPromptText("Quarter");
        comboBoxTimeForCompare.getItems().addAll(quarterPicker);
        comboBoxTimeForCompare.setPromptText("Quarter");

        Client.orderController.getBranches();   //set branches.
        ObservableList<String> branches = FXCollections.observableArrayList((ArrayList<String>)Client.orderController.getResponse().getData());
        comboBoxBranch.getItems().addAll(branches);
        comboBoxBranchForCompare.getItems().addAll(branches);



    }

}
package clientgui;

import client.Client;
import client.SurveyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import survey.Survey;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class SurveySummaryUploadPageController implements Initializable {

    private SurveyController surveyController;

    private SerialBlob uploadedPdf;

    @FXML
    private ComboBox<String> surveyComboBox;

    @FXML
    private Button browseButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Label messageLabel;

    @FXML
    private Label fileNameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        surveyController = Client.surveyController;
        ObservableList<String> surveysList = FXCollections.observableArrayList(surveyController.getAllSurveys());
        surveyComboBox.setItems(surveysList);
    }

    public void browseButtonClick(ActionEvent event) {
        messageLabel.setVisible(false);
        fileNameLabel.setVisible(false);
        File selectedFile = browseFile();
        if(selectedFile != null) {
            fileNameLabel.setText(selectedFile.getName());
            fileNameLabel.setAlignment(Pos.CENTER);
            fileNameLabel.setVisible(true);
            try {
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                uploadedPdf = new SerialBlob(fileContent);
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Error, Please Try Again", true);
            }
        }
    }

    public void confirmButtonClick(ActionEvent event)
    {
        boolean returnValue = Client.surveyController.uploadSurveySummary(new Survey(surveyController.getSurveyID(surveyComboBox.getValue()), uploadedPdf));

        if(returnValue)
            showMessage("Upload Successfully", false);
        else
            showMessage("Upload Failed", true);
    }

    private File browseFile() {
        FileChooser fileChooser = new FileChooser ();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );

        return fileChooser.showOpenDialog(null);
    }

    private void showMessage(String message,boolean Error) {
        messageLabel.setText(message);
        messageLabel.setAlignment(Pos.CENTER);
        if(Error)
            messageLabel.setTextFill(Paint.valueOf("red"));
        messageLabel.setTextFill(Paint.valueOf("black"));
        messageLabel.setVisible(true);
    }
}
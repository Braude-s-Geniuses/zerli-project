package clientgui;

import client.Client;
import client.SurveyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import survey.SurveyAnswers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SurveySelectPageController implements Initializable {

    private SurveyController surveyController;
    @FXML
    private Label surveyLabel;

    @FXML
    private ComboBox<String> surveyComboBox;

    @FXML
    private Button showButton;

    @FXML
    private Label errorLabel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        surveyController = Client.surveyController;
        ObservableList<String> surveysList = FXCollections.observableArrayList(surveyController.getAllSurveys());
        surveyComboBox.setItems(surveysList);
    }

    @FXML
    public void showButtonClick(ActionEvent event)  {
        errorLabel.setVisible(false);
        SurveyDataPageController dataPage = new SurveyDataPageController();
        List<SurveyAnswers> surveyAnswers = surveyController.getAllSurveyAnswers(surveyComboBox.getValue());
        if(surveyAnswers == null)
            showErrorMessage("Load Survey Answers Fail, Please Try Again");
        else {
            dataPage.setSurveyAnswers(surveyAnswers);
            dataPage.setSurveyName(surveyComboBox.getValue());
            MainDashboardController.setContentFromFXML("SurveyDataPage.fxml");
        }
    }

    private void showErrorMessage(String message)
    {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
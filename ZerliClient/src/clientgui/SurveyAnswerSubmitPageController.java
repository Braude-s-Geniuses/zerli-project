package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import survey.SurveyAnswers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;




public class SurveyAnswerSubmitPageController implements Initializable {

    public SurveyAnswers answers = new SurveyAnswers(0,null,null);
    private int index=0;
    public List<List> surveyData = new ArrayList<List>();
    public List<Integer> surveyIDs = new ArrayList<Integer>();
    public List<String> surveyNames = new ArrayList<String>();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<String> comboBoxSurveys;

    @FXML
    private TextField username_lable;

    @FXML
    private Label messLable;
    @FXML
    private ToggleGroup answer_1=null;

    @FXML
    private ToggleGroup answer_2=null;

    @FXML
    private ToggleGroup answer_3=null;

    @FXML
    private ToggleGroup answer_4=null;

    @FXML
    private ToggleGroup answer_5=null;

    @FXML
    private ToggleGroup answer_6=null;

    /**
     *
     * Initialize method - displays to the user a clean screen with no comments,
     * no previous selections, no poll names or username.
     * The user has to select/type from the beginning.
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
        messLable.setVisible(false);
        surveyData = Client.surveyController.getSurveysIdsAndNames();
        surveyIDs = surveyData.get(0);
        surveyNames = surveyData.get(1);
        ObservableList<String> Names = FXCollections.observableArrayList(surveyNames);
        comboBoxSurveys.getItems().addAll(Names);
    }

    /**
     * This function gets a survey name from combo-box that the client chosen and
     * returns the index of its position within the list of survey names.
     *
     * @param event
     */
    @FXML
    void getIndex(ActionEvent event) {
        index=0;
        String survey = comboBoxSurveys.getValue();
        for(String str : surveyNames){
            if(str == survey){
                break;
            }
            index++;
        }
    }

    /**
     * This function creates a new survey.
     * Puts into the survey the customer's name, survey number and
     * the answers marked by the customer.
     */
    @FXML
    private void enterSurvey(){

        answers.setUsername(username_lable.getText());
        answers.setSurveyID(surveyIDs.get(index));

        List<Integer> toAnswers = new ArrayList<Integer>();
        String convertInt;

        RadioButton selectedRadioButton1 = (RadioButton) answer_1.getSelectedToggle();
        if(selectedRadioButton1 != null) {
            convertInt = selectedRadioButton1.getText();
            toAnswers.add(Integer.valueOf(convertInt));
        }
        RadioButton selectedRadioButton2 = (RadioButton) answer_2.getSelectedToggle();
        if(selectedRadioButton2 != null) {
            convertInt = selectedRadioButton2.getText();
            toAnswers.add(Integer.valueOf(convertInt));
        }
        RadioButton selectedRadioButton3 = (RadioButton) answer_3.getSelectedToggle();
        if(selectedRadioButton3 != null) {
            convertInt = selectedRadioButton3.getText();
            toAnswers.add(Integer.valueOf(convertInt));
        }
        RadioButton selectedRadioButton4 = (RadioButton) answer_4.getSelectedToggle();
        if(selectedRadioButton4 != null) {
            convertInt = selectedRadioButton4.getText();
            toAnswers.add(Integer.valueOf(convertInt));
        }
        RadioButton selectedRadioButton5 = (RadioButton) answer_5.getSelectedToggle();
        if(selectedRadioButton5 != null) {
            convertInt = selectedRadioButton5.getText();
            toAnswers.add(Integer.valueOf(convertInt));
        }
        RadioButton selectedRadioButton6 = (RadioButton) answer_6.getSelectedToggle();
        if(selectedRadioButton6 != null) {
            convertInt = selectedRadioButton6.getText();
            toAnswers.add(Integer.valueOf(convertInt));
        }

        answers.setAnswers(toAnswers);

        return;
    }

    /**
     * This function sends the survey completed by the user and displays a message accordingly.
     * First the function checks whether all the survey responses have been filled out, then sends another request,
     * displays a message accordingly.
     */
    @FXML
    private void sendSurvey() {
        enterSurvey();
        if (Client.surveyController.checkSurveyAnswersComplete(answers)) {
            switch (Client.surveyController.addSurveyAnswersOfCustomer(answers)) {

                case SURVEY_UNAUTHORIZED_CUSTOMER:
                    showErrorMessage("This customer is unable to complete this survey.");
                    break;
                case SURVEY_ALREADY_FILLED:
                    showErrorMessage("This customer already filled this survey.");
                    break;
                case SURVEY_INSERT_FAIL:
                    showErrorMessage("There is a problem, please try again later...");
                    break;
                case SURVEY_INSERT_SUCCESS:
                    showSuccessfulMessage("              Survey sent successfully!");
                    break;
                default:
                    break;
            }
        } else {
            showErrorMessage("                      Please fill in all answers.");
        }
    }

    /**
     * Shows Error label with given message
     * @param err - the message to show
     */
    private void showErrorMessage(String err) {
        messLable.setText(err);
        messLable.setTextFill(Color.RED);
        messLable.setVisible(true);
    }

    /**
     * Shows Success label with given message
     * @param message - the message to show
     */
    private void showSuccessfulMessage(String message) {
        messLable.setText(message);
        messLable.setTextFill(Color.GREEN);
        messLable.setVisible(true);
    }

    /**
     * This function resets the results of the previous survey.
     * @param event
     */
    @FXML
    void reset(ActionEvent event){
        MainDashboardController.setContentFromFXML("SurveyAnswerSubmitPage.fxml");
    }

}
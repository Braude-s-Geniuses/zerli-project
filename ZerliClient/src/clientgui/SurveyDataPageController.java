package clientgui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import survey.SurveyAnswers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SurveyDataPageController implements Initializable {

    private static final int numberOfAnswers = 10;
    private static final int numberOfQuestions = 6;

    private int[] answers;

    private int questionNumber = 1;


    private static String surveyName = null;

    private static List<SurveyAnswers> surveyAnswers;

    @FXML // fx:id="categoryAxis"
    private CategoryAxis categoryAxis; // Value injected by FXMLLoader

    @FXML // fx:id="numberAxis"
    private NumberAxis numberAxis; // Value injected by FXMLLoader

    @FXML // fx:id="questionNumberLabel"
    private Label questionNumberLabel; // Value injected by FXMLLoader

    @FXML // fx:id="surveyNameLabel"
    private Label surveyNameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="barChart"
    private BarChart<String, Number> barChart; // Value injected by FXMLLoader

    @FXML // fx:id="nextButton"
    private Button nextButton; // Value injected by FXMLLoader

    @FXML // fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        answers = new int[numberOfAnswers]; //counter array 1-10 answer
        backButton.setText("Back");
        backButton.setVisible(true);
        sumTheAnswers();
        questionNumberLabel.setText("Question #" + questionNumber);
        surveyNameLabel.setText(surveyName + " Survey Results");
        setBarChart();
    }

    public void nextButtonClick()
    {
        if(questionNumber<=7) {
            SetArrayAndChart(true);
            if (questionNumber == 1)
                backButton.setText("Back");
            else backButton.setText("Previous");
        }
    }

    public void backButtonClick()
    {
        if (questionNumber!=1) {
            SetArrayAndChart(false);
        }
        else MainDashboardController.setContentFromFXML("SurveySelectPage.fxml");
    }

    private void SetArrayAndChart(boolean nextButton) {
        int valueToAdd = nextButton ? 1:(-1);
        resetArray();
        questionNumber+=valueToAdd;
        sumTheAnswers();
        questionNumberLabel.setText("Question #" + questionNumber);
        setBarChart();
    }

    private void resetArray() {
        for (int i = 0 ; i < numberOfAnswers ; i++)
            answers[i] = 0;
    }

    private void sumTheAnswers()
    {
        for (SurveyAnswers surveyAnswer : surveyAnswers)
        {
            answers[surveyAnswer.getAnswers().get(questionNumber-1)-1]++; // for the every Question
        }
    }

    private void setBarChart() {
        barChart.getData().clear();
        barChart.setTitle("Answer Distribution of Question #" + questionNumber);
        categoryAxis.setLabel("Grade");
        numberAxis.setLabel("Num. of Customers");
        numberAxis.setStyle("-fx-font-size: 14");
        categoryAxis.setStyle("-fx-font-size: 14");
        numberAxis.tickLabelFontProperty().set(Font.font(14));
        categoryAxis.tickLabelFontProperty().set(Font.font(14));
        XYChart.Series series = new XYChart.Series();
        for(int i = 0 ; i<numberOfAnswers ; i++) {
            series.getData().add(new XYChart.Data(String.valueOf(i+1), answers[i]));
        }
        barChart.getData().clear();
        barChart.getData().addAll(series);
    }


    public List<SurveyAnswers> getSurveyAnswers() {
        return surveyAnswers;
    }

    public void setSurveyAnswers(List<SurveyAnswers> surveyAnswers) {
        SurveyDataPageController.surveyAnswers = surveyAnswers;
    }


    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        SurveyDataPageController.surveyName = surveyName;
    }
}
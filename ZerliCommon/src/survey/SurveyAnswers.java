package survey;

import java.io.Serializable;
import java.util.List;

public class SurveyAnswers implements Serializable {
    private int surveyID;
    private String username;
    private List<Integer> answers;

    public SurveyAnswers(int surveyID, String username, List<Integer> answers) {
        this.surveyID = surveyID;
        this.username = username;
        this.answers = answers;
    }

    public SurveyAnswers(int surveyID) {
        this.surveyID = surveyID;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Integer> questions) {
        this.answers = questions;
    }
}
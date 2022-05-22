package survey;

import java.io.Serializable;
import java.util.List;

public class SurveyQuestions implements Serializable {
    private int surveyID;
    private List<String> questions;

    public SurveyQuestions(int surveyID, List<String> questions) {
        this.surveyID = surveyID;
        this.questions = questions;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
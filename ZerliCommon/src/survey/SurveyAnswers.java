package survey;

import java.io.Serializable;
import java.util.List;

/**
 * The class is used to describe a Customer's answers to the questions
 * of a Survey
 */
public class SurveyAnswers implements Serializable {
    /**
     * The ID of the survey in the database
     */
    private int surveyID;

    /**
     * The customer's username who submitted the answered
     */
    private String username;

    /**
     * a List of the answers for the 6 questions of the survey
     */
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
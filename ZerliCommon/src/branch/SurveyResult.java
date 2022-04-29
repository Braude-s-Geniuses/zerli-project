package branch;

import java.io.Serializable;

public class SurveyResult implements Serializable {
    private int customerId;
    private int surveyId;
    private int[] answer;

    public SurveyResult(int customerId, int surveyId, int[] answer) {
        this.customerId = customerId;
        this.surveyId = surveyId;
        this.answer = answer;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public int[] getAnswer() {
        return answer;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }
}

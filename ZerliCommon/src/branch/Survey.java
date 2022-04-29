package branch;

import java.io.Serializable;
import java.sql.Timestamp;

public class Survey implements Serializable {
    private int surveyId;
    private int expertId;
    private String name;
    private Timestamp startDate;
    private Timestamp endDate;

    public Survey(int surveyId, int expertId, String name, Timestamp startDate, Timestamp endDate) {
        this.surveyId = surveyId;
        this.expertId = expertId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public int getExpertId() {
        return expertId;
    }

    public void setExpertId(int expertId) {
        this.expertId = expertId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "surveyId=" + surveyId +
                ", expertId=" + expertId +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

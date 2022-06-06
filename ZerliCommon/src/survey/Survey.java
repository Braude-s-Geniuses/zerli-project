package survey;

import javax.sql.rowset.serial.SerialBlob;
import java.io.Serializable;
import java.util.Date;

/**
 * This class is used to describe a Survey in the system
 */
public class Survey implements Serializable {
    /**
     * The ID of the survey in the database
     */
    private int surveyID;

    /**
     * The User ID of the expert who submitting his reportData
     */
    private int expertID;

    /**
     * The title of the Survey
     */
    private String name;

    /**
     * The PDF conclusion report added by the Expert Service Employee
     */
    private SerialBlob reportData;

    public Survey(int surveyID, int expertID, SerialBlob reportData) {
        this.surveyID = surveyID;
        this.expertID = expertID;
        this.reportData = reportData;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }

    public int getExpertID() {
        return expertID;
    }

    public void setExpertID(int expertID) {
        this.expertID = expertID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SerialBlob getReportData() {
        return reportData;
    }

    public void setReportData(SerialBlob reportData) {
        this.reportData = reportData;
    }
}
package survey;

import javax.sql.rowset.serial.SerialBlob;
import java.io.Serializable;
import java.util.Date;

public class Survey implements Serializable {
    private int surveyID;
    private int expertID;
    private String name;
    private Date startDate;
    private Date endDate;
    private SerialBlob reportData;


    public Survey(int surveyID, int expertID, String name, Date startDate, Date endDate, SerialBlob reportData) {
        this.surveyID = surveyID;
        this.expertID = expertID;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reportData = reportData;
    }

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public SerialBlob getReportData() {
        return reportData;
    }

    public void setReportData(SerialBlob reportData) {
        this.reportData = reportData;
    }
}
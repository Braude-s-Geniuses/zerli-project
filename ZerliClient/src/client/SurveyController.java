package client;

import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import survey.Survey;
import survey.SurveyAnswers;

import java.util.ArrayList;
import java.util.List;

public class SurveyController extends AbstractController {

    private List<SurveyAnswers> surveyAnswersList = null;

    private List<String> surveyNames = null;

    private boolean uploadStatus = false;

    private int surveyID = -1;

    private List<List> currSurveys = null;
    private List<Integer> currCustomers = null;



    public List<List> getSurveyIds(){
        return currSurveys;
    }

    public boolean checkIfFull(SurveyAnswers survey){return survey.getAnswers().size() == 6;}


    public List<List> getSurveyIDs() {
        Message requestIdsOfSurvey = new Message();
        requestIdsOfSurvey.setTask(MessageFromClient.SURVEY_IDS_REQUEST);
        requestIdsOfSurvey.setData(null);

        Client.clientController.getClient().handleMessageFromUI(requestIdsOfSurvey,true);

        return getSurveyIds();
    }

    public MessageFromServer tryToSentSurvey(SurveyAnswers survey){
        Message requestInsertSurvey = new Message();
        requestInsertSurvey.setTask(MessageFromClient.SURVEY_SEND);
        requestInsertSurvey.setData(survey);

        Client.clientController.getClient().handleMessageFromUI(requestInsertSurvey,true);

        return getResponse().getAnswer();
    }

    public List<String> getAllSurveyNamesByExpert(int expertID) {
        Message requestSurveysNamesByExpert = new Message();
        requestSurveysNamesByExpert.setTask(MessageFromClient.SURVEY_NAMES_GET_BY_EXPERT);
        requestSurveysNamesByExpert.setData(expertID);

        Client.clientController.getClient().handleMessageFromUI(requestSurveysNamesByExpert,true);

        return surveyNames;
    }

    public List<SurveyAnswers> getAllSurveyAnswers(String value)
    {
        Message requestSurveyAnswer = new Message();
        requestSurveyAnswer.setTask(MessageFromClient.SURVEY_ANSWERS_GET);
        requestSurveyAnswer.setData(value);

        Client.clientController.getClient().handleMessageFromUI(requestSurveyAnswer,true);

        return surveyAnswersList;
    }

    public boolean uploadSurveySummary(Survey survey) {
        Message requestUploadSummary = new Message();
        requestUploadSummary.setTask(MessageFromClient.SURVEY_UPLOAD_SUMMARY);
        requestUploadSummary.setData(survey);

        Client.clientController.getClient().handleMessageFromUI(requestUploadSummary,true);

        return uploadStatus;
    }

    public int getSurveyID(String name) {
        Message requestSurveyID = new Message();
        requestSurveyID.setTask(MessageFromClient.SURVEY_ID_GET);
        requestSurveyID.setData(name);

        Client.clientController.getClient().handleMessageFromUI(requestSurveyID,true);

        return surveyID;
    }

    // Getters and Setters
    public List<SurveyAnswers> getSurveyAnswersList() {
        return surveyAnswersList;
    }

    public void setSurveyAnswersList(List<SurveyAnswers> surveyAnswersList) {
        this.surveyAnswersList = surveyAnswersList;
    }

    public List<String> getSurveyNames() {
        return surveyNames;
    }

    public void setSurveyNames(List<String> surveyNames) {
        this.surveyNames = surveyNames;
    }

    public boolean isUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(boolean uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }
    public void setCurrSurveys(List<List> currSurveys) {
        this.currSurveys = currSurveys;
    }

    public void setCurrCustomers(List<Integer> currCustomers) {
        this.currCustomers = currCustomers;
    }

}
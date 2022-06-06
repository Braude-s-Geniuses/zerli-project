package client;

import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import survey.Survey;
import survey.SurveyAnswers;

import java.util.List;

/**
 * Controller for everything related to Surveys
 */
public class SurveyController extends AbstractController {

    private List<SurveyAnswers> surveyAnswersList = null;

    private List<String> surveyNames = null;

    private boolean uploadStatus = false;

    private int surveyID = -1;

    private List<List> currSurveys = null;
    private List<Integer> currCustomers = null;

    SurveyController(IMessageService service) {
        super(service);
    }

    /**
     * Checks if the SurveyAnswers instance has 6 answers completed
     * @param survey
     * @return true if 6 questions were answered; false otherwise
     */
    public boolean checkSurveyAnswersComplete(SurveyAnswers survey){return survey.getAnswers().size() == 6;}


    /**
     * Gets all surveys ids and names from the server
     * @return a List of (List Survey ID, List Survey Name)
     */
    public List<List> getSurveysIdsAndNames() {
        Message requestIdsOfSurvey = new Message();
        requestIdsOfSurvey.setTask(MessageFromClient.SURVEY_IDS_REQUEST);
        requestIdsOfSurvey.setData(null);

        getService().sendToServer(requestIdsOfSurvey, true);

        return getSurveyIds();
    }

    /**
     * Adds a customer's answers to a survey if he's authorized to so do
     * @param survey - the answers of the customer
     * @return the server's response regarding if it was successful or not
     */
    public MessageFromServer addSurveyAnswersOfCustomer(SurveyAnswers survey){
        Message requestInsertSurvey = new Message();
        requestInsertSurvey.setTask(MessageFromClient.SURVEY_SEND);
        requestInsertSurvey.setData(survey);

        return (getService().sendToServer(requestInsertSurvey, true)).getAnswer();
    }


    /**
     * This method prepares MessageFromClient to get all survey names
     * and sends it to server using clientController
     * @return the surveyNames - List
     */
    public List<String> getAllSurveys() {
        Message message = new Message();
        message.setTask(MessageFromClient.SURVEY_NAMES_GET);
        message.setData(null);

        getService().sendToServer(message, true);

        return surveyNames;
    }

    /**
     * This method prepares MessageFromClient to get all survey answers
     * and sends it to server using clientController
     * @param value
     * @return list of (ids, names)
     */
    public List<SurveyAnswers> getAllSurveyAnswers(String value) {
        Message requestSurveyAnswer = new Message();
        requestSurveyAnswer.setTask(MessageFromClient.SURVEY_ANSWERS_GET);
        requestSurveyAnswer.setData(value);

        getService().sendToServer(requestSurveyAnswer, true);

        return surveyAnswersList;
    }

    /**
     * This method prepares MessageFromClient to upload survey summary
     * and sends it to server using clientController
     * @param survey
     * @return the uploadStatus
     */
    public boolean uploadSurveySummary(Survey survey) {
        Message requestUploadSummary = new Message();
        requestUploadSummary.setTask(MessageFromClient.SURVEY_UPLOAD_SUMMARY);
        requestUploadSummary.setData(survey);

        getService().sendToServer(requestUploadSummary, true);

        return uploadStatus;
    }

    /**
     * This method prepares MessageFromClient to get survey ID
     * and sends it to server using clientController
     * @param name
     * @return the survey id
     */
    public int getSurveyID(String name) {
        Message requestSurveyID = new Message();
        requestSurveyID.setTask(MessageFromClient.SURVEY_ID_GET);
        requestSurveyID.setData(name);

        getService().sendToServer(requestSurveyID, true);

        return surveyID;
    }

    // Getters and Setters
    public List<List> getSurveyIds(){
        return currSurveys;
    }

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
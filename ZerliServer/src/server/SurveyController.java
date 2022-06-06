package server;

import communication.Message;
import communication.MessageFromServer;
import survey.Survey;
import survey.SurveyAnswers;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SurveyController {

    private final ZerliServer server;
    private final Connection con;

    private  PreparedStatement preparedStatement = null;

    public SurveyController() {
        server = ServerController.getServer();
        con = Server.databaseController.getConnection();
    }

    /**
     * Adds a customer's survey response if he is eligible to do so
     * @param survey
     * @return
     */
    public Message addSurveyAnswersByCustomer(SurveyAnswers survey) {
        String username = survey.getUsername();
        int surveyID = survey.getSurveyID();

        if (!checksAuthorizedCustomer(username,surveyID)){
            return new Message(null,MessageFromServer.SURVEY_UNAUTHORIZED_CUSTOMER);
        }

        if(checkIfFilled(username,surveyID)){
            return new Message(null,MessageFromServer.SURVEY_ALREADY_FILLED);
        }
        else{
            Statement stmt;
            try {
                stmt = con.createStatement();
                String str = "INSERT INTO survey_result VALUES( \"" + username + "\" , \""
                        + surveyID + "\" , \"" + survey.getAnswers().get(0) + "\" , \"" +
                        survey.getAnswers().get(1)  + "\" , \"" + survey.getAnswers().get(2)  +
                        "\" , \"" + survey.getAnswers().get(3)  + "\" , \"" +survey.getAnswers().get(4) +
                        "\" , \"" +  survey.getAnswers().get(5)  +   "\");";

                stmt.executeUpdate(str);

            } catch (SQLException e) {
                e.printStackTrace();
                return new Message(null, MessageFromServer.SURVEY_INSERT_FAIL);
            }
        }
        return new Message(null, MessageFromServer.SURVEY_INSERT_SUCCESS);
    }


    /**
     * Returns a List of Lists that contains Survey IDs and Names
     * @return List (List Ids, List Names)
     */
    public Message getSurveyIdsAndNames() {
        Statement stmt;
        ArrayList<Integer> surveysIDs = new ArrayList<Integer>();
        ArrayList<String> surveysNames = new ArrayList<String>();
        List<List>  surveyData = new ArrayList<List>();
        try {
            stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM survey;");
            while (resultSet.next()) {
                surveysIDs.add(resultSet.getInt("survey_id"));
                surveysNames.add(resultSet.getString("survey_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.SURVEY_IDS_REQUEST_FAIL);
        }
        surveyData.add(surveysIDs);
        surveyData.add(surveysNames);
        return new Message(surveyData, MessageFromServer.SURVEY_IDS_REQUEST_SUCCESS);

    }

    /**
     * Checks if customer is allowed to submit Survey Results
     * @param username
     * @param SurveyID
     * @return true if he's allowed; false otherwise
     */
    public boolean checksAuthorizedCustomer(String username, int SurveyID) {
        Statement stmt;
        try {
            stmt = con.createStatement();
            String str = "SELECT * FROM survey_customer where survey_id = \"" +
                    SurveyID +  "\" and username = \"" + username + "\";";

            ResultSet resultSet = stmt.executeQuery(str);

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if customer already answered given survey
     * @param username
     * @param surveyID
     * @return true if he didnt fill; false otherwise
     */
    public boolean checkIfFilled(String username, int surveyID) {
        Statement stmt;
        try {
            stmt = con.createStatement();
            String queryToExecute = "SELECT * FROM survey_result where username = \"" +
                    username + "\" AND survey_id= \"" + surveyID + "\";";
            ResultSet resultSet = stmt.executeQuery(queryToExecute);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets all surveys
     * @return
     */
    public Message tryToGetSurveyNames() {
        Statement stmt;
        List<String> surveys = new ArrayList<>();
        try {
            stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM survey;");
            while (resultSet.next()) {
                String surveyName = resultSet.getString("survey_name");
                surveyName.replace('_', ' ');
                if(!surveys.contains(surveyName))
                    surveys.add(surveyName);
            }
            Collections.sort(surveys);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.SURVEY_NAMES_FAIL);
        }
        return new Message(surveys, MessageFromServer.SURVEY_NAMES_SUCCESS);
    }

    /**
     * Gets all survey submitted results
     * @param name - the survey name
     * @return
     */
    public Message tryToGetSurveyAnswers(String name)
    {
        List<SurveyAnswers> surveyAnswersList = new ArrayList<>();
        List<Integer> answers;
        int index = 0;
        int firstAnswerColumn = 3;
        int numberOfAnswers = 6;
        try {
            preparedStatement=  con.prepareStatement("SELECT survey_id FROM survey WHERE survey_name=? ;", Statement.RETURN_GENERATED_KEYS);//get survey id
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int id = resultSet.getInt("survey_id");
            preparedStatement=  con.prepareStatement("SELECT * FROM survey_result WHERE survey_id=? ;", Statement.RETURN_GENERATED_KEYS);// get all survey results
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                surveyAnswersList.add(new SurveyAnswers(id));
                answers = new ArrayList<>();
                for(int i = firstAnswerColumn ; i<numberOfAnswers +firstAnswerColumn ; i++)
                    answers.add(resultSet.getInt(i));
                surveyAnswersList.get(index).setAnswers(answers);
                index++;
            }
            preparedStatement = null;
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.SURVEY_ANSWERS_FAIL);
        }
        return new Message(surveyAnswersList, MessageFromServer.SURVEY_ANSWERS_SUCCESS);
    }

    /**
     * Uploads expert conclusion report to database
     * @param survey
     * @return
     */
    public Message tryToUploadFile(Survey survey) {
        try {
            preparedStatement = con.prepareStatement("UPDATE survey SET conclusion_report=? WHERE survey_id= ?", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setBlob(1,survey.getReportData());
            preparedStatement.setInt(2,survey.getSurveyID());

            if(preparedStatement.executeUpdate()==1)
                return new Message(null, MessageFromServer.SURVEY_UPLOAD_SUMMARY_SUCCESS);
            preparedStatement = null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(null, MessageFromServer.SURVEY_UPLOAD_SUMMARY_FAIL);
    }

    /**
     * Converts survey name to survey id
     * @param name
     * @return
     */
    public Message tryToGetSurveyID(String name) {
        try {
            preparedStatement = con.prepareStatement("SELECT survey_id FROM survey WHERE survey_name=? ;", Statement.RETURN_GENERATED_KEYS);//get survey id
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new Message(resultSet.getInt("survey_id"), MessageFromServer.SURVEY_ID_GET_SUCCESS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new Message(null, MessageFromServer.SURVEY_ID_GET_FAIL);
    }
}
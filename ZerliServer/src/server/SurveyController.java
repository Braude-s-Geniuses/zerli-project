package server;

import communication.Message;
import communication.MessageFromServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import survey.Survey;
import survey.SurveyAnswers;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SurveyController {

    private ZerliServer server;
    private Connection con;

    private  PreparedStatement preparedStatement = null;

    public SurveyController() {
        server = ServerController.getServer();
        con = Server.databaseController.getConnection();
    }


    public Message tryToInsertSurvey(SurveyAnswers survey) {
        String username = survey.getUsername();
        int surveyID = survey.getSurveyID();

        if (!checksAuthorizedCustomer(username,surveyID)){
            return new Message(null,MessageFromServer.UNAUTHORIZED_CUSTOMER);
        }

        if(surveyAlreadyFilled(username,surveyID)){
            return new Message(null,MessageFromServer.SURVEY_HAS_BEEN_COMPLETED);
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
                return new Message(null, MessageFromServer.SURVER_INSERT_NOT_SUCCESSFULLY);
            }
        }
        return new Message(null, MessageFromServer.SURVER_INSERT_SUCCEED);
    }



    public Message tryToGetIDsOfSurveys() {
        Statement stmt;
        ArrayList<Integer> surveysIDs = new ArrayList<Integer>();
        ArrayList<String> surveysNames = new ArrayList<String>();
        List<List>  surveyData = new ArrayList<List>();
        try {
            stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM survey;");
            while (resultSet.next()) {
                surveysIDs.add(resultSet.getInt(1));
                surveysNames.add(resultSet.getString(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.SURVEY_IDS_NOT_SUCCESSFULLY);
        }
        surveyData.add(surveysIDs);
        surveyData.add(surveysNames);
        return new Message(surveyData, MessageFromServer.SURVEY_IDS_SUCCESSFUL);

    }
    public boolean checksAuthorizedCustomer(String username, int SurveyID) {
        Statement stmt;
        try {
            stmt = con.createStatement();
            String str = "SELECT * FROM survey_customer where survey_id = \"" +
                    SurveyID +  "\" and username = \"" + username + "\";";

            ResultSet resultSet = stmt.executeQuery(str);

            return resultSet.next() ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean surveyAlreadyFilled(String username,int surveyID) {
        Statement stmt;
        try {
            stmt = con.createStatement();
            String queryToExecute = "SELECT * FROM survey_result where username = \"" +
                    username + "\" AND survey_id= \"" + surveyID + "\";";
            ResultSet resultSet = stmt.executeQuery(queryToExecute);
            return resultSet.next() ? true : false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Message tryToGetSurveyNames(int expertID) {
        Statement stmt;
        List<String> surveys = new ArrayList<>();
        try {
            stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM survey WHERE expert_id = "+ expertID+";"); //get all surveys names by expert
            while (resultSet.next()) {
                String surveyName = resultSet.getString(4);
                surveyName.replace('_', ' ');
                if(!surveys.contains(surveyName))
                    surveys.add(surveyName);
            }
            Collections.sort(surveys);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Message(null, MessageFromServer.SURVEY_NAMES_NOT_SUCCESSFULLY);
        }
        return new Message(surveys, MessageFromServer.SURVEY_NAMES_SUCCESSFULLY);
    }

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
            int id = resultSet.getInt(1);
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
            return new Message(null, MessageFromServer.SURVEY_ANSWERS_NOT_SUCCESSFULLY);
        }
        return new Message(surveyAnswersList, MessageFromServer.SURVEY_ANSWERS_SUCCESSFULLY);
    }

    public Message tryToUploadFile(Survey survey) {
        try {
            preparedStatement = con.prepareStatement("UPDATE survey SET conclusion_report=?" +
                    " WHERE survey_id= ? " +
                    "AND expert_id= ?", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setBlob(1,survey.getReportData());
            preparedStatement.setInt(2,survey.getSurveyID());
            preparedStatement.setInt(3,survey.getExpertID());

            if(preparedStatement.executeUpdate()==1)
                return new Message(null, MessageFromServer.UPLOAD_SURVEY_SUMMARY_SUCCESSFULLY);
            preparedStatement = null;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return new Message(null, MessageFromServer.UPLOAD_SURVEY_SUMMARY_NOT_SUCCESSFULLY);
    }

    public Message tryToGetSurveyID(String name) {
        try {
            preparedStatement=  con.prepareStatement("SELECT survey_id FROM survey WHERE survey_name=? ;", Statement.RETURN_GENERATED_KEYS);//get survey id
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return new Message(resultSet.getInt(1), MessageFromServer.GET_SURVEY_ID_SUCCESSFULLY);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
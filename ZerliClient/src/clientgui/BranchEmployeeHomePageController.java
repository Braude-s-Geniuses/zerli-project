package clientgui;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import user.User;
import javafx.event.ActionEvent;

import java.io.IOException;

public class BranchEmployeeHomePageController {

    @FXML // fx:id="btnBrowseCatalogue"
    private Button btnBrowseCatalogue; // Value injected by FXMLLoader

    @FXML // fx:id="btnInsertSurveyAnswers"
    private Button btnInsertSurveyAnswers; // Value injected by FXMLLoader

    @FXML // fx:id="btnActivateDiscount"
    private Button btnActivateDiscount; // Value injected by FXMLLoader

    @FXML // fx:id="Logout"
    private Button Logout; // Value injected by FXMLLoader

    @FXML // fx:id="welcomeLabel"
    private Label welcomeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="CustomerNameLabel"
    private Label CustomerNameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="roleLabel"
    private Label roleLabel; // Value injected by FXMLLoader

    public void startNewScene(ActionEvent event, String userType) throws Exception{
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(userType+"HomePage.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli Branch Employee menu");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        Client.clientController.attachExitEventToStage(primaryStage);
    }

    @FXML
    void BtnBrowseCatalogueClick(ActionEvent event) {

    }

    @FXML
    void BtnInsertSurveyAnswersClick(ActionEvent event) {

    }

    @FXML
    void BtnActivateDiscountClick(ActionEvent event) {

    }

    @FXML
    void LogoutUser(ActionEvent event) {
        LoginFormController loginController = new LoginFormController();
        ((Node) event.getSource()).getScene().getWindow().hide();
        try {
            loginController.start();
        }
        catch(IOException e)
        {
        }

    }
}

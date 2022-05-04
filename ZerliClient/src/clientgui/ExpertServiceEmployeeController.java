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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ExpertServiceEmployeeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label CustomerNameLabel;

    @FXML
    private Button Logout;

    public void startNewScene(ActionEvent event, String userType) throws Exception{
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(userType+"HomePage.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli Expert menu");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        Client.clientController.attachExitEventToStage(primaryStage);
    }

    public void LogoutUser (ActionEvent event) throws IOException {
        LoginFormController loginController = new LoginFormController();
        ((Node) event.getSource()).getScene().getWindow().hide();
        loginController.start();

    }

}

package clientgui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class DeliveryOperatorHomePageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label CustomerNameLabel;

    @FXML
    private Button Logout;

    public void startNewScene(ActionEvent event) throws Exception{
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("DeliveryOperatorHomePage.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("Zerli Delivery Operator Menu");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            Client.clientController.attachExitEventToStage(primaryStage);
    }

    public void LogoutUser (ActionEvent event) throws Exception {
        LoginFormController loginController = new LoginFormController();
        loginController.logoutClick(event,Client.clientController.getClient().getUser());
        ((Node) event.getSource()).getScene().getWindow().hide();
        loginController.start();
    }

}
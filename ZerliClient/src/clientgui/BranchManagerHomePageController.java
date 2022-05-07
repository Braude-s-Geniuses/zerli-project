/**
 * Sample Skeleton for 'BranchManagerHomePage.fxml' Controller Class
 */

package clientgui;

import client.Client;
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

public class BranchManagerHomePageController {

    @FXML // fx:id="btnOrdersManagement"
    private Button btnOrdersManagement; // Value injected by FXMLLoader

    @FXML // fx:id="btnNewCustomers"
    private Button btnNewCustomers; // Value injected by FXMLLoader

    @FXML // fx:id="btnBranchReports"
    private Button btnBranchReports; // Value injected by FXMLLoader

    @FXML // fx:id="Logout"
    private Button Logout; // Value injected by FXMLLoader

    @FXML // fx:id="welcomeLabel"
    private Label welcomeLabel; // Value injected by FXMLLoader

    @FXML // fx:id="CustomerNameLabel"
    private Label CustomerNameLabel; // Value injected by FXMLLoader

    @FXML // fx:id="roleLabel"
    private Label roleLabel; // Value injected by FXMLLoader

    public void startNewScene(ActionEvent event) throws Exception{
        ((Node) event.getSource()).getScene().getWindow().hide();
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("BranchManagerHomePage.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli Branch Manager menu");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        Client.clientController.attachExitEventToStage(primaryStage);
    }

    @FXML
    void BtnBranchReportsClick(ActionEvent event) {

    }

    @FXML
    void BtnNewCustomersClick(ActionEvent event) {

    }

    @FXML
    void BtnOrdersManagementClick(ActionEvent event) {

    }

    public void LogoutUser (ActionEvent event) throws Exception {
        LoginFormController loginController = new LoginFormController();
        loginController.logoutClick(event,Client.clientController.getClient().getUser());
        ((Node) event.getSource()).getScene().getWindow().hide();
        loginController.start();
    }


}

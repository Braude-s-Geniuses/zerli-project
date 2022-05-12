/**
 * Sample Skeleton for 'CEOHomePage.fxml' Controller Class
 */

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

public class CEOHomePageController {

    @FXML // fx:id="btnViewReports"
    private Button btnViewReports; // Value injected by FXMLLoader

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
        Parent root = FXMLLoader.load(getClass().getResource("CEOHomePage.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli CEO menu");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        Client.clientController.attachExitEventToStage(primaryStage);
    }

    @FXML
    void BtnViewReportsClick(ActionEvent event) {

    }

    public void LogoutUser (ActionEvent event) throws Exception {

    }

}

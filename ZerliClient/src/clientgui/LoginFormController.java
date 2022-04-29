/**
 * Sample Skeleton for 'LoginForm.fxml' Controller Class
 */

package clientgui;

import java.awt.*;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import client.LoginClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import user.User;

public class LoginFormController {

    private LoginClientController client;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="username_textField"
    private TextField username_textField; // Value injected by FXMLLoader

    @FXML // fx:id="password_textField"
    private TextField password_textField; // Value injected by FXMLLoader

    @FXML // fx:id="login_Button"
    private Button login_Button; // Value injected by FXMLLoader

    @FXML
    private Label error_Lable;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert username_textField != null : "fx:id=\"username_textField\" was not injected: check your FXML file 'LoginForm.fxml'.";
        assert password_textField != null : "fx:id=\"password_textField\" was not injected: check your FXML file 'LoginForm.fxml'.";
        assert login_Button != null : "fx:id=\"login_Button\" was not injected: check your FXML file 'LoginForm.fxml'.";
        error_Lable.setVisible(false);
    }

    /**
     *  This method is called by pressing login button.
     *  Forwards the client to relevant screen, in case of error prints warring to screen.
     *
     * @param event , Forward to the next screen according the privilege.
     * @throws Exception , In case of a problem with connectivity with the Server.
     */

    public void loginClick (ActionEvent event) throws Exception{
        User newUser = client.tryToLogin(username_textField.getText(),password_textField.getText());
        error_Lable.setVisible(false);
        switch (newUser.getUserType()){

            case UNREGISTERED:
                error_Lable.setTextFill(javafx.scene.paint.Color.color(255,0,0));
                error_Lable.setVisible(true);
                error_Lable.setText("You typed the wrong username or password. try again.");
                break;

            case CUSTOMER:
                startNewScene(event,"Customer");
                break;

            case BRANCH_EMPLOYEE:
                startNewScene(event,"BranchEmployee");
                break;

            case BRANCH_MANAGER:
                startNewScene(event,"BranchManager");
                break;

            case SERVICE_EMPLOYEE:
                startNewScene(event,"ServiceEmployee");
                break;

            case EXPERT_SERVICE_EMPLOYEE:
                startNewScene(event,"ExpertServiceEmployee");
                break;

            case DELIVERY_OPERATOR:
                startNewScene(event,"DeliveryOperator");
                break;

            case CEO:
                startNewScene(event,"CEO");
                  break;
        }

    }

    /**
     * This method getting the type of the user and forward him to the relevant screen.
     *
     * @param event, pushing the login button.
     * @param userType getting the user type of the client.
     * @throws Exception In case of a problem with connectivity with the Server.
     */
    public void startNewScene(ActionEvent event,String userType) throws Exception{
        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource(userType + ".fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("Zerli Menu");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            Client.clientController.attachExitEventToStage(primaryStage);
        }catch(ConnectException e){
            error_Lable.setTextFill(javafx.scene.paint.Color.color(255,0,0));
            error_Lable.setVisible(true);
            error_Lable.setText("There is a temporary problem with the server. Please try again later.");
        }
    }

}

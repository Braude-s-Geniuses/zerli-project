/**
 * Sample Skeleton for 'LoginForm.fxml' Controller Class
 */

package clientgui;

import java.net.URL;
import java.util.ResourceBundle;

import client.LoginClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert username_textField != null : "fx:id=\"username_textField\" was not injected: check your FXML file 'LoginForm.fxml'.";
        assert password_textField != null : "fx:id=\"password_textField\" was not injected: check your FXML file 'LoginForm.fxml'.";
        assert login_Button != null : "fx:id=\"login_Button\" was not injected: check your FXML file 'LoginForm.fxml'.";
    }

    public void loginClick (ActionEvent event) throws Exception{
        client.tryToLogin(username_textField.getText(),password_textField.getText());
    }

}

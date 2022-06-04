package clientgui;

import client.Client;
import communication.MessageFromServer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    private Border border;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField fldUsername;

    @FXML
    private PasswordField fldPassword;

    @FXML
    private Button login_Button;

    @FXML
    private Label error_Lab;

    @FXML
    private Button back_Button;

    /**
     * need to update
     *
     * @param event
     */
    public void backClick(ActionEvent event) throws IOException {
        MainDashboardController.setContentFromFXML("BrowseCatalogPage.fxml");
    }


    /**
     * This method is called by pressing login button.
     * Forwards the client to relevant screen, in case of error prints warring to screen.
     *
     * @param event , Forward to the next screen according the privilege.
     * @throws Exception , In case of a problem with connectivity with the Server.
     */

    public void loginClick(ActionEvent event) throws Exception {
        border = new Border(new BorderStroke(Color.INDIANRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

        if (fldUsername.getText().isEmpty() || fldPassword.getText().isEmpty()) {
            showErrorMessage("You typed the wrong username or password. Please try again.");
            return;
        }

        MessageFromServer result = (Client.userController.login(fldUsername.getText(), fldPassword.getText())).getAnswer();

        if(result == MessageFromServer.LOGIN_SUCCESS || result == MessageFromServer.CUSTOMER_IS_BLOCKED) {
            MainDashboardController.setContentFromFXML("UserHomePage.fxml");
            MainDashboardController.buildUserNavigation();
            MainDashboardController.refreshLoginButton();
        }
        else if(result == MessageFromServer.ALREADY_LOGGED_IN) {
            showErrorMessage("                                This user is already logged in.");
        }
        else {
            showErrorMessage("You typed the wrong username or password. Please try again.");
        }
    }

    private void showErrorMessage(String err) {
        fldUsername.setBorder(border);
        fldPassword.setBorder(border);
        error_Lab.setText(err);
        error_Lab.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fldUsername.requestFocus();

        fldUsername.setOnKeyPressed( evt ->{
            if(evt.getCode().equals(KeyCode.ENTER) ){
                fldPassword.requestFocus();
            }
        });
        fldPassword.setOnKeyPressed( evt ->{
            if(evt.getCode().equals(KeyCode.ENTER)){
                login_Button.fire();
            }
        });

    }
}
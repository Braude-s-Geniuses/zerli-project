/**
 * Sample Skeleton for 'LoginForm.fxml' Controller Class
 */

package clientgui;

import client.Client;
import client.LoginClientController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginFormController {

   private static LoginClientController loginClientController;
      // private static CustomerController customerControllerController; omer and gal
     private static BranchEmployeeHomePageController branchEmployeeHomePageController;
    private static BranchManagerHomePageController branchManagerController;
    private static ServiceEmployeeHomePageController serviceEmployeeController;
    private static ExpertServiceEmployeeHomePageController expertServiceEmployeeController;
    private static CEOHomePageController ceoController;

    private static DeliveryOperatorHomePageController deliveryOperatorController;

   private Border border;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="username_textField"
    private TextField fldUsername;

    @FXML // fx:id="password_textField"
    private PasswordField fldPassword;

    @FXML // fx:id="login_Button"
    private Button login_Button;

    @FXML // fx:id="error_Lab"
    private Label error_Lab;

    @FXML // fx:id="back_Button"
    private Button back_Button;

    /**
     * need to update
     *
     * @param event
     */
    public void backClick(ActionEvent event) {
        //browseCatalogPage.start();
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
        loginClientController = new LoginClientController();

        if (fldUsername.getText().isEmpty() || fldPassword.getText().isEmpty()) {
            showErrorMessage("You typed the wrong username or password. Please try again.");
            return;
        }

        User newUser = loginClientController.tryToLogin(fldUsername.getText(), fldPassword.getText());
        moveToUserSceneByUserType(event, newUser);
    }

    public void logoutClick(ActionEvent event, User userOut) throws Exception {
        loginClientController = new LoginClientController();
        loginClientController.tryToLogOut(userOut.getUserId());
        return;
    }

    private void moveToUserSceneByUserType(ActionEvent event,User newUser) throws Exception {
        switch (newUser.getUserType()) {

            case UNREGISTERED:
                showErrorMessage("You typed the wrong username or password. Please try again.");
                break;
            case LOGGED_IN:
                showErrorMessage("                                This user is already logged in.");
                break;
            case SERVICE_EMPLOYEE:
                serviceEmployeeController = new ServiceEmployeeHomePageController();
                try {
                    serviceEmployeeController.startNewScene(event);
                } catch (ConnectException e) {
                    showErrorMessage("You typed the wrong username or password. Please try again.");
                }
                break;
            case EXPERT_SERVICE_EMPLOYEE:
                expertServiceEmployeeController = new ExpertServiceEmployeeHomePageController();
                try {
                    expertServiceEmployeeController.startNewScene(event);
                } catch (ConnectException e) {
                    showErrorMessage("You typed the wrong username or password. Please try again.");
                }
                break;

            case DELIVERY_OPERATOR:
                deliveryOperatorController = new DeliveryOperatorHomePageController();
                try {
                    deliveryOperatorController.startNewScene(event);
                } catch (ConnectException e) {
                    showErrorMessage("You typed the wrong username or password. Please try again.");
                }
                break;

//            case CUSTOMER:
//                //customerControllerController= new CustomerControllerController();
//                try {
//                    //customerControllerController.startNewScene(event, "Customer");
//                }catch(ConnectException e) {
//                    showErrorMessage("You typed the wrong username or password. Please try again.");
//                }
//                break;
//
            case BRANCH_EMPLOYEE:
                branchEmployeeHomePageController= new BranchEmployeeHomePageController();
                try {
                    branchEmployeeHomePageController.startNewScene(event);
                }catch(ConnectException e) {
                    showErrorMessage("You typed the wrong username or password. Please try again.");
                }
                break;

            case BRANCH_MANAGER:
                branchManagerController= new BranchManagerHomePageController();
                try {
                branchManagerController.startNewScene(event);
                }catch(ConnectException e) {
                    showErrorMessage("You typed the wrong username or password. Please try again.");
                }
                break;

            case CEO:
                ceoController= new CEOHomePageController();
                try {
                    ceoController.startNewScene(event);
                }catch(ConnectException e) {
                    showErrorMessage("You typed the wrong username or password. Please try again.");
                }
                break;
        }
    }

    /**
     * This method will open the relevant scene for the client - according the privilege.
     *
     * @throws IOException
     */
    public void start() throws IOException {

        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("LoginForm.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Zerli login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        Client.clientController.attachExitEventToStage(primaryStage);
    }

    private void showErrorMessage(String err) {
        fldUsername.setBorder(border);
        fldPassword.setBorder(border);
        error_Lab.setText(err);
        error_Lab.setVisible(true);
    }
}
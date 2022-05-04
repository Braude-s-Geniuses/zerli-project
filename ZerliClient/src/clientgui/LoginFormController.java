/**
 * Sample Skeleton for 'LoginForm.fxml' Controller Class
 */

package clientgui;

import java.io.IOException;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import user.User;
import user.UserType;

public class LoginFormController {

   private static LoginClientController loginClientController;
      // private static CustomerController customerControllerController; omer and gal
     //private static BranchControllerController branchControllerController; haim
    //private static BranchManagerController branchManagerController;
    private static ServiceEmployeeController serviceEmployeeController;
    private static ExpertServiceEmployeeController expertServiceEmployeeController;
    //private static CEOController ceoController;

    private static DeliveryOperatorController deliveryOperatorController;


   Border border;

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
     * @param event
     */
    public void backClick(ActionEvent event){
        //broseCatalogPage.start();
    }


    /**
     *  This method is called by pressing login button.
     *  Forwards the client to relevant screen, in case of error prints warring to screen.
     *
     * @param event , Forward to the next screen according the privilege.
     * @throws Exception , In case of a problem with connectivity with the Server.
     */

        public void loginClick (ActionEvent event) throws Exception{
        border = new Border(new BorderStroke(Color.INDIANRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        loginClientController = new LoginClientController();

        if(fldUsername.getText().isEmpty() || fldPassword.getText().isEmpty()){
            showErrorMessage();
            return;
        }

        User newUser = loginClientController.tryToLogin(fldUsername.getText(), fldPassword.getText());
            moveToUserSceneByUserType(event, newUser.getUserType());
        }


    private void moveToUserSceneByUserType(ActionEvent event, UserType userType) throws Exception {
        switch (userType){

            case UNREGISTERED:
                showErrorMessage();
                break;

//            case CUSTOMER:
//                //customerControllerController= new CustomerControllerController();
//                try {
//                    //customerControllerController.startNewScene(event, "Customer");
//                }catch(ConnectException e) {
//                    error_Lab.setTextFill(javafx.scene.paint.Color.color(255, 0, 0));
//                    error_Lab.setVisible(true);
//                    error_Lab.setText("There is a temporary problem with the server. Please try again later.");
//                }
//                break;
//
//            case BRANCH_EMPLOYEE:
//                branchControllerController= new BranchControllerController();
//                try {
//                    branchControllerController.startNewScene(event, "BranchEmployee");
//                }catch(ConnectException e) {
//                    error_Lab.setTextFill(javafx.scene.paint.Color.color(255, 0, 0));
//                    error_Lab.setVisible(true);
//                    error_Lab.setText("There is a temporary problem with the server. Please try again later.");
//                }
//                break;
//
//            case BRANCH_MANAGER:
//                branchManagerController= new BranchManagerController();
//                try {
//                    branchManagerController.startNewScene(event, "BranchManager");
//                }catch(ConnectException e) {
//                    error_Lab.setTextFill(javafx.scene.paint.Color.color(255, 0, 0));
//                    error_Lab.setVisible(true);
//                    error_Lab.setText("There is a temporary problem with the server. Please try again later.");
//                }
//                break;

            case SERVICE_EMPLOYEE:
                serviceEmployeeController= new ServiceEmployeeController();
                try {
                    serviceEmployeeController.startNewScene(event, "ServiceEmployee");
                }catch(ConnectException e) {
                    error_Lab.setTextFill(javafx.scene.paint.Color.color(255, 0, 0));
                    error_Lab.setVisible(true);
                    error_Lab.setText("There is a temporary problem with the server. Please try again later.");
                }
                break;

            case EXPERT_SERVICE_EMPLOYEE:
                expertServiceEmployeeController= new ExpertServiceEmployeeController();
                try {
                    expertServiceEmployeeController.startNewScene(event, "ExpertServiceEmployee");
                }catch(ConnectException e) {
                    error_Lab.setTextFill(javafx.scene.paint.Color.color(255, 0, 0));
                    error_Lab.setVisible(true);
                    error_Lab.setText("There is a temporary problem with the server. Please try again later.");
                }
                break;

            case DELIVERY_OPERATOR:
                deliveryOperatorController= new DeliveryOperatorController();
                try {
                    deliveryOperatorController.startNewScene(event, "DeliveryOperator");
                }catch(ConnectException e){
            error_Lab.setTextFill(javafx.scene.paint.Color.color(255,0,0));
            error_Lab.setVisible(true);
            error_Lab.setText("There is a temporary problem with the server. Please try again later.");
        }
                break;

//            case CEO:
//                ceoController= new CeoController();
//                try {
//                    ceoController.startNewScene(event, "CEO");
//                }catch(ConnectException e) {
//                    error_Lab.setTextFill(javafx.scene.paint.Color.color(255, 0, 0));
//                    error_Lab.setVisible(true);
//                    error_Lab.setText("There is a temporary problem with the server. Please try again later.");
//                }
//                break;
        }
    }

    /**
     * This method getting the type of the user and forward him to the relevant screen.
     *
     * @param event, pushing the login button.
     * @param userType getting the user type of the client.
     * @throws Exception In case of a problem with connectivity with the Server.
     */
//    public void startNewScene(ActionEvent event,String userType) throws Exception{
//        try {
//            ((Node) event.getSource()).getScene().getWindow().hide();
//            Stage primaryStage = new Stage();
//            Parent root = FXMLLoader.load(getClass().getResource(userType+"HomePage.fxml"));
//            Scene scene = new Scene(root);
//
//            primaryStage.setTitle("Zerli Menu");
//            primaryStage.setScene(scene);
//            primaryStage.setResizable(false);
//            primaryStage.show();
//            Client.clientController.attachExitEventToStage(primaryStage);
//        }catch(ConnectException e){
//            error_Lab.setTextFill(javafx.scene.paint.Color.color(255,0,0));
//            error_Lab.setVisible(true);
//            error_Lab.setText("There is a temporary problem with the server. Please try again later.");
//        }
//    }



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
    private void showErrorMessage() {
        fldUsername.setBorder(border);
        fldPassword.setBorder(border);
        error_Lab.setText("You typed the wrong username or password. Please try again.");
        error_Lab.setVisible(true);
    }

}

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
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.ConnectException;

/** represents the GUI Controller of the client hostname input form.
 *
 */
public class InputHostnameFormController {

    @FXML
    private TextField fldHostname;

    @FXML
    private Button btnConnect = null;

    @FXML
    private Label lblErrorMessage;
    private Stage primaryStage;

    /** The method displays the input hostname form gui to the client.
     *
     * @param primaryStage
     * @throws Exception
     */
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("InputHostnameForm.fxml"));
        this.primaryStage=primaryStage;
        Scene scene = new Scene(root);
        primaryStage.setTitle("Zerli Client");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /** The button initializes the <code>ClientController</code> and connects to the server.
     *
     * @param event
     * @throws Exception
     */
    public void clickBtnConnect(ActionEvent event) throws Exception {
        Border border = new Border(new BorderStroke(Color.INDIANRED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
        LoginFormController loginController = new LoginFormController();

        if (fldHostname.getText().isEmpty()) {
            fldHostname.setBorder(border);
            return;
        }

        Client.initController(fldHostname.getText());
        try {
            Client.clientController.getClient().openConnection();
            lblErrorMessage.setVisible(false);

            /**
             * The connection was successful - show the login screen.
             */
            ((Node) event.getSource()).getScene().getWindow().hide();
            loginController.start();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("BrowseCatalogForm.fxml")); // change here for testing pages
            Scene scene = new Scene(root);

            primaryStage.setTitle("Zerli Client");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            Client.clientController.attachExitEventToStage(primaryStage);

        } catch (ConnectException e) {
            fldHostname.setBorder(border);
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText("Failed to connect to host!");
        }

    }

}

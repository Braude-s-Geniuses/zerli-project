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
    private Button btnConnect;

    @FXML
    private Label lblErrorMessage;

    /** The method displays the input hostname form gui to the client.
     *
     * @throws Exception
     */
    public void start() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("InputHostnameForm.fxml"));
        Stage primaryStage = new Stage();
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

        if (fldHostname.getText().isEmpty()) {
            fldHostname.setBorder(border);
            return;
        }

        Client.initController(fldHostname.getText(), true);
        try {
            Client.clientController.getClient().openConnection();
            lblErrorMessage.setVisible(false);

            ((Node) event.getSource()).getScene().getWindow().hide();

            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("MainDashboard.fxml"));
            Scene scene = new Scene(root);

            primaryStage.setTitle("Zerli Client");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            /* Adds the functionality of logging out user and disconnecting client connection before exiting */
            Client.clientController.attachExitEventToStage(primaryStage);

            MainDashboardController.setContentFromFXML("BrowseCatalogPage.fxml");

        } catch (ConnectException e) {
            fldHostname.setBorder(border);
            lblErrorMessage.setVisible(true);
            lblErrorMessage.setText("Failed to connect to host!");
        }

    }

}

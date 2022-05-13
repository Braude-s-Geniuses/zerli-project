package clientgui;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class UserHomePageController implements Initializable {

    @FXML
    private Label lblUserRole;

    @FXML
    private Label lbluserFullname;

    @FXML
    private AnchorPane picturePane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblUserRole.setText(Client.userController.getLoggedInUser().getUserType().toString());
        lbluserFullname.setText(Client.userController.getLoggedInUser().getFirstName() + " " + Client.userController.getLoggedInUser().getLastName());
    }
}

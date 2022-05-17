package clientgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import util.Alert;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertTestController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button button;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Test");
    }

    @FXML
    void clickButton(ActionEvent event) {
        System.out.println("Test");
        MainDashboardController.createAlert("Product added successfully!", Alert.SUCCESS, Duration.seconds(3), 135, 67);
        MainDashboardController.createAlert("Product added successfully!", Alert.PRIMARY, Duration.seconds(3), 135, 120);
        MainDashboardController.createAlert("Product added successfully!", Alert.DANGER, Duration.seconds(3), 135, 180);
        MainDashboardController.createAlert("Product added successfully!", Alert.WARNING, Duration.seconds(3), 135, 240);
        MainDashboardController.createAlert("Product added successfully!", Alert.INFO, Duration.seconds(3), 135, 300);
        MainDashboardController.createAlert("Product added successfully!", Alert.SECONDARY, Duration.seconds(3), 135, 360);
        MainDashboardController.createAlert("Product added successfully!", Alert.LIGHT, Duration.seconds(3), 135, 420);
        MainDashboardController.createAlert("Product added successfully!", Alert.DARK, Duration.seconds(3), 135, 480);
    }
}

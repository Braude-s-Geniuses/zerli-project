package clientgui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ReportPageController implements Initializable {
    public static ArrayList<ImageView> imageViews = new ArrayList<>();

    @FXML
    private VBox vboxReport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for ( ImageView img: imageViews) {
            img.setFitWidth(860);
            img.setFitHeight(980);
            vboxReport.getChildren().add(img);
        }
        imageViews.clear();

    }
}
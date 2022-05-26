package clientgui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CompareQuarterlyPageController implements Initializable {

    public static ArrayList<ImageView> imageViewsFromFirstReport;
    public static ArrayList<ImageView> imageViewsFromSecondReport;

    @FXML
    private VBox vboxReportLeft;

    @FXML
    private VBox vboxReportRight;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for ( ImageView img: imageViewsFromFirstReport) {
            img.setFitWidth(860);
            img.setFitHeight(980);
            vboxReportRight.getChildren().add(img);
        }
        for ( ImageView img: imageViewsFromSecondReport) {
            img.setFitWidth(860);
            img.setFitHeight(980);
            vboxReportLeft.getChildren().add(img);
        }

        imageViewsFromFirstReport.clear();
        imageViewsFromSecondReport.clear();
    }

}
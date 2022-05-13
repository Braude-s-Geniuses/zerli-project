package clientgui;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OrderCompletePageController implements Initializable {
    @FXML
    private Button btnContinueShopping;
    @FXML
    private Label lblOrderId;

    /**
     *
     * @param location
     * The location used to resolve relative paths for the root object, or
     * <tt>null</tt> if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or <tt>null</tt> if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        lblOrderId.setText(String.valueOf((int)Client.orderController.getResponse().getData()));
        lblOrderId.setUnderline(true);
    }

    @FXML
    void clickBtnContinueShopping(ActionEvent event) throws IOException {
        MainDashboardController.setContentFromFXML("BrowseCatalogPage.fxml");
    }

}
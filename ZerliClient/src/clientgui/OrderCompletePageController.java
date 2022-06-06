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
    @FXML
    private Label lblOrderDiscount;
    @FXML
    private Label lblDiscount;

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
        lblOrderId.setText(String.valueOf((int)Client.orderController.getService().getResponse().getData()));
        lblOrderId.setUnderline(true);
        float discount = Client.orderController.getCurrentOrder().getPrice()  - Client.orderController.getCurrentOrder().getDiscountPrice();
        if(discount > 0)
            lblOrderDiscount.setText(discount + " \u20AA");
        else{
            lblDiscount.setVisible(false);
        }
        Client.orderController.getCart().clear();
        MainDashboardController.refreshBalanceLabel();
    }

    @FXML
    void clickBtnContinueShopping(ActionEvent event) throws IOException {
        MainDashboardController.setContentFromFXML("BrowseCatalogPage.fxml");
    }

}
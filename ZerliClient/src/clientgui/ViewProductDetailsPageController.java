package clientgui;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import order.Item;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import static java.lang.String.valueOf;

public class ViewProductDetailsPageController implements Initializable {

    @FXML
    private ImageView productImage;

    @FXML
    private Label priceLabel;

    @FXML
    private Label productID;

    @FXML
    private Label productName;

    @FXML
    private Label productColor;

    @FXML
    private Label productItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productImage.setImage(Client.productController.getProductImages().get(BrowseCatalogPageController.currentProduct.getProductId()));
        productID.setText(valueOf(BrowseCatalogPageController.currentProduct.getProductId()));
        productName.setText(BrowseCatalogPageController.currentProduct.getName());
        productColor.setText(BrowseCatalogPageController.currentProduct.getDominantColor());
        if(BrowseCatalogPageController.currentProduct.getPrice() != BrowseCatalogPageController.currentProduct.getDiscountPrice()){
            priceLabel.setText(BrowseCatalogPageController.currentProduct.getDiscountPrice() + " \u20AA");
        }
        else
            priceLabel.setText(BrowseCatalogPageController.currentProduct.getPrice() + " \u20AA");

        productName.getStyleClass().add("name-label");
        priceLabel.getStyleClass().add("price-label");

        Client.catalogController.getProductItems(BrowseCatalogPageController.currentProduct.getProductId());
        ArrayList<Item> items = (ArrayList<Item>) Client.catalogController.getResponse().getData();

        for(Item item : items)
            productItems.setText(productItems.getText() + item.getName() + " (" + item.getColor() + "), ");

        productItems.setText(productItems.getText().substring(0, productItems.getText().length() - 2));
    }

    public void backBtnClick(ActionEvent actionEvent) {
        MainDashboardController.setContentFromFXML("BrowseCatalogPage.fxml");
    }
}
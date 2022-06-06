package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import product.Item;
import order.Order;
import order.OrderProduct;
import user.Customer;
import user.UserType;
import util.Alert;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static java.lang.String.valueOf;

public class ViewProductDetailsPageController implements Initializable {

    ObservableList<Integer> quantityPicker = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8,9, 10);

    @FXML
    private AnchorPane baseAnchor;

    @FXML
    private Button backBtn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnBuyNow;

    @FXML
    private ImageView productImage;

    @FXML
    private Label lblName;

    @FXML
    private Label lblDetails;

    @FXML
    private Label lblProductNum;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblDiscountPrice;
    @FXML
    private ComboBox<Integer> cbAmount;

    /**
     * Adds to cart the selected product
     * @param event
     */
    @FXML
    void clickBtnAddToCart(ActionEvent event) {
        Integer valueOfQuantity = cbAmount.getValue();

        Client.orderController.addToCart(new OrderProduct(BrowseCatalogPageController.currentProduct, Integer.valueOf(valueOfQuantity)));
        MainDashboardController.createAlert(BrowseCatalogPageController.currentProduct.getName() + " was added to cart", Alert.SUCCESS, Duration.seconds(2), 155, 95);
        MainDashboardController.refreshCartCounter();
    }

    /**
     * Adds to cart the selected product and start directly order process
     * @param event
     */
    @FXML
    void clickBtnBuyNow(ActionEvent event) {

        Integer valueOfQuantity = cbAmount.getValue();

        Client.orderController.addToCart(new OrderProduct(BrowseCatalogPageController.currentProduct, Integer.valueOf(valueOfQuantity)));
        MainDashboardController.refreshCartCounter();
        Client.orderController.setCurrentOrder(new Order());
        Client.orderController.getCurrentOrder().setProductList(Client.orderController.getCart());
        Client.orderController.getCurrentOrder().setDiscountPrice(Client.orderController.getOrderPrice(true));
        Client.orderController.getCurrentOrder().setPrice(Client.orderController.getOrderPrice(false));
        Client.orderController.getCurrentOrder().setCustomerId(Client.userController.getLoggedInUser().getUserId());
        MainDashboardController.setContentFromFXML("OrderDeliveryPage.fxml");
    }

    /**
     * Initializes the controller
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
    public void initialize(URL location, ResourceBundle resources) {
        productImage.setImage(Client.productController.getProductImages().get(BrowseCatalogPageController.currentProduct.getProductId()));
        lblProductNum.setText(String.valueOf(BrowseCatalogPageController.currentProduct.getProductId()));
        lblName.setText(BrowseCatalogPageController.currentProduct.getName());

        if(BrowseCatalogPageController.currentProduct.getPrice() > BrowseCatalogPageController.currentProduct.getDiscountPrice()){
            lblDiscountPrice.setText(BrowseCatalogPageController.currentProduct.getDiscountPrice() + " \u20AA");
            lblPrice.getStyleClass().add("order-label");

        }

        lblPrice.setText(BrowseCatalogPageController.currentProduct.getPrice() + " \u20AA");

        Client.catalogController.getProductItems(BrowseCatalogPageController.currentProduct.getProductId());
        HashMap<Item,Integer> items = (HashMap<Item, Integer>) Client.catalogController.getService().getResponse().getData();
        lblDetails.setText(mapToString(items));

        if(Client.userController.getLoggedInUser() != null && Client.userController.getLoggedInUser().getUserType() == UserType.CUSTOMER && !((Customer) Client.userController.getLoggedInUser()).isBlocked()){
            btnAdd.setVisible(true);
            btnBuyNow.setVisible(true);
            cbAmount.setVisible(true);
        }
        cbAmount.setValue(1);
        cbAmount.getItems().addAll(quantityPicker);



    }

    /**
     * Given hashmap of items, return toString of it
     * @param items
     * @return
     */
    public String mapToString(HashMap<Item,Integer> items) {
        int i = items.size();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Item, Integer> e : items.entrySet()) {
            Item key = e.getKey();
            Integer value = e.getValue();
            sb.append(value +" of " + key.getName());
            if(--i != 0){
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Returns to catalog page
     * @param actionEvent
     */
    public void backBtnClick(ActionEvent actionEvent) {
        MainDashboardController.setContentFromFXML("BrowseCatalogPage.fxml");
    }
}
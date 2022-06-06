package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import order.OrderProduct;
import product.Product;
import user.Customer;
import user.UserType;
import util.Alert;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BrowseCatalogPageController implements Initializable {

    public static Product currentProduct;

    @FXML
    private TilePane tilePane;

    @FXML
    private TilePane tilePaneSingle;

    @FXML
    private ScrollPane sPane;

    @FXML
    private ScrollPane sPaneSingle;

    @FXML
    private TabPane tabPane;

    ObservableList<String> quantityPicker =
            FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPane.getStyleClass().add("tab-pane");

        Client.catalogController.getProducts();
        ArrayList<Product> arrivedList = (ArrayList<Product>) Client.catalogController.getService().getResponse().getData();

        for (Product product : arrivedList) {
            if(!product.isCustomMade())
                tilePane.getChildren().add(createProductTile(product));
            else
                tilePaneSingle.getChildren().add(createProductTile(product));
        }

        sPane.setFitToWidth(true);
        sPaneSingle.setFitToWidth(true);
    }

    /**
     * This function creates the node that will be added to the proper pane (single\multi itemized product)
     * @param product is the product we add to the tile pane
     * @return product tile that is created from data fetched from Data Base
     */
    private Node createProductTile(Product product) {


        HBox hBox = new HBox();
        VBox vBox = new VBox();

        ImageView iv = new ImageView();
        iv.setFitHeight(250.0);
        iv.setFitWidth(150.0);

        Button addBtn = new Button("Add to cart");
        addBtn.getStyleClass().add("btn");
        Button viewDetails = new Button("View details");
        viewDetails.getStyleClass().add("btn-secondary");

        Label nameLabel = new Label(product.getName());
        nameLabel.getStyleClass().add("name-label");
        nameLabel.setWrapText(true);
        nameLabel.setPrefWidth(170);

        ComboBox<String> comboBoxQuantity = new ComboBox<>(quantityPicker);
        comboBoxQuantity.getStyleClass().add("combo-color");

        Text newPrice = new Text();
        Text priceLabel = new Text("Price: " + product.priceToString());
        priceLabel.getStyleClass().add("price-label");

        comboBoxQuantity.getSelectionModel().selectFirst();

        Client.productController.createProductImage(product);
        iv.setImage(Client.productController.getProductImages().get(product.getProductId()));

        // Check which price to display to the customer
        if(product.getPrice() != product.getDiscountPrice()) {
            priceLabel.setStrikethrough(true);
            newPrice.setText("Discount Price: " + product.getDiscountPrice() + " \u20AA");
            newPrice.getStyleClass().add("new-price-label");
        }

        if(Client.userController.getLoggedInUser() != null)
            if(Client.userController.getLoggedInUser().getUserType() == UserType.CUSTOMER && ((Customer) Client.userController.getLoggedInUser()).isBlocked())
                vBox.getChildren().addAll(nameLabel, priceLabel, newPrice, new Label("Color: " + product.getDominantColor()), viewDetails);
            else
                vBox.getChildren().addAll(nameLabel, priceLabel, newPrice, new Label("Color: " + product.getDominantColor()), comboBoxQuantity, addBtn, viewDetails);
        else
            vBox.getChildren().addAll(nameLabel, priceLabel, newPrice, new Label("Color: " + product.getDominantColor()), viewDetails);
        hBox.getChildren().addAll(iv, vBox);
        vBox.setSpacing(15);
        vBox.setId(String.valueOf(product.getProductId()));
        iv.setTranslateY(50);
        hBox.setPadding(new Insets(5, 30, 20, 70));
        vBox.setPadding(new Insets(50, 30, 20, 25));

        addBtn.setOnAction(event -> {
            String valueOfQuantity = comboBoxQuantity.getValue();

            Client.orderController.addToCart(new OrderProduct(product, Integer.valueOf(valueOfQuantity)));
            MainDashboardController.createAlert(product.getName() + " was added to cart", Alert.SUCCESS, Duration.seconds(2), 135, 67);
            MainDashboardController.refreshCartCounter();
        });
        addBtn.setCursor(Cursor.HAND);
        viewDetails.setOnAction(event -> {
            currentProduct = product;
            MainDashboardController.setContentFromFXML("ViewProductDetailsPage.fxml");
        });
        viewDetails.setCursor(Cursor.HAND);
        return hBox;
    }

}
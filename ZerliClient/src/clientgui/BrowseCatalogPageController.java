package clientgui;

import client.Client;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import order.OrderProduct;
import order.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BrowseCatalogPageController implements Initializable {

    @FXML
    private TilePane tilePane;

    @FXML
    private ScrollPane sPane;

    @FXML
    private AnchorPane baseAnchor;

    ObservableList<String> quantityPicker =
            FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    private float sum = Client.orderController.getCart().size();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        new Thread(() -> {
//            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), baseAnchor);
//            fadeTransition.setFromValue(0.0);
//            fadeTransition.setToValue(1.0);
//            fadeTransition.setCycleCount(1);
//            fadeTransition.play();
//        }).start();

        Client.catalogController.getProducts();
        ArrayList<Product> arrivedList = Client.catalogController.getList();

        for (Product product : arrivedList) {
            tilePane.getChildren().add(createProductTile(product));
        }
        sPane.setFitToWidth(true);

    }

    /**
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
        Label nameLabel = new Label(product.getName());
        Text newPrice = new Text();
        ComboBox<String> comboBoxQuantity = new ComboBox<>(quantityPicker);
        Text priceLabel = new Text("Price: " + product.priceToString());

        priceLabel.getStyleClass().add("price-label");
        nameLabel.getStyleClass().add("name-label");
        addBtn.getStyleClass().add("btn");
        comboBoxQuantity.getStyleClass().add("combo-color");
        comboBoxQuantity.getSelectionModel().selectFirst();

        if (product.getImage() != null)
            iv.setImage(new Image(product.getImage()));

        if(product.getPrice() != product.getDiscountPrice()) {
            priceLabel.setStrikethrough(true);
            newPrice.setText("Discount Price: " + product.getDiscountPrice() + " \u20AA");
            newPrice.getStyleClass().add("new-price-label");
        }
//            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0), evt -> newPrice.setVisible(false)),
//                    new KeyFrame(Duration.seconds(0.2), evt -> newPrice.setVisible(true)));
//            timeline.setCycleCount(Animation.INDEFINITE);
//            timeline.play();

        if(Client.userController.getLoggedInUser() != null)
            vBox.getChildren().addAll(nameLabel, priceLabel, newPrice, new Label("Color: " + product.getDominantColor()), comboBoxQuantity, addBtn);
        else
            vBox.getChildren().addAll(nameLabel, priceLabel, newPrice, new Label("Color: " + product.getDominantColor()));
        hBox.getChildren().addAll(iv, vBox);
        vBox.setSpacing(15);
        iv.setTranslateY(50);
        hBox.setPadding(new Insets(50, 30, 50, 70));
        vBox.setPadding(new Insets(50, 30, 50, 25));


        addBtn.setOnAction(event -> {

            String valueOfQuantity = comboBoxQuantity.getValue();
            float currentPrice = product.getPrice();
            String name = product.getName();
            String color = product.getDominantColor();
            if (product.getDiscountPrice() != 0) {
                currentPrice = (product.getPrice() * (100 - product.getDiscountPrice())) / 100;
            }

            sum = sum + Float.parseFloat(valueOfQuantity);

            Client.orderController.addToCart(new OrderProduct(product, Integer.valueOf(valueOfQuantity)));
            MainDashboardController.refreshCartCounter();
            System.out.println(Client.orderController.getCart());
        });

        return hBox;
    }
}
package clientgui;

import client.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import order.Product;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BrowseCatalogFormController implements Initializable {

    @FXML
    private TilePane tilePane;

    @FXML
    private ScrollPane sPane;

    ObservableList<String> quantityPicker =
            FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");


    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        Button addBtn = new Button("Add to cart");
        Label nameLabel = new Label(product.getName());
        Text priceLabel = new Text("Price: " + product.getPrice() + " $");
        Text newPrice = new Text();
        ComboBox<String> comboBoxQuantity = new ComboBox<>(quantityPicker);

        nameLabel.getStyleClass().add("name-label");
        priceLabel.getStyleClass().add("price-label");
        addBtn.getStyleClass().add("btn");
        comboBoxQuantity.getStyleClass().add("combo-color");
        comboBoxQuantity.getSelectionModel().selectFirst();

        if (product.getImage() != null)
            iv.setImage(new Image(product.getImage()));

        if (product.getDiscountPrice() != 0) {
            priceLabel.setStrikethrough(true);
            float onlyPriceDiscount = (product.getPrice() * (100 - product.getDiscountPrice())) / 100;
            newPrice.setText("Discount price: " + onlyPriceDiscount + " $");
            newPrice.getStyleClass().add("new-price-label");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.0), evt -> newPrice.setVisible(false)),
                    new KeyFrame(Duration.seconds(0.2), evt -> newPrice.setVisible(true)));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

        }

        vBox.getChildren().addAll(nameLabel, priceLabel, newPrice, new Label("Color: " + product.getDominantColor()), comboBoxQuantity, addBtn);
        hBox.getChildren().addAll(iv, vBox);
        vBox.setSpacing(15);
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

            //testing communication server and client
            System.out.println("Hi there! Client sending: " + valueOfQuantity + " ," + currentPrice + ", " + name + ", " + color);

            ArrayList<Object> sendAddToCartItems = new ArrayList<>();
            sendAddToCartItems.add(valueOfQuantity);
            sendAddToCartItems.add(currentPrice);
            sendAddToCartItems.add(name);
            sendAddToCartItems.add(color);
            Client.catalogController.orderFromCatalog(sendAddToCartItems);
            System.out.println(Client.catalogController.getRecievedFromCatalog().getData());
            //**************************************
        });

        return hBox;
    }

    /**
     * @param actionEvent will open the login form
     */
    public void clickLoginBtn(ActionEvent actionEvent) {
        //This function will call the new form login for Haim and Marom and will reference "login.fxml"
    }


}
package clientgui;

import client.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import order.OrderProduct;
import product.Product;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerProductBuilderModalController implements Initializable {

    public static ArrayList<Product> arrivedList;

    @FXML
    private AnchorPane baseAnchor;

    @FXML
    private TabPane tabPane;

    @FXML
    private ScrollPane sPane;

    @FXML
    private TilePane tilePane;

    @FXML
    private ScrollPane sPaneSingle;

    @FXML
    private TilePane tilePaneSingle;

    ObservableList<String> quantityPicker =
            FXCollections.observableArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPane.getStyleClass().add("tab-pane");

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
     * @param product is the product we add to the tile pane
     * @return product tile that is created from data fetched from Data Base
     */
    private Node createProductTile(Product product) {
        HBox hBox = new HBox();
        VBox vBox = new VBox();

        ImageView iv = new ImageView();
        iv.setFitHeight(250.0);
        iv.setFitWidth(150.0);

        Button addBtn = new Button("Add to Product");
        addBtn.getStyleClass().add("btn");

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

        if(product.getPrice() != product.getDiscountPrice()) {
            priceLabel.setStrikethrough(true);
            newPrice.setText("Discount Price: " + product.getDiscountPrice() + " \u20AA");
            newPrice.getStyleClass().add("new-price-label");
        }

        if(Client.userController.getLoggedInUser() != null)
            vBox.getChildren().addAll(nameLabel, priceLabel, newPrice, new Label("Color: " + product.getDominantColor()), comboBoxQuantity, addBtn);
        else
            vBox.getChildren().addAll(nameLabel, priceLabel, newPrice, new Label("Color: " + product.getDominantColor()));
        hBox.getChildren().addAll(iv, vBox);
        vBox.setSpacing(15);
        vBox.setId(String.valueOf(product.getProductId()));
        iv.setTranslateY(50);
        hBox.setPadding(new Insets(5, 30, 20, 70));
        vBox.setPadding(new Insets(50, 30, 20, 25));

        addBtn.setOnAction(event -> {
            String valueOfQuantity = comboBoxQuantity.getValue();
            CustomerProductBuilderListPageController.addProductToListView(new OrderProduct(product, Integer.valueOf(valueOfQuantity)));
            createSuccessAlert(product.getName() + " added to your customized product", Duration.seconds(3), 135, 67);
        });

        return hBox;
    }

    public void createSuccessAlert(String message, Duration duration, double layoutX, double layoutY) {
        TextFlow textFlow = new TextFlow();
        Text text = new Text(message);

        text.setFill(Color.web("#0f5132"));
        textFlow.getChildren().add(text);
        textFlow.getStyleClass().add("text-flow");
        textFlow.getStyleClass().add("text-flow-success");
        textFlow.setLayoutX(layoutX);
        textFlow.setLayoutY(layoutY);

        baseAnchor.getChildren().add(textFlow);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        duration,
                        event -> {
                            baseAnchor.getChildren().remove(textFlow);
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}

package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import product.Item;
import order.OrderProduct;
import util.Alert;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CustomerProductBuilderListPageController implements Initializable {

    public static ListView<Object> listSelectedProductsBox;
    public static Label totalLabelBox;
    public static ArrayList<ComboBox> comboBoxQuantityArrayBox;
    public static ArrayList<OrderProduct> listAddedProductsBox;

    private final ArrayList<OrderProduct> listAddedProducts = new ArrayList<>();
    static ObservableList<String> quantityPicker = FXCollections.observableArrayList("0", "1", "2", "3","4","5","6","7","8","9","10");
    private final ArrayList<ComboBox> comboBoxQuantityArray = new ArrayList<>();

    @FXML
    private ListView<Object> listSelectedProducts;

    @FXML
    private Button btnFinish;

    @FXML
    private Label totalLabel;

    @FXML
    private Button btnBrowseProducts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listSelectedProductsBox = listSelectedProducts;
        totalLabelBox = totalLabel;
        comboBoxQuantityArrayBox = comboBoxQuantityArray;
        listAddedProductsBox = listAddedProducts;
        listSelectedProducts.setPlaceholder(new Label("Use the 'Browse Products' button and build your own."));
    }

    @FXML
    void clickBtnBrowseProducts(ActionEvent event) throws IOException {
        Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        Parent content = FXMLLoader.load(MainDashboardController.class.getResource("CustomerProductBuilderModal.fxml"));
        Scene dialogScene = new Scene(content, 990, 660);
        dialog.setScene(dialogScene);
        dialog.setTitle("Product Builder - Browse Products");
        dialog.show();
    }

    @FXML
    void clickBtnFinish(ActionEvent event) throws IOException, SQLException, InterruptedException {
        if(listAddedProducts.isEmpty()) {
            MainDashboardController.createAlert("You must add products to your product to finish", Alert.DANGER, Duration.seconds(2), 135, 67);
        } else {
            HashMap<Item, Integer> productItems = new HashMap<Item, Integer>();
            float totalPrice = 0;
            float totalDiscountPrice = 0;

            /* Loops all added products */
            for(OrderProduct op : listAddedProducts) {
                Client.catalogController.getProductItems(op.getProduct().getProductId());
                HashMap<Item, Integer> currentProductItems = (HashMap<Item, Integer>) Client.catalogController.getService().getResponse().getData();

                /* Loops all the items in every added product */
                for(Item item : currentProductItems.keySet()) {
                    boolean updated = false;

                    /* Loops all the items in the new created product */
                     for(Item addedItem : productItems.keySet()) {
                         /* if current item already exists in the newly created product, update its quantity */
                         if(item.getItemId() == addedItem.getItemId()) {
                             productItems.put(addedItem, productItems.get(addedItem) + (currentProductItems.get(item) * op.getQuantity()));
                             updated = true;
                             break;
                         }
                     }

                     if(!updated)
                         productItems.put(item, currentProductItems.get(item) * op.getQuantity());
                }

                totalPrice += (op.getProduct().getPrice() * op.getQuantity());
                totalDiscountPrice += (op.getProduct().getDiscountPrice() * op.getQuantity());
            }

            ImageView imageView = new ImageView("/flower.png");

            BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
            Graphics2D g2d = bImage.createGraphics();
            g2d.drawImage(bImage, 0, 0, null);
            g2d.dispose();
            ByteArrayOutputStream baos =new ByteArrayOutputStream();
            ImageIO.write(bImage, "png", baos);

            byte[] fileContent = baos.toByteArray();

            Client.catalogController.createProduct.setPrice(totalPrice);
            Client.catalogController.createProduct.setDiscountPrice(totalDiscountPrice);
            Client.catalogController.createProduct.setImage(new SerialBlob(fileContent));
            Client.catalogController.createProduct.setDominantColor("Custom");
            Client.catalogController.createProduct.setItems(productItems);
            Client.catalogController.createProduct.setCustomMade(false);
            Client.catalogController.createProduct.setCustomerProduct(true);

            Client.orderController.addToCart(new OrderProduct(Client.catalogController.createProduct, 1));
            MainDashboardController.refreshCartCounter();
            MainDashboardController.createAlert(Client.catalogController.createProduct.getName() + " is built successfully and added to cart", Alert.SUCCESS, Duration.seconds(5), 135, 67);

            btnBrowseProducts.setDisable(true);
            btnFinish.setDisable(true);
        }
    }

    public static void addProductToListView(OrderProduct product) {
        OrderProduct existingProduct = getProductByName(product.getProduct().getName());

        if(existingProduct != null) {
            for(Object box : listSelectedProductsBox.getItems()) {
                String currentProductId = ((HBox) box).getId();

                if(Integer.valueOf(currentProductId) == product.getProduct().getProductId()) {
                    ((ComboBox<String>)((HBox) box).getChildren().get(2)).setValue(String.valueOf(Integer.valueOf(((ComboBox<String>)((HBox) box).getChildren().get(2)).getValue()) + product.getQuantity()));
                    break;
                }
            }
        } else {
            ImageView iv = new ImageView();
            iv.setFitHeight(150);
            iv.setFitWidth(130);
            Client.productController.createProductImage(product.getProduct());
            iv.setImage(Client.productController.getProductImages().get(product.getProduct().getProductId()));
            Label imageLabel = new Label(null, iv);
            Label nameLabel = new Label(product.getProduct().getName() + "\n" + product.getProduct().getDominantColor() + "\n" + product.getProduct().customMadeToString());
            Label priceLabel = new Label(product.getQuantity() * product.getProduct().getPrice() + " \u20AA");
            Label discountPriceLabel = new Label(product.getQuantity() * product.getProduct().getDiscountPrice() + " \u20AA");
            if (product.getProduct().getPrice() > product.getProduct().getDiscountPrice()) {
                priceLabel.getStyleClass().add("order-label");
                discountPriceLabel.setStyle("-fx-text-fill: red");
                discountPriceLabel.setFont(new Font(18));
                discountPriceLabel.setPrefWidth(80);
            } else {
                discountPriceLabel.setVisible(false);
            }
            nameLabel.setFont(new Font(18));
            nameLabel.setAlignment(Pos.CENTER_LEFT);
            nameLabel.setStyle("-fx-text-fill: #77385a");
            nameLabel.setPrefWidth(200);
            nameLabel.setWrapText(true);

            priceLabel.setFont(new Font(18));
            priceLabel.setPrefWidth(80);
            priceLabel.setStyle("-fx-text-fill: #77385a");

            ComboBox<String> comboBoxQuantity = new ComboBox<>(quantityPicker);
            comboBoxQuantity.setOnAction(event -> {
                int rowNumber = comboBoxQuantityArrayBox.indexOf(comboBoxQuantity);
                listSelectedProductsBox.getSelectionModel().select(rowNumber);
                HBox productHBox = (HBox) listSelectedProductsBox.getSelectionModel().getSelectedItem();
                String productName = nameLabel.getText();
                String productAmount = comboBoxQuantity.getValue();
                int newAmount = Integer.parseInt(productAmount);
                String[] splitNameLabelString = productName.split("\n");
                OrderProduct op = getProductByName(splitNameLabelString[0]);
                if (newAmount == 0) {
                    listSelectedProductsBox.getItems().remove(productHBox);
                    listAddedProductsBox.remove(op);
                } else {
                    listAddedProductsBox.get(listAddedProductsBox.indexOf(op)).setQuantity(newAmount);
                    if (op == null) {//if is empty
                        return;
                    }
                    ((Label) productHBox.getChildren().get(3)).setText(op.getQuantity() * op.getProduct().getPrice() + " \u20AA");
                    ((Label) productHBox.getChildren().get(4)).setText(op.getQuantity() * op.getProduct().getDiscountPrice() + " \u20AA");
                }

                updateTotalLabel();
            });
            comboBoxQuantity.getSelectionModel().select(product.getQuantity());
            comboBoxQuantity.setBackground(Background.EMPTY);

            HBox h = new HBox(30, imageLabel, nameLabel, comboBoxQuantity, priceLabel, discountPriceLabel);
            HBox.setHgrow(nameLabel, Priority.max(Priority.ALWAYS, Priority.ALWAYS));
            h.setAlignment(Pos.CENTER_LEFT);
            h.setId(String.valueOf(product.getProduct().getProductId()));
            listSelectedProductsBox.getItems().addAll(h);
            listAddedProductsBox.add(product);
            comboBoxQuantityArrayBox.add(comboBoxQuantity);
        }

        updateTotalLabel();
    }

    private static void updateTotalLabel() {
        float totalPrice = 0;

        for(OrderProduct op : listAddedProductsBox)
            totalPrice += (op.getProduct().getDiscountPrice() * op.getQuantity());

        totalLabelBox.setText("Total: " + totalPrice + " \u20AA");

    }

    public static OrderProduct getProductByName(String productName){
        for (OrderProduct op : listAddedProductsBox){
            if (op.getProduct().getName().equals(productName)){
                return op;
            }
        }
        return null;
    }

}

package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import order.Item;
import order.Product;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

/**
 * TODO: Add page validation.
 */
public class ProductAddPageController implements Initializable {

    private ArrayList<Item> availableItems;
    private SerialBlob uploadedImage;
    private ObservableList<Item> items = FXCollections.observableArrayList();
    private ObservableList<Item> itemsAdded = FXCollections.observableArrayList();

    @FXML
    private TextField fldName;

    @FXML
    private Label lblName;

    @FXML
    private Button btnAddProduct;

    @FXML
    private Label lblMessage;

    @FXML
    private Button btnBack;

    @FXML
    private ListView<Item> listItems;

    @FXML
    private ListView<Item> listProduct;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRemove;

    @FXML
    private Label lblItems;

    @FXML
    private Label lblPrice;

    @FXML
    private TextField fldPrice;

    @FXML
    private Label lblImage;

    @FXML
    private Button btnUploadImage;

    @FXML
    private Label lblName112;

    @FXML
    private ComboBox<?> cbColor;

    @FXML
    private Label lblPictureName;

    @FXML
    private ImageView imgProduct;

    @FXML
    private Label lblAddMessage;

    @FXML
    private Label lblCalculatedPrice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> colorList = new ArrayList<String>();

        colorList.add("Black");
        colorList.add("White");
        colorList.add("Gray");
        colorList.add("Silver");
        colorList.add("Maroon");
        colorList.add("Red");
        colorList.add("Purple");
        colorList.add("Pink");
        colorList.add("Green");
        colorList.add("Lime");
        colorList.add("Olive");
        colorList.add("Yellow");
        colorList.add("Navy");
        colorList.add("Blue");
        colorList.add("Teal");
        colorList.add("Aqua");

        ObservableList observableList = FXCollections.observableList(colorList);
        cbColor.setItems(observableList);

        Client.itemController.getItems();
        availableItems = (ArrayList<Item>) Client.itemController.getResponse().getData();

        for (Item item : (ArrayList<Item>) Client.itemController.getResponse().getData())
            items.add(item);

        listItems.setItems(items);
        listProduct.setItems(itemsAdded);
    }

    @FXML
    void clickBtnAdd(ActionEvent event) {
        if(listItems.getSelectionModel().getSelectedItem() == null) {
            lblAddMessage.setVisible(true);
            lblAddMessage.setText("Select an item from left list");
            return;
        }

        lblAddMessage.setVisible(false);
        Item selectedItem = listItems.getSelectionModel().getSelectedItem();
        items.remove(selectedItem);
        itemsAdded.add(selectedItem);
        updateCalculatedPrice();
    }

    @FXML
    void clickBtnRemove(ActionEvent event) {
        if(listProduct.getSelectionModel().getSelectedItem() == null) {
            lblAddMessage.setVisible(true);
            lblAddMessage.setText("Select an item from right list");
            return;
        }

        lblAddMessage.setVisible(false);
        Item selectedItem = listProduct.getSelectionModel().getSelectedItem();
        itemsAdded.remove(selectedItem);
        items.add(selectedItem);
        updateCalculatedPrice();
    }

    @FXML
    void clickBtnAddProduct(ActionEvent event) throws IOException, SerialException {

        HashMap<Item, Integer> items = new HashMap<>();
        boolean customMade = false;

        for(Item item : availableItems)
            items.put(item, 1);

        if(availableItems.size() == 1)
            customMade = true;

        Product product = new Product(fldName.getText(), items, Float.valueOf(fldPrice.getText()), Float.valueOf(fldPrice.getText()), uploadedImage, customMade, cbColor.getValue().toString());
        Client.productController.addProduct(product);
    }

    @FXML
    void clickBtnBack(ActionEvent event) {
        MainDashboardController.setContentFromFXML("UserHomePage.fxml");
    }

    @FXML
    void clickBtnUploadImage(ActionEvent event) throws IOException, SQLException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File file = fileChooser.showOpenDialog(null);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        uploadedImage = new SerialBlob(fileContent);

        lblPictureName.setText(file.getName());
        lblPictureName.setVisible(true);
    }

    public void updateCalculatedPrice() {
        float price = 0.0f;

        for(Item item : itemsAdded) {
            price += item.getPrice();
        }
        if(price != 0.0f) {
            lblCalculatedPrice.setText("(Calculated Price: " + price + ")");
            lblCalculatedPrice.setVisible(true);
        } else
            lblCalculatedPrice.setVisible(false);
    }
}

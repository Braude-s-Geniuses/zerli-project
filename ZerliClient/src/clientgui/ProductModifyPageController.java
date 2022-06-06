package clientgui;

import client.Client;
import communication.MessageFromServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import product.Item;
import product.Product;
import user.BranchEmployee;
import user.UserType;
import util.Alert;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.*;

public class ProductModifyPageController implements Initializable {

    private static Product currentProduct;

    private ArrayList<Item> availableItems;
    private SerialBlob uploadedImage;
    private final ObservableList<Item> items = FXCollections.observableArrayList();
    private final ObservableList<ItemWithQuantity> itemsAdded = FXCollections.observableArrayList();
    private final List<Integer> choices = new ArrayList<>();

    @FXML
    private TextField fldName;

    @FXML
    private Label lblName;

    @FXML
    private Button btnModifyProduct;

    @FXML
    private Label lblNameMessage;

    @FXML
    private Button btnBack;

    @FXML
    private ListView<Item> listItems;

    @FXML
    private ListView<ItemWithQuantity> listProduct;

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
    private Label lblColor;

    @FXML
    private ComboBox<String> cbColor;

    @FXML
    private Label lblPictureName;

    @FXML
    private Label lblAddMessage;

    @FXML
    private Label lblCalculatedPrice;

    @FXML
    private Label lblItemsMessage;

    @FXML
    private Label lblPriceMessage;

    @FXML
    private Label lblDiscountPriceMessage;

    @FXML
    private Label lblColorMessage;

    @FXML
    private TextField fldDiscountPrice;

    @FXML
    private CheckBox cbInCatalog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideErrorsLabels();
        if(Client.userController.getLoggedInUser().getUserType() == UserType.BRANCH_EMPLOYEE) {
            Client.userController.getPermissions(Client.userController.getLoggedInUser());
            BranchEmployee branchEmployee = Client.userController.getBranchEmployeeForInformation();

            if (!branchEmployee.isDiscount())
                fldDiscountPrice.setDisable(true);
        }

        for (int i = 1; i <= 20; i++)
            choices.add(i);

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
        availableItems = (ArrayList<Item>) Client.itemController.getService().getResponse().getData();

        for (Item item : (ArrayList<Item>) Client.itemController.getService().getResponse().getData())
            items.add(item);

        listItems.setItems(items);
        listProduct.setItems(itemsAdded);

        if(currentProduct != null) {
            fldName.setText(currentProduct.getName());
            cbInCatalog.setSelected(currentProduct.isInCatalog());
            fldPrice.setText(String.valueOf(currentProduct.getPrice()));
            fldDiscountPrice.setText(String.valueOf(currentProduct.getDiscountPrice()));
            cbColor.setValue(currentProduct.getDominantColor());

            Client.productController.getProductItems(currentProduct.getProductId());

            HashMap<Item, Integer> productItems = (HashMap<Item, Integer>) Client.productController.getService().getResponse().getData();

            currentProduct.setItems(productItems);

            for(Item itemInProduct : currentProduct.getItems().keySet()) {
                ItemWithQuantity itemWithQuantity = new ItemWithQuantity(itemInProduct, currentProduct.getItems().get(itemInProduct));
                itemsAdded.add(itemWithQuantity);
            }

            listProduct.refresh();
            updateCalculatedPrice();
        }
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

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choices);
        dialog.setTitle("Zerli Client");
        dialog.setHeaderText("Select quantity of " + selectedItem.getName() + " you wish you add.");
        dialog.setContentText("Quantity:");

        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(selectedQuantity -> {
            boolean done = false;
            for(ItemWithQuantity itemWithQuantity : itemsAdded) {
                if (itemWithQuantity.item.getItemId() == selectedItem.getItemId()) {
                    itemWithQuantity.quantity += result.get();
                    done = true;
                    listProduct.refresh();
                    break;
                }
            }

            if(!done) {
                itemsAdded.add(new ItemWithQuantity(selectedItem, result.get()));
            }

            updateCalculatedPrice();
        });
    }

    @FXML
    void clickBtnBack(ActionEvent event) {
        MainDashboardController.setContentFromFXML("ProductsManagePage.fxml");
    }

    @FXML
    void clickBtnRemove(ActionEvent event) {
        if(listProduct.getSelectionModel().getSelectedItem() == null) {
            lblAddMessage.setVisible(true);
            lblAddMessage.setText("Select an item from right list");
            return;
        }

        lblAddMessage.setVisible(false);
        ItemWithQuantity selectedItem = listProduct.getSelectionModel().getSelectedItem();
        itemsAdded.remove(selectedItem);
        updateCalculatedPrice();
    }

    @FXML
    void clickBtnUploadImage(ActionEvent event) throws IOException, SQLException {
        FileChooser fileChooser = new FileChooser();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File file = fileChooser.showOpenDialog(null);
        if(file != null) {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            uploadedImage = new SerialBlob(fileContent);

            lblPictureName.setText(file.getName());
            lblPictureName.setVisible(true);
        }
    }

    @FXML
    void clickBtnModifyProduct(ActionEvent event) throws IOException, SerialException {
        hideErrorsLabels();

        if(validateBeforeSubmit()) {
            HashMap<Item, Integer> items = new HashMap<>();
            boolean customMade = false;

            for(ItemWithQuantity itemWithQuantity : itemsAdded)
                items.put(itemWithQuantity.item, itemWithQuantity.quantity);

            if(itemsAdded.size() == 1 && itemsAdded.get(0).quantity == 1)
                customMade = true;

            currentProduct.setName(fldName.getText());
            currentProduct.setItems(items);
            currentProduct.setPrice(Float.valueOf(fldPrice.getText()));
            currentProduct.setDiscountPrice(Float.valueOf(fldDiscountPrice.getText()));
            currentProduct.setCustomMade(customMade);
            currentProduct.setDominantColor(cbColor.getValue());
            currentProduct.setInCatalog(cbInCatalog.isSelected());
            if(uploadedImage != null)
                currentProduct.setImage(uploadedImage);

            Client.productController.updateProduct(currentProduct);

            if(Client.productController.getService().getResponse().getAnswer() == MessageFromServer.PRODUCT_UPDATE_SUCCESS) {
                MainDashboardController.createAlert("Product updated successfully!", Alert.SUCCESS, Duration.seconds(3), 135, 67);
            } else {
                MainDashboardController.createAlert("Failed to update product.", Alert.DANGER, Duration.seconds(3), 135, 67);
            }
        }
    }

    private void updateCalculatedPrice() {
        float price = 0.0f;

        for(ItemWithQuantity itemWithQuantity : itemsAdded) {
            price += itemWithQuantity.item.getPrice() * itemWithQuantity.quantity;
        }
        if(price != 0.0f) {
            lblCalculatedPrice.setText("(Calculated Price: " + price + ")");
            lblCalculatedPrice.setVisible(true);
        } else
            lblCalculatedPrice.setVisible(false);
    }

    private boolean validateBeforeSubmit() {
        boolean validated = true;
        if(fldName.getText().isEmpty()) {
            lblNameMessage.setVisible(true);
            validated = false;
        }

        if(itemsAdded.isEmpty()) {
            lblItemsMessage.setVisible(true);
            validated = false;
        }

        if(fldPrice.getText().isEmpty()) {
            lblPriceMessage.setVisible(true);
            validated = false;
        }

        try {
            Float.parseFloat(fldPrice.getText());

        } catch (NumberFormatException e) {
            lblPriceMessage.setVisible(true);
            validated = false;
        }

        if(fldDiscountPrice.getText().isEmpty()) {
            lblDiscountPriceMessage.setVisible(true);
            validated = false;
        }

        try {
            Float.parseFloat(fldDiscountPrice.getText());

            if(Float.parseFloat(fldDiscountPrice.getText()) > Float.parseFloat(fldPrice.getText()))
                throw new NumberFormatException();
        } catch (NumberFormatException e) {
            lblDiscountPriceMessage.setVisible(true);
            validated = false;
        }

        if(cbColor.getSelectionModel().isEmpty()) {
            lblColorMessage.setVisible(true);
            validated = false;
        }

        return validated;
    }

    private void hideErrorsLabels() {
        lblNameMessage.setVisible(false);
        lblItemsMessage.setVisible(false);
        lblPriceMessage.setVisible(false);
        lblDiscountPriceMessage.setVisible(false);
        lblColorMessage.setVisible(false);
    }

    public static void setCurrentProduct(Product product) {
        currentProduct = product;
    }

    private class ItemWithQuantity {
        Item item;
        int quantity;

        ItemWithQuantity(Item item, int quantity) {
            this.item = item;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return item.getName() + " (" + quantity + ")";
        }
    }

    private Item getItemById(int itemId) {
        for(Item item : availableItems) {
            if(item.getItemId() == itemId)
                return item;
        }

        return null;
    }
}

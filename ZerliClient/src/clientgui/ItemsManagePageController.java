package clientgui;

import client.Client;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import product.Item;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemsManagePageController implements Initializable {

    /**
     * stores the currentItem being modified
     */
    private Item currentItem;

    /**
     * stores a list of all the items retrieved from the database on initialization
     */
    private final ObservableList<Item> items = FXCollections.observableArrayList();

    @FXML
    private TableView<Item> tableItems;

    @FXML
    private TableColumn<Item, String> columnName;

    @FXML
    private TableColumn<Item, String> columnType;

    @FXML
    private TableColumn<Item, String> columnColor;

    @FXML
    private TableColumn<Item, Float> columnPrice;

    @FXML
    private TableColumn<Item, Boolean> columnAction;

    @FXML
    private AnchorPane paneModifyItem;

    @FXML
    private TextField fldName;

    @FXML
    private TextField fldPrice;

    @FXML
    private ComboBox<String> cbType;

    @FXML
    private ComboBox<String> cbColor;

    @FXML
    private Button btnAddItem;

    @FXML
    private Button btnUpdateItem;

    @FXML
    private Button btnDeleteItem;

    @FXML
    private Label lblCloseItem;

    @FXML
    private Label lblUpdateItemMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paneModifyItem.setVisible(false);
        List<String> colorList = new ArrayList<String>();
        List<String> typeList = new ArrayList<String>();

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
        cbColor.getItems().clear();
        cbColor.setItems(observableList);
        cbColor.getStyleClass().add("combo-time");

        typeList.add("Branch");
        typeList.add("Flower");
        typeList.add("Leaf");

        observableList = FXCollections.observableList(typeList);
        cbType.getItems().clear();
        cbType.setItems(observableList);
        cbType.getStyleClass().add("combo-time");

        columnName.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        columnType.setCellValueFactory(new PropertyValueFactory<Item, String>("type"));
        columnColor.setCellValueFactory(new PropertyValueFactory<Item, String>("color"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Item, Float>("price"));
        columnAction.setSortable(false);

        Client.itemController.getItems();
        for (Item item : (ArrayList<Item>) Client.itemController.getService().getResponse().getData())
            items.add(item);

        tableItems.setItems(items);

        // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        columnAction.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Item, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });

        columnAction.setCellFactory(new Callback<TableColumn<Item, Boolean>, TableCell<Item, Boolean>>() {
            @Override
            public TableCell<Item, Boolean> call(TableColumn<Item, Boolean> param) {
                return new AddItemCell(tableItems);
            }
        });
    }

    /**
     * Internal class used to allow to add an action button of 'Modify'
     * to each TableRow
     */
    private class AddItemCell extends TableCell<Item, Boolean> {
        final Button modifyButton = new Button("Modify");
        final StackPane paddedButton = new StackPane();
        final DoubleProperty buttonY = new SimpleDoubleProperty();

        AddItemCell(final TableView table) {
            modifyButton.getStyleClass().add("btn");
            paddedButton.setPadding(new Insets(1));
            paddedButton.getChildren().add(modifyButton);
            modifyButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    buttonY.set(event.getScreenY());
                }
            });
            modifyButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    paneModifyItem.setVisible(true);
                    lblUpdateItemMessage.setText("");
                    btnUpdateItem.setDisable(false);
                    btnDeleteItem.setDisable(false);
                    currentItem = (Item) table.getItems().get(getTableRow().getIndex());
                    fldName.setText(currentItem.getName());
                    cbType.setValue(currentItem.getType());
                    cbColor.setValue(currentItem.getColor());
                    fldPrice.setText(Float.toString(currentItem.getPrice()));
                }
            });
        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if(!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(paddedButton);
            }
            else {
                setGraphic(null);
            }
        }
    }

    @FXML
    void clickCloseItem() {
        paneModifyItem.setVisible(false);
    }

    @FXML
    void clickBtnAddItem(ActionEvent event) {
        MainDashboardController.setContentFromFXML("ItemAddPage.fxml");
    }

    @FXML
    void clickBtnUpdateItem(ActionEvent event) {
        boolean itemChanged = false;
        Item updatedItem = new Item(currentItem.getItemId(), currentItem.getName(), currentItem.getType(), currentItem.getColor(), currentItem.getPrice());

        if(!fldName.getText().isEmpty() && !fldName.getText().equals(currentItem.getName())) {
            itemChanged = true;
            updatedItem.setName(fldName.getText());
        }

        if(cbType.getValue() != null && !cbType.getValue().equals("") && !cbType.getValue().equals(currentItem.getType())) {
            itemChanged = true;
            updatedItem.setType(cbType.getValue());
        }

        if(!cbColor.getSelectionModel().isEmpty() && !cbColor.getValue().equals(currentItem.getColor())) {
            itemChanged = true;
            updatedItem.setColor(cbColor.getValue());
        }

        float price;

        if(!fldPrice.getText().isEmpty()) {
            try {
                price = Float.parseFloat(fldPrice.getText());

                if(price <= 0.0) {
                    lblUpdateItemMessage.setStyle("-fx-text-fill: indianred");
                    lblUpdateItemMessage.setText("You must enter a price greater than 0");
                    return;
                }
                if(price != currentItem.getPrice()) {
                    itemChanged = true;
                    updatedItem.setPrice(price);
                }
            } catch(NumberFormatException e) {
                lblUpdateItemMessage.setStyle("-fx-text-fill: indianred");
                lblUpdateItemMessage.setText("You must enter a valid price");
                return;
            }
        }

        if(!itemChanged) {
            lblUpdateItemMessage.setStyle("-fx-text-fill: indianred");
            lblUpdateItemMessage.setText("No changes were made");
        }
        else {
            Client.itemController.updateItem(updatedItem);

            switch(Client.itemController.getService().getResponse().getAnswer()) {
                case ITEM_UPDATE_SUCCESS:
                    lblUpdateItemMessage.setStyle("-fx-text-fill: green");
                    lblUpdateItemMessage.setText("Item updated successfully");

                    for (Item item : items) {
                        if(item.getItemId() == currentItem.getItemId()) {
                            item.setName(updatedItem.getName());
                            item.setType(updatedItem.getType());
                            item.setColor(updatedItem.getColor());
                            item.setPrice(updatedItem.getPrice());
                            tableItems.refresh();
                            break;
                        }
                    }
                    break;
                case ITEM_UPDATE_FAIL:
                    lblUpdateItemMessage.setStyle("-fx-text-fill: indianred");
                    lblUpdateItemMessage.setText("Item name already exists");
            }
        }
    }

    @FXML
    void clickBtnDeleteItem(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Item: " + currentItem.getName());
        alert.setContentText("Are you sure you want to delete this item?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Client.itemController.deleteItem(currentItem);

            switch(Client.itemController.getService().getResponse().getAnswer()) {
                case ITEM_DELETE_SUCCESS:
                    lblUpdateItemMessage.setStyle("-fx-text-fill: green");
                    lblUpdateItemMessage.setText("Item deleted successfully");
                    btnUpdateItem.setDisable(true);
                    btnDeleteItem.setDisable(true);

                    for (Item item : items) {
                        if(item.getItemId() == currentItem.getItemId()) {
                            items.remove(item);
                            tableItems.refresh();
                            break;
                        }
                    }
                    break;
                case ITEM_DELETE_FAIL:
                    lblUpdateItemMessage.setStyle("-fx-text-fill: indianred");
                    lblUpdateItemMessage.setText("Failed to delete item");
                    break;
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }
}

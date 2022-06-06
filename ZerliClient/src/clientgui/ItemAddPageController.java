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
import javafx.scene.control.TextField;
import javafx.util.Duration;
import product.Item;
import util.Alert;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ItemAddPageController implements Initializable {

    @FXML
    private TextField fldName;

    @FXML
    private TextField fldPrice;

    @FXML
    private Label lblName;

    @FXML
    private Label lblColor;

    @FXML
    private Label lblType;

    @FXML
    private Label lblPrice;

    @FXML
    private Label lblMessage;

    @FXML
    private ComboBox cbType;

    @FXML
    private ComboBox cbColor;

    @FXML
    private Button btnSubmit;

    @FXML
    void clickBtnSubmit(ActionEvent event) {
        if(fldName.getText().isEmpty()) {
            lblMessage.setStyle("-fx-text-fill: indianred");
            lblMessage.setText("You must type a new name");
            return;
        }

        if(cbType.getValue() == null || cbType.getValue().equals("")) {
            lblMessage.setStyle("-fx-text-fill: indianred");
            lblMessage.setText("You must select a type");
            return;
        }

        if(cbColor.getSelectionModel().isEmpty()) {
            lblMessage.setStyle("-fx-text-fill: indianred");
            lblMessage.setText("You must select a color");
            return;
        }

        if(fldPrice.getText().isEmpty()) {
            lblMessage.setStyle("-fx-text-fill: indianred");
            lblMessage.setText("You must enter a price");
            return;
        }

        float price;

        try {
            price = Float.parseFloat(fldPrice.getText());

            if(price <= 0.0) {
                lblMessage.setStyle("-fx-text-fill: indianred");
                lblMessage.setText("You must enter a price greater than 0");
                return;
            }
        } catch(NumberFormatException e) {
            lblMessage.setStyle("-fx-text-fill: indianred");
            lblMessage.setText("You must enter a valid price");
            return;
        }

        Item item = new Item(fldName.getText(), cbType.getValue().toString(), cbColor.getValue().toString(), price);
        Client.itemController.addItem(item);

        switch(Client.itemController.getService().getResponse().getAnswer()) {
            case ITEM_ADD_SUCCESS:
                MainDashboardController.setContentFromFXML("ItemAddPage.fxml");
                MainDashboardController.createAlert("Item successfully added", Alert.SUCCESS, Duration.seconds(3), 135, 67);
                break;
            case ITEM_ADD_FAIL:
                lblMessage.setStyle("-fx-text-fill: indianred");
                lblMessage.setText("Item name already exists");
        }
    }

    @FXML
    void clickBtnBack(ActionEvent event) {
        MainDashboardController.setContentFromFXML("UserHomePage.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

        typeList.add("Branch");
        typeList.add("Flower");
        typeList.add("Leaf");

        observableList = FXCollections.observableList(typeList);
        cbType.getItems().clear();
        cbType.setItems(observableList);
    }
}

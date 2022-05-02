package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import order.Product;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BrowseCatalogFormController implements Initializable {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<Object> listView;

    ObservableList<Product> productList = FXCollections.observableArrayList();
    ObservableList<String> quantityPicker =
            FXCollections.observableArrayList("1", "2", "3","4","5","6","7","8","9","10");
    ObservableList<String> colorPicker =
            FXCollections.observableArrayList("Red","Pink","White");


    Image image = new Image("flower.png");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Client.catalogController.getProducts();

        ArrayList<Product> arrivedList = Client.catalogController.getList();

//        for (int i = 0; i < 20; i++) {
//            productList.add(new Product(i, "hello",i , i, image));
//        }

//        for (int i = 0; i < productList.size(); i++) {
//
//            Label label = new Label("Very beautiful Rose " + i , new ImageView(productList.get(i).getImage()));
//            label.setFont(new Font(22));
//            Button addBtn = new Button("Add to cart");
//            addBtn.getStyleClass().add("btn");
//            ComboBox comboBoxQuantity = new ComboBox(quantityPicker);
//            ComboBox comboBoxColor = new ComboBox(colorPicker);
//            comboBoxQuantity.getSelectionModel().selectFirst();
//            comboBoxColor.getSelectionModel().selectFirst();
//            HBox h = new HBox( label, addBtn,comboBoxQuantity,comboBoxColor);
//            h.setSpacing(120.0);
//            h.setAlignment(Pos.CENTER_LEFT);
//            listView.getItems().addAll(h);
//        }


        Label label = new Label(arrivedList.get(0).getName(), new ImageView(arrivedList.get(0).getImage()));
        label.setFont(new Font(22));
        Button addBtn = new Button("Add to cart");
        addBtn.getStyleClass().add("btn");
        ComboBox comboBoxQuantity = new ComboBox(quantityPicker);
        ComboBox comboBoxColor = new ComboBox(colorPicker);
        comboBoxQuantity.getSelectionModel().selectFirst();
        comboBoxColor.getSelectionModel().selectFirst();
        HBox h = new HBox( label, addBtn,comboBoxQuantity,comboBoxColor);
        h.setSpacing(120.0);
        h.setAlignment(Pos.CENTER_LEFT);
        listView.getItems().addAll(h);


    }


    public void clickLoginBtn(ActionEvent actionEvent) {
    }
}
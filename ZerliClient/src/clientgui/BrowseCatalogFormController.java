package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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

    @FXML
     private Button loginBtn;


    ObservableList<String> quantityPicker =
            FXCollections.observableArrayList("1", "2", "3","4","5","6","7","8","9","10");//will be replaced with quantity from database product
    ObservableList<String> colorPicker =
            FXCollections.observableArrayList("Red","Pink","White");//will be replaced with color from database product




    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Client.catalogController.getProducts();

        ArrayList<Product> arrivedList = Client.catalogController.getList();


        for(int i=0 ; i< arrivedList.size() ; i++) {
            Image image = new Image(arrivedList.get(i).getImage());
            Label label = new Label(arrivedList.get(i).getName() , new ImageView(image));
            label.setFont(new Font(16));
            Button addBtn = new Button("Add to cart");
            addBtn.getStyleClass().add("btn");

            addBtn.setOnAction(new EventHandler() {

                @Override
                public void handle(Event event) {
                    //here i will send the description of order (which item,quantity and color with price)
                    System.out.println("Hi there! You clicked me!");
                }

            });

            ComboBox comboBoxQuantity = new ComboBox(quantityPicker);
            ComboBox comboBoxColor = new ComboBox(colorPicker);
            comboBoxQuantity.getSelectionModel().selectFirst();
            comboBoxColor.getSelectionModel().selectFirst();
            HBox h = new HBox(label,new Label("Price: " +arrivedList.get(i).getPrice() + "₪"), addBtn, comboBoxQuantity, comboBoxColor);
            h.setSpacing(70.0);
            h.setAlignment(Pos.CENTER_LEFT);
            listView.getItems().addAll(h);
        }

    }


    public void clickLoginBtn(ActionEvent actionEvent) {

    }

    public void clickAddBtn(ActionEvent actionEvent){

    }

}
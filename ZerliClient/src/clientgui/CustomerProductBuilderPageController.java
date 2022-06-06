package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import product.Product;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerProductBuilderPageController implements Initializable {
    private final ObservableList<String> colors = FXCollections.observableArrayList();
    private final ObservableList<String> selectedColors = FXCollections.observableArrayList();
    private ArrayList<Product> originalProductList;
    private final ArrayList<Product> filteredProductList = new ArrayList<>();

    @FXML
    private TextField fldProductName;

    @FXML
    private ListView<String> listColors;

    @FXML
    private ListView<String> listSelectedColors;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnRemove;

    @FXML
    private Label lblAddMessage;

    @FXML
    private Label lblRange;

    @FXML
    private Label lblName;

    @FXML
    private Slider sliderPrice;

    @FXML
    private Button btnBuild;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        float minPrice = Integer.MAX_VALUE;
        float maxPrice = Integer.MIN_VALUE;

        Client.catalogController.getProducts();
        originalProductList = (ArrayList<Product>) Client.catalogController.getService().getResponse().getData();

        for(Product product : originalProductList) {
            if(product.getDiscountPrice() > maxPrice)
                maxPrice = product.getDiscountPrice();
            if(product.getDiscountPrice() < minPrice)
                minPrice = product.getDiscountPrice();
        }

        sliderPrice.setMin(minPrice);
        sliderPrice.setMax(maxPrice);
        sliderPrice.setValue(maxPrice);
        lblRange.setText(minPrice + "\u20AA-" + maxPrice + "\u20AA");

        float finalMinPrice = minPrice;
        sliderPrice.valueProperty().addListener((observable, oldValue, newValue) -> {
            lblRange.setText(finalMinPrice + "\u20AA-" + Math.ceil((Double) newValue) + "\u20AA");
        });

        colors.add("Black");
        colors.add("White");
        colors.add("Gray");
        colors.add("Silver");
        colors.add("Maroon");
        colors.add("Red");
        colors.add("Purple");
        colors.add("Pink");
        colors.add("Green");
        colors.add("Lime");
        colors.add("Olive");
        colors.add("Yellow");
        colors.add("Navy");
        colors.add("Blue");
        colors.add("Teal");
        colors.add("Aqua");
        listColors.refresh();

        listColors.setItems(colors);
        listSelectedColors.setItems(selectedColors);
    }

    @FXML
    void clickBtnAdd(ActionEvent event) {
        if(listColors.getSelectionModel().getSelectedItem() == null) {
            lblAddMessage.setVisible(true);
            lblAddMessage.setText("Select a color from the left list");
            return;
        }

        lblAddMessage.setVisible(false);
        String selectedColor = listColors.getSelectionModel().getSelectedItem();
        selectedColors.add(selectedColor);
        colors.remove(selectedColor);
        listSelectedColors.refresh();
        listColors.refresh();
    }

    @FXML
    void clickBtnRemove(ActionEvent event) {
        if(listSelectedColors.getSelectionModel().getSelectedItem() == null) {
            lblAddMessage.setVisible(true);
            lblAddMessage.setText("Select a color from the right list");
            return;
        }

        lblAddMessage.setVisible(false);
        String selectedColor = listSelectedColors.getSelectionModel().getSelectedItem();
        selectedColors.remove(selectedColor);
        colors.add(selectedColor);
        listSelectedColors.refresh();
        listColors.refresh();
    }

    @FXML
    void clickBtnBuild(ActionEvent event) {
        filteredProductList.clear();

        if(fldProductName.getText().isEmpty()) {
            lblName.setVisible(true);
            return;
        }

        for(Product product : originalProductList) {
            if(product.getDiscountPrice() > sliderPrice.getValue())
                continue;
            if(!selectedColors.isEmpty() && !selectedColors.contains(product.getDominantColor()))
                continue;

            filteredProductList.add(product);
        }

        Client.catalogController.createProduct = new Product();
        Client.catalogController.createProduct.setName(fldProductName.getText());
        CustomerProductBuilderModalController.arrivedList = filteredProductList;

        MainDashboardController.setContentFromFXML("CustomerProductBuilderListPage.fxml");
    }

}

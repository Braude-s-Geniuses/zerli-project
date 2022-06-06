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
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import product.Product;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductsManagePageController implements Initializable {

    private final ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> tableProducts;

    @FXML
    private TableColumn<Product, String> columnName;

    @FXML
    private TableColumn<Product, Boolean> columnCustom;

    @FXML
    private TableColumn<Product, String> columnColor;

    @FXML
    private TableColumn<Product, String> columnPrice;

    @FXML
    private TableColumn<Product, Boolean> columnInCatalog;

    @FXML
    private TableColumn<Product, Boolean> columnAction;

    @FXML
    private Button btnAddProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        columnName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        columnCustom.setCellValueFactory(new PropertyValueFactory<Product, Boolean>("customMade"));
        columnColor.setCellValueFactory(new PropertyValueFactory<Product, String>("dominantColor"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<Product, String>("price"));
        columnInCatalog.setCellValueFactory(new PropertyValueFactory<Product, Boolean>("inCatalog"));
        columnAction.setSortable(false);

        Client.productController.getProducts();
        for (Product product : (ArrayList<Product>) Client.productController.getService().getResponse().getData())
            products.add(product);

        tableProducts.setItems(products);

        // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        columnAction.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Product, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Product, Boolean> param) {
                return new SimpleBooleanProperty(param.getValue() != null);
            }
        });

        columnAction.setCellFactory(new Callback<TableColumn<Product, Boolean>, TableCell<Product, Boolean>>() {
            @Override
            public TableCell<Product, Boolean> call(TableColumn<Product, Boolean> param) {
                return new ProductsManagePageController.AddProductCell(tableProducts);
            }
        });

    }

    @FXML
    void clickBtnAddProduct(ActionEvent event) {
        MainDashboardController.setContentFromFXML("ProductAddPage.fxml");
    }

    private class AddProductCell extends TableCell<Product, Boolean> {
        final Button modifyButton = new Button("Modify");
        final StackPane paddedButton = new StackPane();
        final DoubleProperty buttonY = new SimpleDoubleProperty();

        AddProductCell(final TableView table) {
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
                    ProductModifyPageController.setCurrentProduct((Product) table.getItems().get(getTableRow().getIndex()));
                    MainDashboardController.setContentFromFXML("ProductModifyPage.fxml");
                }
            });
        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(paddedButton);
            } else {
                setGraphic(null);
            }
        }
    }
}

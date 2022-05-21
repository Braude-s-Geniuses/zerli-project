package clientgui;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import order.Order;
import order.OrderProduct;

import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderDetailsPageController implements Initializable {
    public static Order order;
    @FXML
    private ListView<Object> productsAsListView;

    @FXML
    private Label detailsLabel;

    @FXML
    private Button btnBack;

    @FXML
    void clickBtnBack(ActionEvent event) {
        MainDashboardController.setContentFromFXML("MyOrdersPage.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<OrderProduct> orderProducts;
        Client.orderController.getOrderProducts(order.getOrderId());
        orderProducts = (ArrayList<OrderProduct>) Client.orderController.getResponse().getData();
        detailsLabel.setStyle("-fx-border-color: #77385a;");
        detailsLabel.setText("Order Number:   #" + order.getOrderId() + "\nStatus:    " + order.getOrderStatus() + "\nTotal Paid Price:    " + order.discountPriceToString());
        detailsLabel.setAlignment(Pos.BASELINE_CENTER);
        for (OrderProduct op : orderProducts) {
            ImageView iv = new ImageView();
            iv.setFitHeight(150.0);
            iv.setFitWidth(130.0);
            Client.productController.createProductImage(op.getProduct());
            iv.setImage(Client.productController.getProductImages().get(op.getProduct().getProductId()));
            Label lblImage = new Label(null, iv);
            Label lblName = new Label(op.getProduct().getName());
            lblName.getStyleClass().add("details-label");
            Label lblPrice = new Label(op.getProduct().discountPriceToString());
            lblPrice.getStyleClass().add("details-label");
            Label lblCustom = new Label(op.getProduct().customMadeToString());
            lblCustom.getStyleClass().add("details-label");
            Label lblQuantity = new Label("Amount:  " + op.getQuantity());
            lblQuantity.getStyleClass().add("details-label");
            setLabels(lblName);
            setLabels(lblPrice);
            setLabels(lblCustom);
            setLabels(lblQuantity);

            HBox h = new HBox(25, lblImage, lblName, lblPrice, lblCustom, lblQuantity);

            productsAsListView.getItems().add(h);
        }
    }
    private void setLabels (Label lbl){
        lbl.setWrapText(true);
        lbl.setPrefHeight(150);
        lbl.setAlignment(Pos.CENTER_LEFT);
    }
}


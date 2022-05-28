package clientgui;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import order.Order;
import order.OrderProduct;
import order.OrderStatus;
import user.UserType;

import java.net.URL;
import java.sql.Timestamp;
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
    private Button btnCancel;

    @FXML
    private Button btnConfirm;

    /**
     * Returns to view orders page
     * @param event
     */
    @FXML
    void clickBtnBack(ActionEvent event) {
        if(Client.userController.getLoggedInUser().getUserType().equals(UserType.CUSTOMER)) {
            MainDashboardController.setContentFromFXML("MyOrdersPage.fxml");
        } else {
            MainDashboardController.setContentFromFXML("OrderManagerPage.fxml");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<OrderProduct> orderProducts;
        Client.orderController.getOrderProducts(order.getOrderId());
        orderProducts = (ArrayList<OrderProduct>) Client.orderController.getResponse().getData();
        detailsLabel.setStyle("-fx-border-color: #77385a;");
        detailsLabel.setText("Order Number:   #" + order.getOrderId() + "\n" + order.getOrderStatus().toString() + "\nTotal Paid Price:    " + order.discountPriceToString());
        detailsLabel.setAlignment(Pos.BASELINE_CENTER);

        switch(Client.userController.getLoggedInUser().getUserType()){
            case CUSTOMER:
                if(order.getOrderStatus() == OrderStatus.NORMAL_CONFIRMED || order.getOrderStatus() == OrderStatus.EXPRESS_CONFIRMED)
                    btnCancel.setVisible(true);
                btnConfirm.setVisible(false);
                break;
            case BRANCH_MANAGER:
                if(order.getOrderStatus() == OrderStatus.NORMAL_PENDING || order.getOrderStatus() == OrderStatus.EXPRESS_PENDING) {
                    btnConfirm.setText("Confirm Order");
                    btnConfirm.setVisible(true);
                }
                else if(order.getOrderStatus() == OrderStatus.CANCEL_PENDING) {
                    btnConfirm.setText("Confirm Cancel");
                    btnConfirm.setVisible(true);
                }
                break;
            default:
                break;
        }

        if(!orderProducts.isEmpty()) {
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
    }

    @FXML
    void clickBtnCancel(ActionEvent event) {
        Timestamp deliveryTime = order.getDeliveryDate();
        Timestamp currTime = new Timestamp(System.currentTimeMillis());
        long timeLeft = deliveryTime.getTime() - currTime.getTime();
        int hours = (int) timeLeft / 3600000;
        float refund = 0;
        if(Client.userController.getLoggedInUser().getUserType().equals(UserType.CUSTOMER)) {
            order.setOrderStatus("CANCEL_PENDING");
            order.setCancelTime(currTime);
            Client.orderController.setCancelTime(order);
            Client.orderController.setStatusOrder(order);
        }
        else{
            if(hours > 3){
                refund = order.getPrice()+Client.orderController.getBalance(order.getCustomerId());
            }
            if(hours < 3 && hours >= 1){
                refund = order.getPrice()* (float) 0.5;
                refund+= Client.orderController.getBalance(order.getCustomerId());
            }
            if(refund != 0){
                Client.orderController.updateBalance(order.getCustomerId(),refund);
            }
        }

        return;
    }

    @FXML
    void clickBtnConfirm(ActionEvent event) {
        if(order.getOrderStatus() == OrderStatus.NORMAL_PENDING) {
            order.setOrderStatus("NORMAL_CONFIRMED");
        } else if (order.getOrderStatus() == OrderStatus.EXPRESS_PENDING) {
            order.setOrderStatus("EXPRESS_CONFIRMED");
        } else {
            order.setOrderStatus("CANCEL_CONFIRMED");
            clickBtnCancel(event);
        }

        Client.orderController.setStatusOrder(order);
    }

    private void setLabels (Label lbl){
        lbl.setWrapText(true);
        lbl.setPrefHeight(150);
        lbl.setAlignment(Pos.CENTER_LEFT);
    }
}

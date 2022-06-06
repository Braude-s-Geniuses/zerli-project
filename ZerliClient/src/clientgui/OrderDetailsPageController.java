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
import javafx.util.Duration;
import order.Order;
import order.OrderProduct;
import order.OrderStatus;
import user.UserType;
import util.Alert;

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
        if(Client.userController.getLoggedInUser().getUserType().equals(UserType.CUSTOMER))
            MainDashboardController.setContentFromFXML("MyOrdersPage.fxml");
        else
            MainDashboardController.setContentFromFXML("OrderManagerPage.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<OrderProduct> orderProducts;
        Client.orderController.getOrderProducts(order.getOrderId());
        orderProducts = (ArrayList<OrderProduct>) Client.orderController.getService().getResponse().getData();
        detailsLabel.setStyle("-fx-border-color: #77385a;");
        detailsLabel.setText("Order Number:   #" + order.getOrderId() + "\n" + order.getOrderStatus().toString() + "\nTotal Paid Price:    " + order.discountPriceToString());
        detailsLabel.setAlignment(Pos.BASELINE_CENTER);

        switch(Client.userController.getLoggedInUser().getUserType()){
            case CUSTOMER:
                if(order.getOrderStatus() == OrderStatus.NORMAL_CONFIRMED || order.getOrderStatus() == OrderStatus.EXPRESS_CONFIRMED || order.getOrderStatus() == OrderStatus.NORMAL_PENDING || order.getOrderStatus() == OrderStatus.EXPRESS_PENDING)
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
                lblName.setPrefWidth(150);
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
        float refund = 0;
        if(Client.userController.getLoggedInUser().getUserType().equals(UserType.CUSTOMER)) {
            order.setOrderStatus("CANCEL_PENDING");
            order.setCancelTime(currTime);
            Client.orderController.setCancelTime(order);
            Client.orderController.setStatusOrder(order);
            MainDashboardController.createAlert("Order "+ order.getOrderId() + " is awaiting cancellation confirmation", Alert.SUCCESS, Duration.seconds(3), 135, 100);
        }
        else{
            long timeLeft =  deliveryTime.getTime() - order.getCancelTime().getTime();
            int hours = (int) timeLeft / 3600000;
            if(hours > 3){
                refund = order.getDiscountPrice();
            }
            if(hours < 3 && hours >= 1){
                refund = order.getDiscountPrice()* (float) 0.5;
            }
            if(refund != 0){
                Client.orderController.updateBalance(order.getCustomerId(),Client.orderController.getBalance(order.getCustomerId()) + refund);
            }

            /* Notify customer */
            Client.userController.getCustomerEmail(order.getCustomerId());
            String mail = (String) Client.userController.getService().getResponse().getData();
            Client.clientController.sendMail("[SMS/EMAIL SIMULATION] To: " + mail + " | Message: Your Order #" + order.getOrderId() + " has been cancelled and " + refund + " ILS has been credited to your account");
        }

        return;
    }

    @FXML
    void clickBtnConfirm(ActionEvent event) {
        if(order.getOrderStatus() == OrderStatus.NORMAL_PENDING) {
            order.setOrderStatus("NORMAL_CONFIRMED");
            MainDashboardController.createAlert("Order "+ order.getOrderId() + " was confirmed", Alert.SUCCESS, Duration.seconds(3), 135, 100);

            /* Notify customer */
            Client.userController.getCustomerEmail(order.getCustomerId());
            String mail = (String) Client.userController.getService().getResponse().getData();
            Client.clientController.sendMail("[SMS/EMAIL SIMULATION] To: " + mail + " | Message: Your Order #" + order.getOrderId() + " has been confirmed. Thank you for your purchase from us!");
        } else if (order.getOrderStatus() == OrderStatus.EXPRESS_PENDING) {
            order.setOrderStatus("EXPRESS_CONFIRMED");
            MainDashboardController.createAlert("Express order "+ order.getOrderId() + " was confirmed", Alert.SUCCESS, Duration.seconds(3), 135, 100);
            /* Notify customer */
            Client.userController.getCustomerEmail(order.getCustomerId());
            String mail = (String) Client.userController.getService().getResponse().getData();
            Client.clientController.sendMail("[SMS/EMAIL SIMULATION] To: " + mail + " | Message: Your Order #" + order.getOrderId() + " has been confirmed. Thank you for your purchase from us!");
        } else {
            order.setOrderStatus("CANCEL_CONFIRMED");
            clickBtnCancel(event);
            MainDashboardController.createAlert("Order "+ order.getOrderId() + " was canceled successfully", Alert.SUCCESS, Duration.seconds(3), 135, 100);
        }

        Client.orderController.setStatusOrder(order);
    }

    private void setLabels (Label lbl){
        lbl.setWrapText(true);
        lbl.setPrefHeight(150);
        lbl.setAlignment(Pos.CENTER_LEFT);
    }
}

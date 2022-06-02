package clientgui;

import client.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import order.Order;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderManagerPageController implements Initializable {
    private String branch=null;
    @FXML
    private AnchorPane baseAnchor;

    @FXML
    private Button btsShowOrderDetails;

    @FXML
    private ListView<Object> orderList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Client.orderController.requestOrders();
        ArrayList<Order> result = (ArrayList<Order>) Client.orderController.getResponse().getData();
        Client.orderController.getBranch(Client.userController.getLoggedInUser().getUserId());
        if (result != null) {
            ObservableList<Order> orders = FXCollections.observableArrayList(result);
            for (Order order : result) {
                if(order.getBranch().equals("Karmiel")) {
                    switch (order.getOrderStatus()) {
                        case CANCEL_PENDING:
                        case NORMAL_PENDING:
                        case EXPRESS_PENDING:
                            Button button = new Button("View Details");
                            button.setPrefWidth(160);
                            button.setAlignment(Pos.CENTER);
                            button.getStyleClass().add("btn");

                            setActionForButton(button, order);
                            Label lblOrderNumber = new Label("#" + order.getOrderId(), null);
                            Label lblOrderDetails = new Label("Ordered On:  " + order.orderDateToString() + "\nFrom Branch:  " + order.getBranch(), null);
                            Label lblOrderPrices = new Label("Total Paid:\n" + order.discountPriceToString(), null);
                            // Label lblOrderRefund = new Label("Refund Amount:\n" + order.discountPriceToString(), null); //need to calc
                            Label lblOrderStatus = new Label(order.getOrderStatus().toString(), null);
                            lblOrderStatus.setTextFill(Color.web("#e0921d"));
                            lblOrderDetails.setPrefWidth(170);
                            setLabels(lblOrderDetails);
                            lblOrderPrices.setPrefWidth(170);
                            setLabels(lblOrderStatus);
                            lblOrderStatus.setPrefWidth(200);
                            setLabels(lblOrderNumber);
                            lblOrderNumber.setPrefWidth(40);
                            setLabels(lblOrderPrices);
                            HBox h = new HBox(30, lblOrderNumber, lblOrderDetails, lblOrderPrices, lblOrderStatus, button);
                            h.setPrefWidth(940);
                            h.setAlignment(Pos.CENTER);
                            orderList.getItems().addAll(h);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    private void setLabels(Label lbl){
        lbl.setFont(Font.font ("Calibri", 16));
        lbl.setWrapText(true);
        lbl.setAlignment(Pos.BASELINE_LEFT);
    }

    private void setActionForButton(Button button, Order order) {
        button.setOnAction(e->{
            OrderDetailsPageController.order = order;
            MainDashboardController.setContentFromFXML("OrderDetailsPage.fxml");
        });
    }


}
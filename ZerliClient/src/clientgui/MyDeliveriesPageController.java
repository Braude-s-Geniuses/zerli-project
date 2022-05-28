package clientgui;

import client.Client;
import client.DeliveryController;
import communication.MessageFromServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import order.Order;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyDeliveriesPageController implements Initializable {

    @FXML
    private ListView<Object> deliveryList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Client.deliveryController.requestDeliveries();

        ArrayList<Order> result = (ArrayList<Order>) Client.deliveryController.getResponse().getData();
        if (result != null) {
            ObservableList<Order> orders = FXCollections.observableArrayList(result);
            for (Order order : result) {
                Button button = new Button("Confirm Delivered");
                button.setPrefWidth(160);
                button.setAlignment(Pos.CENTER);
                button.getStyleClass().add("btn");

                setActionForButton(button, order);
                Label lblOrderNumber = new Label("#" + order.getOrderId(), null);
                Label lblOrderDetails = new Label("Ordered On:  " + order.orderDateToString() +"\nFrom Branch:  " + order.getBranch(), null);
                Label lblOrderAddress = new Label( "Delivery Address:\n" + order.getDeliveryAddress() ,null);
                Label lblOrderRecipient = new Label( "Recipient:\n" + order.getRecipientName() + "\n" + order.getRecipientPhone(),null);
                Label lblOrderStatus = new Label( order.getOrderStatus().toString(), null);

                switch (order.getOrderStatus()){
                    case EXPRESS_CONFIRMED:
                        lblOrderStatus.setTextFill(Color.web("#bf2d39"));
                        break;
                    case NORMAL_CONFIRMED:
                        lblOrderStatus.setTextFill(Color.web("#77385a"));
                        break;
                    default:
                        break;
                }
                lblOrderDetails.setPrefWidth(170);
                setLabels(lblOrderDetails);
                lblOrderAddress.setPrefWidth(150);
                setLabels(lblOrderStatus);
                lblOrderStatus.setPrefWidth(100);
                setLabels(lblOrderNumber);
                lblOrderNumber.setPrefWidth(40);
                setLabels(lblOrderAddress);
                lblOrderRecipient.setPrefWidth(120);
                setLabels(lblOrderRecipient);

                HBox h = new HBox(30,lblOrderNumber ,lblOrderDetails,lblOrderAddress,lblOrderRecipient ,lblOrderStatus, button);
                h.setPrefWidth(940);
                h.setAlignment(Pos.CENTER);
                deliveryList.getItems().addAll(h);

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
            DeliveryController.order = order;
            Client.deliveryController.makeDelivery();//
            MessageFromServer result = Client.deliveryController.getResponse().getAnswer();
            if(result == MessageFromServer.DELIVERY_UPDATE_SUCCESS){
                button.setDisable(true);
                button.setText("Send");
            }
        });
    }

}
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
import order.OrderStatus;

import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyDeliveriesPageController implements Initializable {

    @FXML
    private ListView<Object> deliveryList;

    /**
     * This function initializes the page and shows
     * all the orders that are ready to deliver is a list
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Client.deliveryController.requestDeliveries();

        ArrayList<Order> result = (ArrayList<Order>) Client.deliveryController.getService().getResponse().getData();
        if (result.size() != 0) {
            ObservableList<Order> orders = FXCollections.observableArrayList(result);
            for (Order order : result) {
                String orderTime = new SimpleDateFormat("hh:mm").format(order.getOrderDate());
                Button button = new Button("Accept delivery");
                button.setPrefWidth(160);
                button.setAlignment(Pos.CENTER);
                button.getStyleClass().add("btn");

                setActionForButton(button, order);
                Label lblOrderNumber = new Label("#" + order.getOrderId(), null);
                Label lblOrderDetails = new Label("Ordered On:  " + order.orderDateToString() + "\nAt time:  " + orderTime + "\nFrom Branch:  " + order.getBranch(), null);
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
        }else {
            Label lblNoOrders = new Label("No upcoming orders", null);
            lblNoOrders.setFont(Font.font ("Calibri", 32));
            lblNoOrders.setTextFill(Color.web("#77385a"));
            lblNoOrders.setPrefWidth(400);
            HBox h = new HBox(30,lblNoOrders);
            h.setPrefWidth(940);
            h.setAlignment(Pos.CENTER);
            deliveryList.getItems().addAll(h);
        }
    }

    /**
     * This function sets the font for each label.
     * @param lbl Import label
     */
    private void setLabels(Label lbl){
        lbl.setFont(Font.font ("Calibri", 16));
        lbl.setWrapText(true);
        lbl.setAlignment(Pos.BASELINE_LEFT);

    }

    /**
     * This function sets the order as delivered and refund the customer if needed.
     * @param button button that was pushed in gui.
     * @param order order to deliver.
     */
    private void setActionForButton(Button button, Order order) {
        button.setOnAction(e->{
            Client.deliveryController.setOrder(order);
            OrderStatus orderStatus= order.getOrderStatus();
            Client.deliveryController.makeDelivery();
            MessageFromServer result = Client.deliveryController.getService().getResponse().getAnswer();
            if(result == MessageFromServer.DELIVERY_UPDATE_SUCCESS){
                button.setDisable(true);
                button.setText("Completed");
                if(checkForRefund(Client.deliveryController.getOrder(), orderStatus)){
                    Client.deliveryController.makeRefund();
                }
            }
        });
    }

    /**
     * This function compares the delivery-time to the order-time and refunds
     * the customer if delivery time is more than three hours
     * @param order Order under check.
     * @param orderStatus of order.
     * @return
     */
    private boolean checkForRefund(Order order, OrderStatus orderStatus) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp orderTime = order.getDeliveryDate();

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        if(fmt.format(currentTime).equals(fmt.format(orderTime))){          //if same day
            float timeDiff = currentTime.getTime() - orderTime.getTime() ;
            float timeDiffHours = timeDiff / (60 * 60 * 1000);
            if (timeDiffHours >= 3 && orderStatus == OrderStatus.EXPRESS_CONFIRMED){
                return true;    //Refund if express order was more than three hours late.
            }

            return timeDiffHours >= 0.5 && orderStatus == OrderStatus.NORMAL_CONFIRMED; //Refund if normal order was more than one hour late.
        } else
            return true;        //if a day or more has passed.
    }

}
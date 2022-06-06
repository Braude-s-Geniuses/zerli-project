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

public class MyOrdersPageController implements Initializable {

    @FXML
    private AnchorPane baseAnchor;

    @FXML
    private Button btsShowOrderDetails;

    @FXML
    private ListView<Object> orderList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Client.orderController.requestOrders();
        orderList.setFocusTraversable(false);
        ArrayList<Order> result = (ArrayList<Order>) Client.orderController.getService().getResponse().getData();
        if (result != null) {
            ObservableList<Order> orders = FXCollections.observableArrayList(result);
            for (Order order : result) {
                Button button = new Button("View Details");
                button.setPrefWidth(160);
                button.setAlignment(Pos.CENTER);
                button.getStyleClass().add("btn");

                setActionForButton(button, order);
                Label lblOrderNumber = new Label("#" + order.getOrderId(), null);
                Label lblOrderDetails = new Label("Ordered On:  " + order.orderDateToString() +"\nFrom Branch:  " + order.getBranch(), null);
                Label lblOrderPrices = new Label( "Total Paid:\n" + order.discountPriceToString() ,null);
                Label lblOrderStatus = new Label(order.getOrderStatus().toString(), null);

                switch (order.getOrderStatus()){
                    case CANCEL_PENDING:
                    case CANCEL_CONFIRMED:
                        lblOrderStatus.setTextFill(Color.web("#bf2d39"));
                        break;
                    case EXPRESS_CONFIRMED:
                    case NORMAL_CONFIRMED:
                        lblOrderStatus.setTextFill(Color.web("#da1782"));
                        break;
                    case NORMAL_PENDING:
                    case EXPRESS_PENDING:
                        lblOrderStatus.setTextFill(Color.web("#e0921d"));
                        break;
                    case NORMAL_COMPLETED:
                    case EXPRESS_COMPLETED:
                        lblOrderStatus.setTextFill(Color.web("#77385a"));
                        break;
                    default:
                        break;
                }
                lblOrderDetails.setPrefWidth(170);
                setLabels(lblOrderDetails);
                lblOrderPrices.setPrefWidth(170);
                setLabels(lblOrderStatus);
                lblOrderStatus.setPrefWidth(200);
                setLabels(lblOrderNumber);
                lblOrderNumber.setPrefWidth(40);
                setLabels(lblOrderPrices);
                lblOrderPrices.setAlignment(Pos.CENTER);

                HBox h = new HBox(30,lblOrderNumber ,lblOrderDetails,lblOrderPrices, lblOrderStatus, button);
                h.setPrefWidth(940);
                h.setAlignment(Pos.CENTER);
                orderList.getItems().addAll(h);

            }
        }
    }
    private void setLabels(Label lbl){
        lbl.setFont(Font.font ("Calibri", 16));
        lbl.setWrapText(true);
        lbl.setAlignment(Pos.BASELINE_LEFT);

    }

    /**
     * initialize the handler of each button in orders list view
     * @param button- each order has its own button
     * @param order
     */
    private void setActionForButton(Button button, Order order) {
        button.setOnAction(e->{
            OrderDetailsPageController.order = order;
            MainDashboardController.setContentFromFXML("OrderDetailsPage.fxml");
        });
    }
}


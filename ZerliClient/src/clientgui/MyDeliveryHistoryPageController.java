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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import order.Order;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MyDeliveryHistoryPageController implements Initializable {

    @FXML
    private ListView<Object> deliveryHistoryList;

    /**
     * This function initialized the page and presents all the deliveries that have been delivered.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Client.deliveryController.requestDeliveriesHistory();

        ArrayList<Order> result = (ArrayList<Order>) Client.deliveryController.getService().getResponse().getData();
        if (result != null) {
            ObservableList<Order> orders = FXCollections.observableArrayList(result);
            for (Order order : result) {
                Button button = new Button("Delivery History");
                button.setPrefWidth(160);
                button.setAlignment(Pos.CENTER);
                button.getStyleClass().add("btn");

                setActionForButton(button, order);
                Label lblOrderNumber = new Label("#" + order.getOrderId(), null);
                Label lblOrderDetails = new Label("Ordered On:  " + order.orderDateToString() +"\nFrom Branch:  " + order.getBranch(), null);
                Label lblOrderPrices = new Label( "Total Paid:\n" + order.discountPriceToString() ,null);
                Label lblOrderStatus = new Label( order.getOrderStatus().toString(), null);

                switch (order.getOrderStatus()){
                    case EXPRESS_COMPLETED:
                    case NORMAL_COMPLETED:
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

                HBox h = new HBox(30,lblOrderNumber ,lblOrderDetails,lblOrderPrices, lblOrderStatus, button);
                h.setPrefWidth(940);
                h.setAlignment(Pos.CENTER);
                deliveryHistoryList.getItems().addAll(h);

            }
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
     * This function initialize a new page with all the delivery details of a specific order.
     * @param button button that was pushed in gui.
     * @param order order to deliver.
     */
    private void setActionForButton(Button button, Order order) {
        button.setOnAction(e->{
            DeliveryDetailsPageController.order = order;
            MainDashboardController.setContentFromFXML("DeliveryDetailsPage.fxml");
        });
    }

}
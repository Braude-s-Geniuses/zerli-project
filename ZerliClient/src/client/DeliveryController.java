package client;

import communication.Message;
import communication.MessageFromClient;
import order.Order;

public class DeliveryController {

    public static Order order;

    private  Message response;
    private Order currentOrder;

    public void requestDeliveries() {
        Message deliveryRequest = new Message(null, MessageFromClient.REQUEST_DELIVERIES_TABLE);
        Client.clientController.getClient().handleMessageFromUI(deliveryRequest, true);
    }

    public void makeDelivery() {
        Message deliveryToSend = new Message(order, MessageFromClient.SEND_DELIVERY);
        Client.clientController.getClient().handleMessageFromUI(deliveryToSend, true);
    }

    public void requestDeliveriesHistory() {
        Message deliveryToSend = new Message(null, MessageFromClient.REQUEST_DELIVERIES_HISTORY_TABLE);
        Client.clientController.getClient().handleMessageFromUI(deliveryToSend, true);
    }

    public void setResponse(Message response) {this.response = response;}

    public Message getResponse() {return response;}


}
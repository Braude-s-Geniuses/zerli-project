package client;

import communication.Message;
import communication.MessageFromClient;
import order.Order;

public class DeliveryController {

    public static Order order;

    private  Message response;
    private Order currentOrder;

    /**
     * This function request from the server orders that are ready to delivered.
     */
    public void requestDeliveries() {
        Message deliveryRequest = new Message(null, MessageFromClient.REQUEST_DELIVERIES_TABLE);
        Client.clientController.getClient().handleMessageFromUI(deliveryRequest, true);
    }

    /**
     * This function request from the server to change the order status to deliver.
     */
    public void makeDelivery() {
        Message deliveryToSend = new Message(order, MessageFromClient.SEND_DELIVERY);
        Client.clientController.getClient().handleMessageFromUI(deliveryToSend, true);
    }

    /**
     * This function request from the server all the orders that were delivered
     */
    public void requestDeliveriesHistory() {
        Message deliveryToSend = new Message(null, MessageFromClient.REQUEST_DELIVERIES_HISTORY_TABLE);
        Client.clientController.getClient().handleMessageFromUI(deliveryToSend, true);
    }


    public void makeRefund() {
        Message OrderToRefund = new Message(order, MessageFromClient.REFUND_ORDER);
        Client.clientController.getClient().handleMessageFromUI(OrderToRefund, true);
    }

    /**
     * Sets the response message from the server.
     * @param response
     */
    public void setResponse(Message response) {this.response = response;}

    /**
     * Returns the response message from the server.
     * @return message from server.
     */
    public Message getResponse() {return response;}


}
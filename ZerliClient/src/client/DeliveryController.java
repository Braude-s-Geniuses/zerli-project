package client;

import communication.Message;
import communication.MessageFromClient;
import order.Order;

public class DeliveryController extends AbstractController {

    private Order order;

    /**
     * This function request from the server orders that are ready to delivered.
     */
    public void requestDeliveries() {
        Message deliveryRequest = new Message(null, MessageFromClient.DELIVERIES_GET);
        Client.clientController.getClient().handleMessageFromUI(deliveryRequest, true);
    }

    /**
     * This function request from the server to change the order status to deliver.
     */
    public void makeDelivery() {
        Message deliveryToSend = new Message(order, MessageFromClient.DELIVERY_ADD_NEW);
        Client.clientController.getClient().handleMessageFromUI(deliveryToSend, true);
    }

    /**
     * This function request from the server all the orders that were delivered
     */
    public void requestDeliveriesHistory() {
        Message deliveryToSend = new Message(null, MessageFromClient.DELIVERY_HISTORY_GET);
        Client.clientController.getClient().handleMessageFromUI(deliveryToSend, true);
    }


    public void makeRefund() {
        Message OrderToRefund = new Message(order, MessageFromClient.DELIVERY_ORDER_REFUND);
        Client.clientController.getClient().handleMessageFromUI(OrderToRefund, true);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
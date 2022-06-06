package client;

import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import order.Order;

/**
 * Controller for everything related to Delivery Operator
 */
public class DeliveryController extends AbstractController {

    private Order order;

    DeliveryController(IMessageService service) {
        super(service);
    }

    /**
     * This function request from the server orders that are ready to delivered.
     */
    public void requestDeliveries() {
        Message deliveryRequest = new Message(null, MessageFromClient.DELIVERIES_GET);
        getService().sendToServer(deliveryRequest, true);
    }

    /**
     * This function request from the server to change the order status to deliver.
     */
    public void makeDelivery() {
        Message deliveryToSend = new Message(order, MessageFromClient.DELIVERY_ADD_NEW);
        getService().sendToServer(deliveryToSend, true);
    }

    /**
     * This function request from the server all the orders that were delivered
     */
    public void requestDeliveriesHistory() {
        Message deliveryToSend = new Message(null, MessageFromClient.DELIVERY_HISTORY_GET);
        getService().sendToServer(deliveryToSend, true);
    }

    /**
     * This function refunds the full paid amount by the customer to his balance
     * and also emails him with an apology
     */
    public void makeRefund() {
        Message OrderToRefund = new Message(order, MessageFromClient.DELIVERY_ORDER_REFUND);
        getService().sendToServer(OrderToRefund, true);

        if(getService().getResponse().getAnswer() == MessageFromServer.DELIVERY_ORDER_REFUND_SUCCESS) {
            Client.userController.getCustomerEmail(order.getCustomerId());
            String email = (String) Client.userController.getService().getResponse().getData();
            Client.clientController.sendMail("[SMS/EMAIL SIMULATION] To: " + email + " | Message: Your Order #" + order.getOrderId() + " has been delivered. We apologize for the delay and we have fully refunded your payment to your account.");
        }
    }

    /**
     * Getter for <code>order</code>
     * @return <code>order</code> the delivery operator currently working on
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Setter for <code>order</code>
     * @param order - sets the order the delivery operator currently working on
     */
    public void setOrder(Order order) {
        this.order = order;
    }

}
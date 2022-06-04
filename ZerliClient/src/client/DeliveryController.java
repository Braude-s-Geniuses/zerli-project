package client;

import com.sun.org.apache.xpath.internal.operations.Or;
import communication.Message;
import communication.MessageFromClient;
import order.Order;

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


    public void makeRefund() {
        Message OrderToRefund = new Message(order, MessageFromClient.DELIVERY_ORDER_REFUND);
        getService().sendToServer(OrderToRefund, true);

        Client.userController.getCustomerEmail(order.getCustomerId());
        String email = (String) Client.userController.getService().getResponse().getData();
        Client.clientController.sendMail("[SMS/EMAIL SIMULATION] To: " + email + " | Message: Your Order #" + order.getOrderId() + " has been delivered. We apologize for the delay and we have fully refunded your payment to your account.");
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

}
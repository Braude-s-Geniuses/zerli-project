package client;

import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import order.Order;
import order.OrderProduct;

import java.util.ArrayList;

public class OrderController {

    public final float DELIVERY_PRICE = 20;
    /**
     * Used to store Products inserted into the cart.
     */
    private ArrayList<OrderProduct> cart = new ArrayList<OrderProduct>();
    private Message response;
    private Order currentOrder;
    public OrderController() {}

    public void addToCart(OrderProduct orderProduct){
        // if added order already exists in cart - adds quantity to existing OrderProduct
        for(OrderProduct op : cart) {
            if(orderProduct.getProduct().equals(op.getProduct())) {
                op.setQuantity(op.getQuantity() + orderProduct.getQuantity());
                return;
            }
        }

        // orderProduct doesn't exist - create it
        cart.add(orderProduct);
    }

    public ArrayList<OrderProduct> getCart() {
        return cart;
    }

    public void setCurrentOrder(Order currentOrder) { this.currentOrder = currentOrder; }

    public Order getCurrentOrder() { return currentOrder; }

    public Message getResponse() {return response;}

    public void setResponse(Message response) {this.response = response; }

    /**
     * Changing the amount of specific product in cart
     *
     * @param productName
     * @return
     */
    public OrderProduct getProductByName(String productName){
        for (OrderProduct op : cart){
            if (op.getProduct().getName().equals(productName)){
                return op;
            }
        }
        return null;
    }

    /**
     * Calculates the price or discount price of the current order
     * @param discount - calculates discounted price or not
     * @return
     */
    public float getOrderPrice(boolean discount){
        float total = 0;
        for (OrderProduct op : cart){
            if(discount)
                total += op.getProduct().getDiscountPrice() * op.getQuantity();
            else
                total += op.getProduct().getPrice() * op.getQuantity();
        }
        return total;
    }

    /**
     * Calculates the price of all the cart includes discounts
     * @return
     */
    public float sumOfCart(){
        float total = 0;
        for (OrderProduct op : cart){
            total += op.getProduct().getDiscountPrice() * op.getQuantity();
        }
        return total;
    }

    /**
     * Fetches all customer`s orders from DB
     */
    public void requestOrders(){
        //   Message ordersRequest = new Message(Client.clientController.getClient().getLoggedInUser().getUserId(), MessageFromClient.REQUEST_ORDERS_TABLE);
        Message ordersRequest = new Message(1, MessageFromClient.REQUEST_ORDERS_TABLE);
        Client.clientController.getClient().handleMessageFromUI(ordersRequest, true);
    }

    /**
     * Get branches list from DB
     */
    public void getBranches() {
        Message msg = new Message(null, MessageFromClient.REQUEST_BRANCHES);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }
    /**
     * Saves completed order in DB
     * @return
     */
    public boolean sendNewOrder() {
        Message msg = new Message(getCurrentOrder(), MessageFromClient.ADD_NEW_ORDER);
        Client.clientController.getClient().handleMessageFromUI(msg, true);
        if(response.getAnswer() == MessageFromServer.ADDED_ORDER_NOT_SUCCESSFULLY){
           return false;
        }
        return true;
    }

    /**
     * Get Order Products
     * @param orderId
     */
    public void getOrderProducts(int orderId) {
        Message msg = new Message(orderId, MessageFromClient.REQUEST_ORDER_PRODUCTS);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }

    /**
     * Updates the balance of customer after he used it
     * @param userId - the id of the current customer
     * @param balance - new balance
     */
    public void updateBalance(int userId, float balance) {
        ArrayList<Object> msgList = new ArrayList<>();
        msgList.add(userId);
        msgList.add(balance);
        Message msg = new Message(msgList, MessageFromClient.UPDATE_BALANCE_FOR_CUSTOMER);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }

    /**
     * Update customer credit card in DB
     * @param userId - current customer id
     * @param cardDetails -new credit card details
     */
    public void updateCreditCard(int userId, ArrayList<String> cardDetails) {
        ArrayList<Object> msgList = new ArrayList<>();
        msgList.add(userId);
        msgList.add(cardDetails);
        Message msg = new Message(msgList, MessageFromClient.UPDATE_CARD_FOR_CUSTOMER);
        Client.clientController.getClient().handleMessageFromUI(msg,false);

    }

    /**
     * After customer used his first order with 20% discount he is no longer a new customer
     * Updates in DB
     * @param userId the id of the current customer
     */
    public void updateNemCustomer(int userId) {
        Message msg = new Message(userId, MessageFromClient.UPDATE_NEW_CUSTOMER);
        Client.clientController.getClient().handleMessageFromUI(msg,false);
    }
}

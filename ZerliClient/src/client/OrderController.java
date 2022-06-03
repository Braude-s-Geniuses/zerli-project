package client;

import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import order.Order;
import order.OrderProduct;

import java.util.ArrayList;

public class OrderController extends AbstractController {

    public final float DELIVERY_PRICE = 20;

    /**
     * Used to store Products inserted into the cart.
     */
    private final ArrayList<OrderProduct> cart = new ArrayList<OrderProduct>();

    private float currBalance = -1;

    private String currBranch = null;

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
     * Fetches all customer`s orders from DB
     */
    public void requestOrders(){
        Message ordersRequest = new Message(Client.userController.getLoggedInUser().getUserId(), MessageFromClient.ORDERS_GET);
        Client.clientController.getClient().handleMessageFromUI(ordersRequest, true);
    }

    /**
     * Fetches all customer`s orders from DB by branch
     */
    public void requestOrders(String branch){
        Message ordersRequest = new Message(branch, MessageFromClient.ORDERS_GET_BY_BRANCH);
        Client.clientController.getClient().handleMessageFromUI(ordersRequest, true);
    }

    /**
     * Get branches list from DB
     */
    public void getBranches() {
        Message msg = new Message(null, MessageFromClient.ORDER_BRANCHES_GET);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }
    /**
     * Saves completed order in DB
     * @return
     */
    public boolean sendNewOrder() {
        Message msg = new Message(getCurrentOrder(), MessageFromClient.ORDER_CREATE_NEW);
        Client.clientController.getClient().handleMessageFromUI(msg, true);
        return getResponse().getAnswer() != MessageFromServer.ORDER_CREATE_NEW_FAIL;
    }

    /**
     * Get Order Products
     * @param orderId
     */
    public void getOrderProducts(int orderId) {
        Message msg = new Message(orderId, MessageFromClient.ORDER_PRODUCTS_GET);
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
        Message msg = new Message(msgList, MessageFromClient.CUSTOMER_BALANCE_UPDATE);
        Client.clientController.getClient().handleMessageFromUI(msg,false);
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
        Message msg = new Message(msgList, MessageFromClient.CUSTOMER_CREDIT_CARD_UPDATE);
        Client.clientController.getClient().handleMessageFromUI(msg,false);

    }

    /**
     * After customer used his first order with 20% discount he is no longer a new customer
     * Updates in DB
     * @param userId the id of the current customer
     */
    public void updateNewCustomer(int userId) {
        Message msg = new Message(userId, MessageFromClient.CUSTOMER_UPDATE_NEW);
        Client.clientController.getClient().handleMessageFromUI(msg,false);
    }

    public void setStatusOrder(Order order){
        Message msg = new Message(order, MessageFromClient.ORDER_CHANGE_STATUS);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }
    public void setCancelTime(Order order){
        Message msg = new Message(order, MessageFromClient.ORDER_CANCEL_TIME);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }

    public float getBalance(int customerId) {
        Message msg = new Message(customerId, MessageFromClient.ORDER_GET_BALANCE);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
        setBalanceCustomer((float) getResponse().getData());

        return getBalanceCustomer();
    }

    public float getBalanceCustomer() {
        return currBalance;
    }

    public void setBalanceCustomer(float currBalance) {
        this.currBalance = currBalance;
    }

    public void getBranch(int userId) {
        Message msg = new Message(userId, MessageFromClient.ORDER_GET_BRANCH);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }

    public String getBranchManager() {
        return currBranch;
    }
}

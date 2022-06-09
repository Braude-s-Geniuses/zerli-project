package client;

import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import order.Order;
import order.OrderProduct;

import java.util.ArrayList;

/**
 * Controller for everything related to Orders
 */
public class OrderController extends AbstractController {

    /**
     * The delivery cost used to calculate the additional cost
     */
    public final float DELIVERY_PRICE = 20;

    /**
     * Used to store Products inserted into the cart.
     */
    private final ArrayList<OrderProduct> cart = new ArrayList<OrderProduct>();

    private final String currBranch = null;

    private Order currentOrder;

    OrderController(IMessageService service) {
        super(service);
    }


    /**
     * adds a product to the current logged in customer
     * @param orderProduct - the product to be added to cart with its quantity
     */
    public void addToCart(OrderProduct orderProduct){
        // if added order already exists in cart - adds quantity to existing OrderProduct
        for(OrderProduct op : cart) {
            if(orderProduct.getProduct().getProductId() == op.getProduct().getProductId()) {
                op.setQuantity(op.getQuantity() + orderProduct.getQuantity());
                return;
            }
        }

        // orderProduct doesn't exist - create it
        cart.add(orderProduct);
    }

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
        getService().sendToServer(ordersRequest, true);
    }

    /**
     * Fetches all customer`s orders from DB by branch
     */
    public void requestOrders(String branch){
        Message ordersRequest = new Message(branch, MessageFromClient.ORDERS_GET_BY_BRANCH);
        getService().sendToServer(ordersRequest, true);
    }

    /**
     * Get branches list from DB
     */
    public void getBranches() {
        Message msg = new Message(null, MessageFromClient.ORDER_BRANCHES_GET);
        getService().sendToServer(msg, true);
    }
    /**
     * Saves completed order in DB
     * @return
     */
    public boolean sendNewOrder() {
        Message msg = new Message(getCurrentOrder(), MessageFromClient.ORDER_CREATE_NEW);

        return (getService().sendToServer(msg, true)).getAnswer() != MessageFromServer.ORDER_CREATE_NEW_FAIL;
    }

    /**
     * Get Order Products
     * @param orderId
     */
    public void getOrderProducts(int orderId) {
        Message msg = new Message(orderId, MessageFromClient.ORDER_PRODUCTS_GET);
        getService().sendToServer(msg, true);
    }

    /**
     * Gets a customer's current balance from the server
     * @param customerId - customer id to get balance from
     * @return given customer's current balance
     */
    public float getBalance(int customerId) {
        Message msg = new Message(customerId, MessageFromClient.ORDER_GET_BALANCE);

        return (float) (getService().sendToServer(msg, true)).getData();
    }

    /**
     * Updates a customer's balance to a given balance
     * <b>Note:</b> this method is async - it will not wait for the server's response
     * @param userId - the id of the customer we want to update the balance to
     * @param balance - new balance
     */
    public void updateBalance(int userId, float balance) {
        ArrayList<Object> msgList = new ArrayList<>();
        msgList.add(userId);
        msgList.add(balance);
        Message msg = new Message(msgList, MessageFromClient.CUSTOMER_BALANCE_UPDATE);
        getService().sendToServer(msg, false);
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
        getService().sendToServer(msg, false);

    }

    /**
     * After customer used his first order with 20% discount he is no longer a new customer
     * Updates in DB
     * @param userId the id of the current customer
     */
    public void updateNewCustomer(int userId) {
        Message msg = new Message(userId, MessageFromClient.CUSTOMER_UPDATE_NEW);
        getService().sendToServer(msg, false);
    }

    /**
     * Updates an order status
     * @param order - the order to update with the new status already in the instance
     */
    public void setStatusOrder(Order order){
        Message msg = new Message(order, MessageFromClient.ORDER_CHANGE_STATUS);
        getService().sendToServer(msg, true);
    }

    /**
     * Updates an order cancel time
     * @param order - the order to update with the cancel time inside already
     */
    public void setCancelTime(Order order){
        Message msg = new Message(order, MessageFromClient.ORDER_CANCEL_TIME);
        getService().sendToServer(msg, true);
    }

    /**
     * Gets the branch a given branch manager is responsible for
     * @param userId - the branch manager to get his branch
     */
    public void getBranch(int userId) {
        Message msg = new Message(userId, MessageFromClient.ORDER_GET_BRANCH);
        getService().sendToServer(msg, true);
    }

    /**
     * Getter for <code>cart</code>
     * @return <code>cart</code>
     */
    public ArrayList<OrderProduct> getCart() {
        return cart;
    }

    /**
     * Setter for <code>currentOrder</code>
     * @param currentOrder
     */
    public void setCurrentOrder(Order currentOrder) { this.currentOrder = currentOrder; }

    /**
     * Getter for <code>currentOrder</code>
     * @return <code>currentOrder</code>
     */
    public Order getCurrentOrder() { return currentOrder; }
}

package client;

import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import order.Item;
import order.Order;
import order.OrderProduct;
import order.Product;

import java.util.ArrayList;

public class OrderController {

    public final float DELIVERY_PRICE = 20;
    /**
     * Used to store Products inserted into the cart.
     */
    private ArrayList<OrderProduct> cart = new ArrayList<OrderProduct>();
    private  Message response;
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
    public void setCart(ArrayList<OrderProduct> newCart){ this.cart = newCart; };
    public void setCurrentOrder(Order currentOrder) { this.currentOrder = currentOrder; }
    public Order getCurrentOrder() { return currentOrder; }

    public Message getResponse() {return response;}

    public void setResponse(Message response) {this.response = response; }

    /**
     * Changing the amount of specific product in cart
     * @param productName
     * @param newAmount
     * @return
     */
    public boolean changeAmountOfProduct(String productName, int newAmount){
        for (OrderProduct op : cart){
            if (op.getProduct().getName().equals(productName)){
                if (newAmount == 0){
                    cart.remove(op);
                    return cart.isEmpty();
                }
                else {
                    op.setQuantity(newAmount);
                }
            }
        }
        return cart.isEmpty();
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
}

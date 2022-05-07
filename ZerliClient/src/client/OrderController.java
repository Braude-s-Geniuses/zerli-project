package client;

import com.sun.org.apache.xpath.internal.operations.Or;
import communication.Message;
import communication.MessageFromClient;
import communication.MessageFromServer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import order.Item;
import order.Order;
import order.OrderProduct;
import order.Product;

import java.util.ArrayList;
import java.util.Observable;

public class OrderController {
    public final float DELIVERY_PRICE = 20;
    /**
     * Used to store Products inserted into the cart.
     */
    private ArrayList<OrderProduct> cart ;
    private  Message response;
    private Order currentOrder;
    public OrderController() {
        this.cart = new ArrayList<>();
    }

    public void addToCart(OrderProduct orderProduct){ //TODO return message that adding to cart was successful.
         cart.add(orderProduct);
    }

    public ArrayList<OrderProduct> getCart() {
        Product product1 = new Product(1,"Rose Flower for Itzhak Efraimov" ,20, 0, null, true,"red");
        Product product2 = new Product(2,"Flower" ,25, 0, null, false,"pink");
        product1.addFlowersToProduct(new Item(1, "Rose", "flower","red",10) ,2);
        product2.addFlowersToProduct(new Item(2, "Flower", "flower","pink",10) ,2);
        OrderProduct op2 = new OrderProduct(product2,5);
        System.out.println("product1: " +product1.toString() + "\nproduct2: " +product2.toString());
        cart.add(new OrderProduct(product1,2));
        cart.add(new OrderProduct(product2,5));


        return cart;
    }

    public void requestOrders(){
     //   Message ordersRequest = new Message(Client.clientController.getClient().getLoggedInUser().getUserId(), MessageFromClient.REQUEST_ORDERS_TABLE);
        Message ordersRequest = new Message(1, MessageFromClient.REQUEST_ORDERS_TABLE);
        Client.clientController.getClient().handleMessageFromUI(ordersRequest, true);
    }
    public void getBranches() {
        Message msg = new Message(null, MessageFromClient.REQUEST_BRANCHES);
        Client.clientController.getClient().handleMessageFromUI(msg,true);
    }

    public void setCurrentOrder(Order currentOrder) { this.currentOrder = currentOrder; }
    public Order getCurrentOrder() { return currentOrder; }

    public void setCart(ArrayList<OrderProduct> newCart){ this.cart = newCart; };

    public Message getResponse() {return response;}

    public void setResponse(Message response) {this.response = response; }

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
    public float sumOfCart(){
        float total = 0;
        for (OrderProduct op : cart){
            total += op.getProduct().getProductPrice() * op.getQuantity();
        }
        return total;
    }


    public boolean sendNewOrder() {
        Message msg = new Message(getCurrentOrder(), MessageFromClient.ADD_NEW_ORDER);
        Client.clientController.getClient().handleMessageFromUI(msg, true);
        if(response.getAnswer() == MessageFromServer.ADDED_ORDER_NOT_SUCCESSFULLY){
           return false;
        }
        return true;
    }
}

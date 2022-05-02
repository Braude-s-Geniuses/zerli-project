package client;

import com.sun.org.apache.xpath.internal.operations.Or;
import communication.Message;
import communication.MessageFromClient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import order.Item;
import order.Order;
import order.OrderProduct;
import order.Product;

import java.util.ArrayList;
import java.util.Observable;

public class OrderController {

    /**
     * Used to store Products inserted into the cart.
     */
    private ArrayList<OrderProduct> cart ;

    private ArrayList<Order> orders = new ArrayList<>();

    public OrderController() {
        this.cart = new ArrayList<>();
    }

    public void addToCart(OrderProduct orderProduct){ //TODO return message that adding to cart was successful.
         cart.add(orderProduct);
    }

    public ArrayList<OrderProduct> getCart() {
        Product product1 = new Product(1,"Rose Flower" ,20, 0, null, true,"red");
        Product product2 = new Product(2,"Flower" ,25, 0, null, false,"pink");
        product1.addFlowersToProduct(new Item(1, "Rose", "flower","red",10) ,2);
        product2.addFlowersToProduct(new Item(2, "Flower", "flower","pink",10) ,2);
        OrderProduct op2 = new OrderProduct(product2,5);
        System.out.println("product1: " +product1.toString() + "\nproduct2: " +product2.toString());
        cart.add(new OrderProduct(product1,2));
        cart.add(new OrderProduct(product2,5));

        System.out.println("cart: " + cart.toString());
        return cart;
    }

    public ArrayList<Order> requestOrders(){
        Message ordersRequest = new Message(Client.clientController.getClient().getLoggedInUser().getUserId(), MessageFromClient.REQUEST_ORDERS_TABLE);
        //System.out.println(Client.clientController.getClient().getLoggedInUser());
        Client.clientController.getClient().handleMessageFromUI(ordersRequest, true);

        return getOrders();
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

}

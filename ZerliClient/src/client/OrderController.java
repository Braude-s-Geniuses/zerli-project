package client;

import communication.Message;
import communication.MessageFromClient;
import order.Item;
import order.Order;
import order.OrderProduct;
import order.Product;

import java.util.ArrayList;

public class OrderController {

    /**
     * Used to store Products inserted into the cart.
     */
    private ArrayList<OrderProduct> cart ;

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
        Message ordersRequest = new Message(null, MessageFromClient.REQUEST_ORDERS_TABLE);
        Client.clientController.getClient().handleMessageFromUI(ordersRequest, true);
        return Client.clientController.getClient().getOrders();
    }




}

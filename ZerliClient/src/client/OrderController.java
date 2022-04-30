package client;

import order.Item;
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
        Product product1 = new Product(1,"Rose Flower" ,20, 0, null, "red");
        product1.addFlowersToProduct(new Item(1, "Rose", "flower","red",10) ,2);

        cart.add(new OrderProduct(product1,2));
        return cart;
    }




}

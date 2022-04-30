package client;

import order.Order;
import order.Product;

import java.util.ArrayList;

public class OrderClientController {

    /**
     * Used to store Products inserted into the cart.
     */
    private ArrayList<Product> cart = null;

    public void addToCart(Product product){ //TODO return message that adding to cart was successful.
        cart.add(product);
    }

    public ArrayList<Product> getCart() {
        return cart;
    }




}

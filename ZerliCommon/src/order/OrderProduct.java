package order;

import product.Product;

import java.io.Serializable;

/**
 * Describes a Product in an Order
 */
public class OrderProduct implements Serializable {
    /**
     * The product in the order
     */
    private Product product;

    /**
     * The quantity of given product in the order
     */
    private int quantity;

    public OrderProduct(){}

    public OrderProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}

package order;

import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.HashMap;

public class Product implements Serializable {
    private int productId;
    private String name;

    private HashMap<Item, Integer> items = new HashMap<Item, Integer>();
    //private int itemId;
    //private int quantity;
    private float productPrice;
    private float discountPrice;
    private ImageView image;

    public Product(int productId, Item item, int quantity, float price, float discountPrice, ImageView image) {
        this.productId = productId;
        this.items.put(item, quantity);
        //this.quantity = quantity;
        this.productPrice = price;
        this.discountPrice = discountPrice;
        this.image = image;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public float getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(float discountPrice) {
        this.discountPrice = discountPrice;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public HashMap<Item, Integer> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", items=" + items +
                ", productPrice=" + productPrice +
                ", discountPrice=" + discountPrice +
                ", image=" + image +
                '}';
    }
}

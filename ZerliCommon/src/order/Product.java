package order;

import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.HashMap;

public class Product implements Serializable {
    private int productId;
    private String name;
    private HashMap<Item, Integer> items = new HashMap<Item, Integer>();
    private float productPrice;
    private float discountPrice;
    private ImageView image;

    private String dominantColor;

    public Product(int productId, String name, float price, float discountPrice, ImageView image, String dominantColor) {
        this.productId = productId;
        this.name = name;
        this.productPrice = price;
        this.discountPrice = discountPrice;
        this.image = image;
        this.dominantColor = dominantColor;
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

    public void setItems(Item item ,Integer quantity) {
        items.replace(item, quantity);
    }

    public void addFlowersToProduct(Item item , Integer quantity){
        this.items.put(item, quantity);
    }


    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    public String getDominantColor() {
        return dominantColor;
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
                ", dominantColor='" + dominantColor + '\'' +
                '}';
    }
}

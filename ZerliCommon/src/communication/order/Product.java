package communication.order;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.HashMap;

public class Product implements Serializable {
    private int productId;
    private String name;
    private float price;
    private float discountPrice;
    private Image image;


    public Product (){}
    public Product(int productId, String name, float price, float discountPrice, Image image) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.discountPrice = discountPrice;
        this.image = image;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(float discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name=" + name +
                ", price=" + price +
                ", discountPrice=" + discountPrice +
                ", image=" + image +
                '}';
    }
}

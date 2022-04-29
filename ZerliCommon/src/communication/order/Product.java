package communication.order;

import javafx.scene.image.ImageView;

import java.io.Serializable;

public class Product implements Serializable {
    private int productId;
    private int itemId;
    private int quantity;
    private float price;
    private float discountPrice;
    private ImageView image;

    public Product(int productId, int itemId, int quantity, float price, float discountPrice, ImageView image) {
        this.productId = productId;
        this.itemId = itemId;
        this.quantity = quantity;
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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", itemId=" + itemId +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discountPrice=" + discountPrice +
                ", image=" + image +
                '}';
    }
}

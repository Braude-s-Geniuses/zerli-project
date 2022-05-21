package order;

import javax.sql.rowset.serial.SerialBlob;
import java.io.Serializable;
import java.util.HashMap;

public class Product implements Serializable {
    private int productId;
    private String name;
    private HashMap<Item, Integer> items = new HashMap<Item, Integer>();
    private float price;
    private float discountPrice;
    private SerialBlob image;
    private boolean customMade;
    private String dominantColor;
    private boolean inCatalog;

    public Product() {

    }

    public Product(String name, HashMap<Item, Integer> items, float price, float discountPrice, SerialBlob image, boolean customMade, String dominantColor) {
        this.name = name;
        this.items = items;
        this.price = price;
        this.discountPrice = discountPrice;
        this.image = image;
        this.customMade = customMade;
        this.dominantColor = dominantColor;
    }

    public Product(int productId, String name, float price, float discountPrice, SerialBlob image, boolean customMade, String dominantColor, boolean inCatalog) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.discountPrice = discountPrice;
        this.image = image;
        this.customMade = customMade;
        this.dominantColor = dominantColor;
        this.inCatalog = inCatalog;
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

    public HashMap<Item, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<Item, Integer> items) {
        this.items = items;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String priceToString() {
        return price + " \u20AA";
    }
    public String discountPriceToString() {
        return discountPrice + " \u20AA";
    }

    public float getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(float discountPrice) {
        this.discountPrice = discountPrice;
    }

    public SerialBlob getImage() {
        return image;
    }

    public void setImage(SerialBlob image) {
        this.image = image;
    }

    public boolean isCustomMade() {
        return customMade;
    }

    public void setCustomMade(boolean customMade) {
        this.customMade = customMade;
    }

    public String customMadeToString(){
        return customMade == false ? "Premade" : "Custom Made";
    }

    public String getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    public boolean isInCatalog() {
        return inCatalog;
    }

    public void setInCatalog(boolean inCatalog) {
        this.inCatalog = inCatalog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return getProductId() == product.getProductId();
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", items=" + items +
                ", price=" + price +
                ", discountPrice=" + discountPrice +
                ", image=" + image +
                ", customMade=" + customMade +
                ", dominantColor='" + dominantColor + '\'' +
                '}';
    }

}

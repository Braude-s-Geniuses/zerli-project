package product;

import javax.sql.rowset.serial.SerialBlob;
import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents a Product in the System.
 * A Product is shown in the Catalog and can be purchased by a Customer(s) multiple times
 * A Product comprises Item(s)
 */
public class Product implements Serializable {
    /**
     * The ID of the Product in the database
     */
    private int productId;

    /**
     * The name of the Product
     */
    private String name;

    /**
     * Every Item in the Product and its respective quantity in it
     */
    private HashMap<Item, Integer> items = new HashMap<Item, Integer>();

    /**
     * The full price of the Product
     */
    private float price;

    /**
     * The Product's discount price (if applicable)
     */
    private float discountPrice;

    /**
     * The Product's picture
     */
    private SerialBlob image;

    /**
     * If the Product consists a single Item with quantity of 1
     * e.g. A single Red Rose is customMade  true
     * A Roses Bouquet is customMade  false
     */
    private boolean customMade;

    /**
     * The color to be displayed as the main color of the Product
     */
    private String dominantColor;

    /**
     * If the Product should be shown in the catalog
     */
    private boolean inCatalog = true;

    /**
     * If the Product was made by a Customer using the Product Builder feature
     */
    private boolean customerProduct = false;

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

    public boolean isCustomerProduct() {
        return customerProduct;
    }

    public void setCustomerProduct(boolean customerProduct) {
        this.customerProduct = customerProduct;
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
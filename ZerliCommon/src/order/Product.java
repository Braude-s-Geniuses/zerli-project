package order;

import java.io.Serializable;
import java.util.HashMap;

public class Product implements Serializable {
    private int productId;
    private String name;
    private float price;
    private float discountPrice;
    private String image;
    private HashMap<Item,Integer> items = new HashMap<>();
    private boolean customMade;
    private String dominantColor;

    public Product() {
    }



    public Product(int productId, String name, float price, float discountPrice, String image, HashMap<Item,Integer> items, boolean customMade,String dominantColor) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.discountPrice = discountPrice;
        this.image = image;
        this.items = items;
        this.customMade = customMade;
        this.dominantColor = dominantColor;
    }

    public Product(int productId, String name, float price, float discountPrice, String image, boolean customMade, String dominantColor) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.discountPrice = discountPrice;
        this.image = image;
        this.customMade = customMade;
        this.dominantColor = dominantColor;
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

    public String priceToString(){
        return String.valueOf(price) + " \u20AA";
    }

    public float getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(float discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HashMap<Item, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<Item, Integer> items) {
        this.items = items;
    }

    public String getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    public boolean getCustomMade(){
        return customMade;
    }

    public void setCustomMade(boolean customMade) {
        this.customMade = customMade;
    }

    public String customMadeToString(){
        return customMade == false ? "Premade" : "Custom Made";
    }

    public void addFlowersToProduct(Item item , Integer quantity){
        this.items.put(item, quantity);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name=" + name +
                ", price=" + price +
                ", discountPrice=" + discountPrice +
                ", image=" + image +
                ", items=" + items +
                ", dominantColor=" + dominantColor +
                ", customMade=" + customMade +
                '}';
    }
}

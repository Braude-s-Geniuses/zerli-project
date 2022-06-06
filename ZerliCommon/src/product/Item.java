package product;

import java.io.Serializable;

/**
 * The class is used to describe an Item in the system
 * <b>Important Note:</b> an Item is used internally to compose a Product.
 * Meaning, a Customer can only buy a Product, therefore: a Product comprises item(s).
 */
public class Item implements Serializable {

    /**
     * The ID of the Item in the database
     */
    private int itemId;

    /**
     * The name of the Item
     */
    private String name;

    /**
     * The type of the Item (e.g. Leaf, Flower, Branch, Chocolate...)
     */
    private String type;

    /**
     * The color of the Item
     */
    private String color;

    /**
     * The item base price
     */
    private float price;

    public Item(int itemId, String name, String type, String color, float price) {
        this.itemId = itemId;
        this.name = name;
        this.type = type;
        this.color = color;
        this.price = price;
    }

    public Item(String name, String type, String color, float price) {
        this.name = name;
        this.type = type;
        this.color = color;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return getName();
    }

}

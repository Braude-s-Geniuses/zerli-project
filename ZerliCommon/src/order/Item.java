package order;

import java.io.Serializable;

public class Item implements Serializable {

    private int itemId;
    private String name;
    private String type;
    private String color;
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

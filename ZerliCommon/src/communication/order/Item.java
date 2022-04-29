package communication.order;

import java.io.Serializable;

public class Item implements Serializable {

    private int itemId;
    private String name;
    private String type;
    private String color;

    public Item(int itemId, String name, String type, String color) {
        this.itemId = itemId;
        this.name = name;
        this.type = type;
        this.color = color;
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

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}

package common;

import java.io.Serializable;
import java.sql.Timestamp;

public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	private int orderNumber;
	private double price;
	private String greetingCardString;
	private String color;
	private String dOrder;
	private String shop;
	private Timestamp deliveryDate;
	private Timestamp orderDate; 

	public Order(int orderNumber, double price, String greetingCardString, String color, String dOrder, String shop,
			Timestamp deliveryDate, Timestamp orderDate) {
		super();
		this.setOrderNumber(orderNumber);
		this.setPrice(price);
		this.setGreetingCardString(greetingCardString);
		this.setColor(color);
		this.setDOrder(dOrder);
		this.setShop(shop);
		this.setDeliveryDate(deliveryDate);
		this.setOrderDate(orderDate);
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double f) {
		this.price = f;
	}

	public String getGreetingCardString() {
		return greetingCardString;
	}

	public void setGreetingCardString(String greetingCardString) {
		this.greetingCardString = greetingCardString;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getDOrder() {
		return dOrder;
	}

	public void setDOrder(String dOrder) {
		this.dOrder = dOrder;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public Timestamp getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Timestamp deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Timestamp getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}

	@Override
	public String toString() {
		return "Order [orderNumber=" + orderNumber + ", price=" + price + ", greetingCardString=" + greetingCardString
				+ ", color=" + color + ", dOrder=" + dOrder + ", shop=" + shop + ", deliveryDate=" + deliveryDate
				+ ", orderDate=" + orderDate + "]";
	}

}

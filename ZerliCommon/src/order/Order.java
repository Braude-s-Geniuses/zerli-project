package order;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * The class is used to describe an Order made by a Customer
 */
public class Order implements Serializable {
    /**
     * The ID of the order within the database
     */
    private int orderId;

    /**
     * The ID of the customer who made the order within the database
     */
    private int customerId;

    /**
     * The branch name where the customer wants the order to be made in
     */
    private String branch;

    /**
     * The order's current status
     */
    private OrderStatus orderStatus;

    /**
     * An optional greeting card message that can be added by customer when placing an order
     */
    private String greetingCard;

    /**
     * The price of the order before any discounts applied
     */
    private float price;

    /**
     * The price of order after discounts of all products (if any)
     */
    private float discountPrice;

    /**
     * The date time when the order was placed
     */
    private Timestamp orderDate;

    /**
     * The date time of when the customer wants to receive the order
     * <b>Note:</b> changes to ACTUAL delivery time once the order status is COMPLETED
     */
    private Timestamp deliveryDate;

    /**
     * The address to delivery the order to (if applicable)
     */
    private String deliveryAddress;

    /**
     * The recipient of the order (Same as customer/Other - provided by him)
     */
    private String recipientName;

    /**
     * The recipient's phone (Same as customer/Other - provided by him)
     */
    private String recipientPhone;

    /**
     * The date time of when the customer started the cancellation process
     */
    private Timestamp cancelTime;

    /**
     * The products included in this order with the quantity of each
     */
    private ArrayList<OrderProduct> productList;



    public Order(){}

    public Order(int orderId, int customerId, String branch, OrderStatus orderStatus, String greetingCard, float price,
                 float discountPrice, Timestamp orderDate, Timestamp deliveryDate, String deliveryAddress, String recipientName,
                 String recipientPhone, Timestamp cancelTime, ArrayList<OrderProduct> productList) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.branch = branch;
        this.orderStatus = orderStatus;
        this.greetingCard = greetingCard;
        this.price = price;
        this.discountPrice = discountPrice;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.deliveryAddress = deliveryAddress;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.cancelTime = cancelTime;
        this.productList = productList;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = OrderStatus.valueOf(orderStatus);
    }
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    public String getGreetingCard() {
        return greetingCard;
    }

    public void setGreetingCard(String greetingCard) {
        this.greetingCard = greetingCard;
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

    public String discountPriceToString(){ return discountPrice + " \u20AA";}

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }

    public String orderDateToString() {
        return new SimpleDateFormat("dd/MM/yyyy").format(orderDate);
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public ArrayList<OrderProduct> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<OrderProduct> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", customerId=" + customerId +
                ", branch='" + branch + '\'' +
                ", orderStatus=" + orderStatus +
                ", greetingCard='" + greetingCard + '\'' +
                ", price=" + price +
                ", discountPrice=" + discountPrice +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", recipientPhone='" + recipientPhone + '\'' +
                ", cancelTime=" + cancelTime +
                ", productList=" + productList +
                '}';
    }
}

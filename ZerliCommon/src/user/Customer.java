package user;

import java.util.ArrayList;

/**
 * This class is used to represent a User of type Customer in the database
 */
public class Customer extends User {
    /**
     * Determines if the customer is blocked from placing Orders
     */
    private boolean blocked;

    /**
     * Stores the Credit Card's number
     */
    private String creditCard;

    /**
     * Stores the Credit Card's exp date
     */
    private String expDate;

    /**
     * Stores the Credit Card's CVV
     */
    private String cvv;

    /**
     * The Customer's account balance where he will be credited for refunds
     * and can use them as a form of payment for future purchases
     */
    private float balance = 0;

    /**
     * Determines if the Customer has placed his first order
     * On first order, a 20% Discount will be applied automatically on his order
     */
    private boolean newCustomer;

    public Customer(int userId, String username, String password, UserType userType, boolean loggedIn, String firstName, String lastName, String id, String email, String phone) {
        super(userId, username, password, userType, loggedIn, firstName, lastName, id, email, phone);
    }

    public Customer(User user) {
        super(user.getUserId(),user.getUsername(),user.getPassword(),user.getUserType(),user.isLoggedIn(),
                user.getFirstName(),user.getLastName(),user.getId(),user.getEmail(),user.getPhone());
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public ArrayList<String> getCardDetails(){
        ArrayList<String> cardDetails = new ArrayList<>();
        cardDetails.add(creditCard);
        cardDetails.add(expDate);
        cardDetails.add(cvv);
        return cardDetails;
    }
    public boolean getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(boolean newCustomer) {
        this.newCustomer = newCustomer;
    }
    @Override
    public String toString() {
        return "Customer{" +
                "blocked=" + blocked +
                ", creditCard='" + creditCard + '\'' +
                ", expDate='" + expDate + '\'' +
                ", cvv='" + cvv + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Customer customer = (Customer) o;
        return blocked == customer.blocked && customer.balance==balance && newCustomer == customer.newCustomer && creditCard.equals(customer.creditCard) && expDate.equals(customer.expDate) && cvv.equals(customer.cvv);
    }

}
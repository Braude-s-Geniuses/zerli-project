package user;

import java.util.ArrayList;

public class Customer extends User {
    private boolean blocked;
    private String creditCard;
    private String expDate;
    private String cvv;
    private float balance = 0;

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
}
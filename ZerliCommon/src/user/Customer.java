package user;

public class Customer extends User {
    private boolean blocked;
    private String creditCard;
    private String expDate;
    private String cvv;
    private float balance;

    public Customer(int userId, String username, String password, UserType userType, boolean loggedIn, String firstName, String lastName, String id, String email, String phone) {
        super(userId, username, password, userType, loggedIn, firstName, lastName, id, email, phone);
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

}

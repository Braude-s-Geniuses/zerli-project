package user;

import java.io.Serializable;

/**
 * This class will store the base User information shared by all UserTypes
 */
public class User implements Serializable {
    /**
     * The ID of the User in the database
     */
    private int userId;

    /**
     * The username that will be used as login credentials
     */
    private String username;

    /**
     * The password that will be used as login credentials
     */
    private String password;

    /**
     * The type of the User (e.g. Customer)
     */
    private UserType userType;

    /**
     * The state of the User in the system.
     */
    private boolean loggedIn;

    /**
     * The first name of the User
     */
    private String firstName;

    /**
     * The last name of the User
     */
    private String lastName;

    /**
     * The government ID of the User
     */
    private String id;

    /**
     * The email of the User
     */
    private String email;

    /**
     * The phone number of the User
     */
    private String phone;

    public User(int userId, String username, String password, UserType userType, boolean loggedIn,
                String firstName, String lastName, String id, String email, String phone) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.loggedIn = loggedIn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.phone = phone;
    }

    public User() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userType=" + userType +
                ", loggedIn=" + loggedIn +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        User user = (User) o;

        if (!getUsername().equals(user.getUsername())) return false;
        return getId().equals(user.getId()) && getUserType().equals(user.getUserType());
    }
}
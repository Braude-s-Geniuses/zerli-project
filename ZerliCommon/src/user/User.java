package user;

import java.io.Serializable;

/**
 *  Each user has a class instance.
 *  The class will store data on the user in the DB.
 *  This information helps for secure connection, execution of customer and manager actions.
 */

// Need to check what happens if a customer has not put in a credit card and allow the manager.

public class User implements Serializable {
    private int userId;
    private String username;
    private String password;
    private UserType userType;
    private boolean loggedIn;
    private String firstName;
    private String lastName;
    private String id;
    private String email;
    private String phone;

    public User(int userId, String username, String password,UserType userType, boolean loggedIn,
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
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getUserId() != user.getUserId()) return false;
        if (!getUsername().equals(user.getUsername())) return false;
        return getId().equals(user.getId());
    }
}

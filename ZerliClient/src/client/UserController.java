package client;

import communication.Message;
import user.BranchEmployee;
import user.Customer;
import user.User;
import user.UserType;

import java.util.List;

import static communication.MessageFromClient.*;

/**
 * The role of this class is to present to provide a secure connectivity to the app.
 * By filtering, associating and documenting customers entry.
 */

public class UserController extends AbstractController {

    /**
     *  Used to store user fetched from the server once <code>userController.tryToLogin()</code> is called.
     */
    private User loggedInUser = null;
    private User userForInformation;
    private Customer customerForInformation;
    private BranchEmployee branchEmployeeForInformation;

    public UserController(IMessageService service) {
        super(service);
    }

    /**
     * This function getting username and password from the client and check if it exists in DB,
     * if exists provides a suitable selection menu,
     * otherwise prints an error message on the screen.
     *
     * @param username, username from the client.
     * @param password, password from the client.
     * @return user-appropriate permission.
     */
    public Message login(String username, String password){
        User newUser = new User(0,username,password,null,false,null,null,null,null,null);
        Message requestLogin = new Message();
        requestLogin.setTask(LOGIN_REQUEST);
        requestLogin.setData(newUser);

        return getService().sendToServer(requestLogin, true);
    }

    /**
     * This method is used to log a user out of the system by sending a logout
     * request to the server
     * <b>Note:</b> all the Client's Application Controllers are also re-instantiated upon calling this function
     * @param id - the user ID needs to be logged out
     */
    public void logout(int id){
        Message requestLogout = new Message();
        requestLogout.setTask(LOGOUT_REQUEST);
        requestLogout.setData(id);

        getService().sendToServer(requestLogout, true);
        loggedInUser = null;

        /* resets all static controller instances except ClientController so the data is completely deleted between connected users sessions */
        Client.initController(null, false);
    }

    /**
     * This method prepares MessageFromClient to change branch employee permissions
     * and sends it to server using clientController
     * @param branchEmployee
     * @return the response from server
     */
    public boolean changeBranchEmployeePermissions(BranchEmployee branchEmployee)
    {
        Message requestChangePermission = new Message();
        requestChangePermission.setTask(EMPLOYEE_PERMISSION_CHANGE);
        requestChangePermission.setData(branchEmployee);

        return  (boolean) (getService().sendToServer(requestChangePermission, true)).getData();
    }

    /**
     * This method prepares MessageFromClient to get user information
     * and sends it to server using clientController
     * @param userIdAndManagerId
     * @return user Object that returns from server
     */
    public User getUserInformation(List<String> userIdAndManagerId) {
        Message getUserInformation = new Message();
        getUserInformation.setTask(USER_INFORMATION_GET);
        getUserInformation.setData(userIdAndManagerId);

        getService().sendToServer(getUserInformation, true);

        return userForInformation;
    }

    /**
     * This method prepares MessageFromClient to create new customer
     * and sends it to server using clientController
     * @param newCustomer
     * @return the response from server
     */
    public boolean createNewCustomer(Customer newCustomer) {
        Message requestCreateNewCustomer = new Message();
        requestCreateNewCustomer.setTask(CUSTOMER_CREATE_NEW);
        requestCreateNewCustomer.setData(newCustomer);


        Client.clientController.sendMail("[SMS/EMAIL SIMULATION] To: " + newCustomer.getEmail() + " | Message: You are now a Zerli Customer! To celebrate, we are giving you a 20% Discount on your first order. No code necessary.");

        return (boolean) (getService().sendToServer(requestCreateNewCustomer, true)).getData();
    }

    /**
     * This method prepares MessageFromClient to get freeze customer
     * and sends it to server using clientController
     * @param customerToFreeze
     * @return the response from server
     */
    public boolean FreezeCustomer(Customer customerToFreeze) {
        Message requestFreezeCustomer = new Message();
        requestFreezeCustomer.setTask(CUSTOMER_FREEZE);
        requestFreezeCustomer.setData(customerToFreeze);

        return (boolean) (getService().sendToServer(requestFreezeCustomer, true)).getData();
    }

    /**
     * This method sends a request to the server to fetch a customerId's email
     * <b>Note:</b> the response from the server is located in <code>this.getResponse()</code>
     * @param customerId - the customer's email we would like to get
     */
    public void getCustomerEmail(int customerId) {
        Message message = new Message(customerId, CUSTOMER_GET_EMAIL);
        getService().sendToServer(message, true);
    }

    /**
     * This method prepares MessageFromClient to get user permissions
     * and sends it to server using clientController
     * @param user
     */
    public void getPermissions(User user) {
        Message getUserPermission = new Message();
        getUserPermission.setTask(GET_USER_PERMISSION);
        getUserPermission.setData(user);

        getService().sendToServer(getUserPermission, true);
    }

    /**
     * This method sets the data Object in the right fields
     * @param data
     */
    public void setUserTypeForInformation(Object data) {
        userForInformation = (User) data;
        if(userForInformation != null) {
            switch (userForInformation.getUserType()) {
                case CUSTOMER:
                    customerForInformation = (Customer) data;
                    break;
                case BRANCH_EMPLOYEE:
                    branchEmployeeForInformation = (BranchEmployee) data;
                    break;
            }
        }
    }

    /**
     *  Getter for <code>loggedInUser</code>.
     *
     * @return <code>loggedInUser</code>.
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Setter for <code>loggedInUser</code>
     * @param loggedInUser
     */
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * Getter for <code>userForInformation</code>
     * @return userForInformation
     */
    public User getUserForInformation() {
        return userForInformation;
    }

    /**
     * Setter for <code>userForInformation</code>
     * @param userForInformation
     */
    public void setUserForInformation(User userForInformation) {
        this.userForInformation = userForInformation;
    }

    /**
     * Getter for customerForInformation
     * @return customerForInformation
     */
    public Customer getCustomerForInformation() {
        return customerForInformation;
    }

    /**
     * Setter for customerForInformation
     * @param customerForInformation
     */
    public void setCustomerForInformation(Customer customerForInformation) {
        this.customerForInformation = customerForInformation;
    }

    /**
     * Getter for <code>branchEmployeeForInformation</code>
     * @return branchEmployeeForInformation
     */
    public BranchEmployee getBranchEmployeeForInformation() {
        return branchEmployeeForInformation;
    }

    /**
     * Setter for <code>branchEmployeeForInformation</code>
     * @param branchEmployeeForInformation
     */
    public void setBranchEmployeeForInformation(BranchEmployee branchEmployeeForInformation) {
        this.branchEmployeeForInformation = branchEmployeeForInformation;
    }

}

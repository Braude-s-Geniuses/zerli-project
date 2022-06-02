package client;

import communication.Message;
import user.BranchEmployee;
import user.Customer;
import user.User;

import java.util.List;

import static communication.MessageFromClient.*;

/**
 * The role of this class is to present to provide a secure connectivity to the app.
 * By filtering, associating and documenting customers entry.
 */

public class UserController {

    private Message response = null;

    /**
     *  Used to store user fetched from the server once <code>userController.tryToLogin()</code> is called.
     */
    private User loggedInUser = null;
    private User userForInformation;
    private Customer customerForInformation;
    private BranchEmployee branchEmployeeForInformation;

    /**
     * This function getting username and password from the client and check if it exists in DB,
     * if exists provides a suitable selection menu,
     * otherwise prints an error message on the screen.
     *
     * @param username, username from the client.
     * @param password, password from the client.
     * @return user-appropriate permission.
     */
    public void login(String username, String password){
        User newUser = new User(0,username,password,null,false,null,null,null,null,null);
        Message requestLogin = new Message();
        requestLogin.setTask(LOGIN_REQUEST);
        requestLogin.setData(newUser);

        Client.clientController.getClient().handleMessageFromUI(requestLogin,true);
    }

    public void logout(int id){
        Message requestLogout = new Message();
        requestLogout.setTask(LOGOUT_REQUEST);
        requestLogout.setData(id);
        Client.clientController.getClient().handleMessageFromUI(requestLogout,true);

        loggedInUser = null;

        /* resets all static controller instances except ClientController so the data is completely deleted between connected users sessions */
        Client.initController(null, false);
    }

    public boolean changeBranchEmployeePermissions(BranchEmployee branchEmployee)
    {
        Message requestChangePermission = new Message();
        requestChangePermission.setTask(CHANGE_PERMISSION);
        requestChangePermission.setData(branchEmployee);

        Client.clientController.getClient().handleMessageFromUI(requestChangePermission,true);

        return  (boolean) response.getData();
    }

    public User getUserInformation(List<String> userIdAndManagerId) {
        Message getUserInformation = new Message();
        getUserInformation.setTask(GET_USER_INFORMATION);
        getUserInformation.setData(userIdAndManagerId);

        Client.clientController.getClient().handleMessageFromUI(getUserInformation,true);

        return userForInformation;
    }

    public boolean createNewCustomer(Customer newCustomer) {
        Message requestCreateNewCustomer = new Message();
        requestCreateNewCustomer.setTask(CREATE_NEW_CUSTOMER);
        requestCreateNewCustomer.setData(newCustomer);

        Client.clientController.getClient().handleMessageFromUI(requestCreateNewCustomer,true);

        return  (boolean) response.getData();
    }

    public boolean FreezeCustomer(Customer customerToFreeze) {
        Message requestFreezeCustomer = new Message();
        requestFreezeCustomer.setTask(FREEZE_CUSTOMER);
        requestFreezeCustomer.setData(customerToFreeze);

        Client.clientController.getClient().handleMessageFromUI(requestFreezeCustomer,true);

        return  (boolean) response.getData();
    }

    public User getUserForInformation() {
        return userForInformation;
    }

    public void setUserForInformation(User userForInformation) {
        this.userForInformation = userForInformation;
    }

    public Customer getCustomerForInformation() {
        return customerForInformation;
    }

    public void setCustomerForInformation(Customer customerForInformation) {
        this.customerForInformation = customerForInformation;
    }

    public BranchEmployee getBranchEmployeeForInformation() {
        return branchEmployeeForInformation;
    }

    public void setBranchEmployeeForInformation(BranchEmployee branchEmployeeForInformation) {
        this.branchEmployeeForInformation = branchEmployeeForInformation;
    }

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

    public Message getResponse() {
        return response;
    }

    public void setResponse(Message response) {
        this.response = response;
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
}

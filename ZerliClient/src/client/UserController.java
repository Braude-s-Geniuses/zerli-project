package client;

import communication.Message;
import communication.MessageFromServer;
import user.Customer;
import user.User;
import user.UserType;

import static communication.MessageFromClient.LOGIN_REQUEST;
import static communication.MessageFromClient.LOGOUT_REQUEST;

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

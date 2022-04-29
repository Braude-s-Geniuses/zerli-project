package client;

import communication.Message;
import user.User;

import java.io.IOException;

import static communication.MessageFromClient.LOGIN_REQUEST;

/**
 * The role of this class is to present to provide a secure connectivity to the app.
 * By filtering, associating and documenting customers entry.
 */

public class LoginClientController {
    ZerliClient client;

    public LoginClientController(){
        client = Client.clientController.getClient();
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

    public User tryToLogin(String username,String password){
        User newUser = new User(0,username,password,null,false,null,null,null,null,null);
        Message requestLogin = new Message();
        requestLogin.setTask(LOGIN_REQUEST);
        requestLogin.setData(newUser);
        client.handleMessageFromUI(requestLogin,true);
        newUser = client.getUser();

        return newUser;
    }

}

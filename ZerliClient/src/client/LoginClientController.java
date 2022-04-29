package client;

import communication.Message;
import user.User;

import java.io.IOException;

import static communication.MessageFromClient.LOGIN_REQUEST;

public class LoginClientController {
    ZerliClient client;

    public LoginClientController(){
        client = Client.clientController.getClient();
    }

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

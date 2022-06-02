package client;

import communication.Message;

public class AbstractController {

    /**
     * stores the response from the server to a client's request in its relevant inheritor
     */
    private Message response;

    /**
     * getter for response
     * @return
     */
    public Message getResponse() {
        return response;
    }

    /**
     * setter for response
     * @param response
     */
    public void setResponse(Message response) {
        this.response = response;
    }
}

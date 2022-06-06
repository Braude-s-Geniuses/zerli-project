package client;

import communication.Message;

/**
 * Implementation of the steps taken when a message is being sent to server
 * and when receiving a message from it
 */
public class MessageService implements IMessageService {

    /**
     * stores the response from the server to a client's request in its relevant inheritor
     */
    private Message response;

    /**
     * this method is a wrapper for <code>handleMessageFromUI</code> inorder to enable
     * testing of functionality using it.
     * @param message - message to send to server
     * @param await - if the client should wait for server's response
     * @return server response if exists; null otherwise
     * @see {@link client.ZerliClient#handleMessageFromUI(Object, boolean)}
     */
    @Override
    public Message sendToServer(Object message, boolean await) {
        Client.clientController.getClient().handleMessageFromUI(message, await);

        if(!await)
            return null;

        return getResponse();
    }

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
